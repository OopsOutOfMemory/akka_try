package com.shengli.alpha.grinder

import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.HashSet
import org.json4s.jackson.JsonMethods.parse
import org.json4s.jvalue2extractable
import org.json4s.jvalue2monadic
import org.json4s.string2JsonInput
import com.shengli.etl.ff.mapping.LogRulesMapping
import com.shengli.etl.ff.strategies.CommonStrategy
import com.shengli.etl.ff.strategies.FieldDesc
import com.shengli.etl.ff.strategies.RegexStrategy
import com.shengli.etl.ff.strategies.Rule
import com.shengli.etl.ff.strategies.SingleValueStrategy
import com.shengli.etl.ff.strategies.OutputStrategy
import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.actorRef2Scala
import com.shengli.etl.ff.strategies.OutputStrategy
import com.shengli.etl.ff.strategies.OneToManyOutputStrategy
import com.shengli.etl.ff.strategies.OneToOneOutputStrategy
import com.shengli.etl.ff.strategies.OutputStrategy
import com.shengli.etl.ff.strategies.OneToOneOutputStrategy

//
case class Meat(rule_exp : String, log_name : String, rule : Rule)

object GrinderApplication  extends App {
	val _system = ActorSystem("Grinder")
	val readerActor = _system.actorOf(Props[FileReaderActor], name = "reader")
	//Many Meat need to be grind
	var meatList : ArrayBuffer[Meat] = _
	var unique_logs : List[String] = _ 
	val writers = new ArrayBuffer[ActorRef]()
	val dateString = "2014-08-28"
	val sources = scala.io.Source.fromFile("item_log.txt")
	val app = "ff14"
	 
	//start process
	initialize()
	initializeActors(unique_logs)
	
	//process input
	sources.getLines.foreach{line=>
	    //got app and data from line first, then process line
	  	meatList.foreach{ meat=>
	  	  if(line.contains(meat.rule_exp)) {
	  	    grindMeat(line, meat)
	  	  }
	  	}
	}
	
	/**
*************************************Initialize related taks *************************************************************
	 */
	def initialize() {
	  //load mappings, load field rules, initial actors
	  //transform rules which load from database or files from json format to objects
	  val listrules = loadRules("task.json") 
	  
	  //transform rules to rule_exp, log_name, rule, main transform to object
	  meatList = formatRuleLog(listrules)
	  
	  //get the  unique log_name, so we can create file handler to write to.
	  unique_logs = uniqueLogs(List("ff14"))
	  
	}
	
	def loadRules(rulePath : String) = {
	  val ruleList = parseRules(rulePath)
	  ruleList
	}
	
	def getRuleLogMapping (app : String) = {
	  val rule_log_map = LogRulesMapping.mappings.get(app).get
	  rule_log_map
	}
	
	/*
	 *This function perform a Meat as (rule_exp, log_name, rule: Rule, opt: outputStrategy) so we can extract
	 * 
	 */
	def formatRuleLog(ruleList : ArrayBuffer[Rule]) : ArrayBuffer[Meat] = {
	     val tupleList = ArrayBuffer[Meat]()
	     ruleList.foreach { rule=>
	          val rule_log_mapping = getRuleLogMapping(rule.app_name)
	          val log_name = rule_log_mapping.get(rule.rule_expression).get
	          tupleList += new Meat(rule.rule_expression, log_name, rule)
	     }  
	     tupleList
	}
	/*
	 * pase a json desc to a rule object
	 */
	def parseRules(rulePath : String) = {
	  //should do this to make json4s avaliable
	  implicit val formats = org.json4s.DefaultFormats
	  val jsonSrc = scala.io.Source.fromFile(rulePath).getLines.mkString;
	  val json = parse(jsonSrc)
	  var ruleList : ArrayBuffer[Rule] = ArrayBuffer[Rule]()
	  json.children.foreach{ child=>
	      val app_name = (child \ "app_name").extract[String]
		  val rule_exp = (child \ "rule_expression").extract[String]
		  val fieldList = (child \ "fields_desc").extract[List[FieldDesc]]
	      val output = (child \ "output_strategy").extract[OutputStrategy]
//	      val output_strategy = output.name match {
//	        case "one2one"=>
//	          output.asInstanceOf[OneToOneOutputStrategy]
//	        case "one2many"=>
//	          output.asInstanceOf[OneToManyOutputStrategy]
//	      }
		  fieldList foreach println
		  val rule = new Rule(app_name, rule_exp,fieldList,output)
		  ruleList+=rule
	  }
	  ruleList
	}
	
