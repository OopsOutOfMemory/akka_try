package com.shengli.etl.ff.strategies

import com.shengli.regex.RegexUtil
import com.shengli.etl.ff.log.Logging
import scala.collection.mutable.ListBuffer

case class FieldDesc(fieldName:String, segmentIndex:Int,  strategy : Strategy,  splitor : String, extract : String)
case class Strategy(name:String, expression:String)
case class Rule(app_name : String, rule_expression : String, fieldList : List[FieldDesc],  out_strategy : Any)


class OutputStrategy(name : String)
class OneToManyOutputStrategy(name:String, var iterate_fields : String) extends OutputStrategy(name)
class OneToOneOutputStrategy(name : String) extends OutputStrategy(name)

//use for different strategies to matching fields
abstract case class BaseStrategy extends Logging {
  def apply(): Any
}

class RegexMutipleStrategy(fieldString : String,fieldDesc :FieldDesc) extends BaseStrategy {
    override def apply() : String = {
     try{
       val rtList = RegexUtil.MatchMutipleRegex(fieldDesc.strategy.expression, fieldString)
       println(rtList)
       var ret = ListBuffer[String]()
	        //extract
		   val fieldValue = fieldDesc.extract match {
		         case "right" =>
		           rtList.foreach{ e=>
		           val fieldValueArray = e.split(fieldDesc.splitor,-1)
		           if(fieldValueArray.size>0) ret += fieldValueArray(1) else ret += ""
		           }
		         case _ =>
		           ListBuffer[String]()
		   }
           //we connect them by , then split it
           ret.mkString(",")
     }
     catch {
       case ex : ArrayIndexOutOfBoundsException => 
         logError("Error when extract field %s, split error, index out of bounds!".format(fieldDesc.fieldName))
         throw new RuntimeException("Error when apply regex strategy on field " + fieldDesc.fieldName) 
     }
  }
}

class RegexStrategy(fieldString : String,fieldDesc :FieldDesc) extends BaseStrategy {
  override def apply() : String = {
     try{
       val rt = RegexUtil.MatchRegex(fieldDesc.strategy.expression, fieldString)
	        //extract
		   val fieldValue = fieldDesc.extract match {
		         case "right" =>
		           val fieldValueArray = rt.split(fieldDesc.splitor,-1)
		           if(fieldValueArray.size>0)  fieldValueArray(1) else ""
		         case _ =>
		           rt
		   }
           fieldValue.toString()
     }
     catch {
       case ex : ArrayIndexOutOfBoundsException => 
         logError("Error when extract field %s, split error, index out of bounds!".format(fieldDesc.fieldName))
         throw new RuntimeException("Error when apply regex strategy on field " + fieldDesc.fieldName) 
     }
  }
}

//This Strategy is for fieldName = fieldValue, return fieldValue
class CommonStrategy(fieldString : String, fieldDesc :FieldDesc) extends BaseStrategy {
    override def apply() : String = {
    	val fieldValueArray = fieldString.split(fieldDesc.splitor,-1)
		   val fieldValue = fieldDesc.extract match {
		         case "right" =>
		            if(fieldValueArray.size>0)  fieldValueArray(1) else ""
		         case _ =>
		           None 
	       }
		   fieldValue.toString()
  }
}

//This Strategy is do not need matching, just return itself
class SingleValueStrategy(fieldString : String) extends BaseStrategy {
    override def apply() : String = {
    	fieldString.toString()
  }
}

