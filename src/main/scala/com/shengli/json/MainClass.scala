package com.shengli.json

import org.json4s._
import org.json4s.jackson.JsonMethods._
import com.shengli.regex.RegexUtil
import scala.collection.mutable.HashMap
import java.util.LinkedHashMap
import com.fasterxml.jackson.core.JsonParseException
import com.shengli.etl.ff.strategies.RegexStrategy
import com.shengli.etl.ff.strategies.FieldDesc
import com.shengli.etl.ff.strategies.CommonStrategy
import com.shengli.etl.ff.strategies.SingleValueStrategy


object MainClass extends App  { 
  
   implicit val formats = DefaultFormats // Brings in default date formats etc.
   val line = "2014-08-10	1	5	f1f3_20	2014-08-10 11:17:58  INFO  item - [game][item][game][evnt]CraftLeveNpcTrade,leve_id=107,handler=EventHandlerType#14 EventHandlerIndex#0 EventHandlerId#917504,actor=Chara#19014409509679630 Entity#268672935,pc_name=培尔贝瑞,x=168.161713,y=8.718561,z=-48.012917,target=Kind#3 BaseId#1001220 LayoutId#2257677 Chara#0 Entity#1073742005,target_name=朱利昂贝尔"
   val jsonSrc = scala.io.Source.fromFile("task.json").getLines.mkString;
   val resultMap = new LinkedHashMap[String,String]
   
   println(jsonSrc)
   try {
	  val json = parse(jsonSrc)
	  json.children.foreach{
	  child=>
      val field = child.extract[FieldDesc]
      val fieldValue = resolveField(field)
      resultMap.put(field.fieldName, fieldValue)
	  }
	  println(resultMap)
   }
   catch{
     case ex : JsonParseException => println("Error when parsing rules -> " + ex.getMessage())
     case ex : ArrayIndexOutOfBoundsException => println("please check your index setting, index out of bounds")
     case ex : Exception =>println("RunTime exception ->" + ex)
   }

   
   val it = resultMap.entrySet().iterator() 
   val row : StringBuilder = new StringBuilder()
   while(it.hasNext()) {
     val entry = it.next()
     row.append(entry.getValue()  + "\t")
   }
   println(row)
   
//writer : write into a file
   
// println(json.children.size)
// println(json.children.foreach(l=>println("s - > :"+l)))
   
   
   def resolveField(fieldDesc :FieldDesc) : String = {
	   //segments
	   val segments = line.split("\t")
	   println(segments.mkString)
	   val valueArr = segments(fieldDesc.segmentIndex).split(",",-1)
	   
	   //field
	   val fieldString = valueArr(fieldDesc.fieldIndex)
	   println(fieldString)
	   
	   //extract strategy
	   val res = fieldDesc.strategy.name match {
	     
	     //This strategy is used for regex matching
	     case "regex" => 
	       val regex = new RegexStrategy(fieldString, fieldDesc)
	       regex.apply
		   
		 //This strategy is commonly used to fileId=fieldValue  
	     case "no_regex" => 
	       val no_regex = new CommonStrategy(fieldString, fieldDesc)
	       no_regex.apply
	     
	     //This strategy is used for the segment directly field
	     case "single_value" => 
	       val do_nothing = new SingleValueStrategy(fieldString)
	       do_nothing.apply
	   }
	   res
   }
}