	def uniqueLogs(apps : List[String]) = {
	    val set = new HashSet[String]
	    apps.foreach{app=>
	      val rlmp = getRuleLogMapping(app)
	      rlmp.valuesIterator.foreach{
	      set += _
	      }
	    }
	    set foreach println
	    set.toList
	}
/************************************Initialize related taks *************************************************************/	
	
	
/************************************actor seeker initialize *************************************************************/
    def initializeActors(unique_logs : List[String]) {
      unique_logs foreach { log =>
//	  writers += _system.actorOf(Props(new FileWriterActor(log+".txt")), name = log).asInstanceOf[FileWriterActor]
	  writers += _system.actorOf(Props(new FileWriterActor("%s_%s.txt".format(log,dateString)) ), name = log)
	}
}  
    //print all writers path
    def printAllWriters() {
		for(w <- writers) {
		  println(w.path)
		}
    }
	
    //find a specific writer by log_name
    def findYourHome(log_name : String) = {
	   val ur_actor = _system.actorFor("/user/"+log_name)
	   ur_actor
	}
	
/************************************End actor seeker*************************************************************/
   
   def grindMeat(line : String, meat : Meat) {
      val rule = meat.rule
      rule.out_strategy match {
        case one2one : OneToOneOutputStrategy =>
          handleOneToOneOutput(rule, line, one2one)
        
        case one2many : OneToManyOutputStrategy => 
          handleOneToManyOutput(rule, line, one2many)
      }
   }
   
   
    def handleOneToManyOutput(rule : Rule, line : String, output_strategy : OutputStrategy) {
       val sb : StringBuilder = new StringBuilder()
       val fieldList = new ArrayBuffer[String]()
       rule.fieldList.foreach{field=>
        val rsField = resolveField(line, field)
        fieldList+=rsField
      }
      for(i <- 0 until fieldList.size) {
        if(i==fieldList.size-1)  sb.append(fieldList(i))  else sb.append(fieldList(i)+"\t")
      }
      
      val log_name = getRuleLogMapping(app).get(rule.rule_expression).get
      
      findYourHome(log_name) ! Line(sb.toString)
   }
   
   
   /*
    * normal output strategy, one long write to one line
    * */
   def handleOneToOneOutput(rule : Rule, line : String, output_strategy : OutputStrategy) {
       val sb : StringBuilder = new StringBuilder()
       val fieldList = new ArrayBuffer[String]()
       rule.fieldList.foreach{field=>
        val rsField = resolveField(line, field)
        fieldList+=rsField
      }
      for(i <- 0 until fieldList.size) {
        if(i==fieldList.size-1)  sb.append(fieldList(i))  else sb.append(fieldList(i)+"\t")
      }
      
      val log_name = getRuleLogMapping(app).get(rule.rule_expression).get
      
      findYourHome(log_name) ! Line(sb.toString)
   }
    
   def resolveField( line : String , fieldDesc :FieldDesc) : String = {
	   //segments
	   val segments = line.split("\t")
	  
	   val segmentValue = segments(fieldDesc.segmentIndex)
	    println("mkstring -> "+segmentValue.mkString)
	   //extract strategy
	   val res = fieldDesc.strategy.name match {
	     
	     //This strategy is used for regex matching
	     case "regex" => 
	       val regex = new RegexStrategy(segmentValue, fieldDesc)
	       regex.apply
		   
		 //This strategy is commonly used to fileId=fieldValue  
	     case "no_regex" => 
	       val no_regex = new CommonStrategy(segmentValue, fieldDesc)
	       no_regex.apply
	     
	     //This strategy is used for the segment directly field
	     case "single_value" => 
	       val do_nothing = new SingleValueStrategy(segmentValue)
	       do_nothing.apply
	   }
	   res
   }

	
/************************************ not used comments*************************************************************/

	//this passage code simulate the line from one game
	//each line should first go the app dim, then get the map, then get the map's values.
//	sources.getLines.foreach {line=> 
//	  for( (rule_exp,log_name) <- rule_log_map) {
//	     //one game ff14, get all rules of this game ff14, pass all the games rule json here
//	    
//	     //iterate whole rules map, according to the json to get the 
//	     //
//	  }
	  
//	  if(line.contains("[gsys]SetStack,")) 
//	  writerActor1 ! Line(line)
//	  else if(line.contains("[evnt]CraftLeveNpcTrade")) 
//	  writerActor2 ! Line(line)
//	}
//	readerActor ! PoisonPill 
}