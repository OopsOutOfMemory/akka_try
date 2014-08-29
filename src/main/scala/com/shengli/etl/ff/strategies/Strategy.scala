package com.shengli.etl.ff.strategies

import com.shengli.regex.RegexUtil
import com.shengli.etl.ff.log.Logging
import scala.collection.mutable.ListBuffer

case class FieldDesc(fieldName:String, segmentIndex:Int,  strategy : Strategy,  splitor : String, extract : String)
case class Strategy(name:String, expression:String)
case class Rule(app_name : String, rule_expression : String, fieldList : List[FieldDesc],  out_strategy : OutputStrategy)

class OutputStrategy(var name:String)
class OneToManyOutputStrategy(name:String, iterate_field : String) extends OutputStrategy(name)
class OneToOneOutputStrategy(name : String) extends OutputStrategy(name)

//use for different strategies to matching fields
abstract case class BaseStrategy extends Logging {
  def apply(): Any
}

class RegexMutipleStrategy(fieldString : String,fieldDesc :FieldDesc) extends BaseStrategy {
    override def apply() : ListBuffer[String] = {
     try{
       val rtList = RegexUtil.MatchMutipleRegex(fieldDesc.strategy.expression, fieldString)
       var ret = ListBuffer[String]()
	        //extract
		   val fieldValue = fieldDesc.extract match {
		         case "right" =>
		           rtList.foreach{ e=>
		           val fieldValueArray = e.split(fieldDesc.splitor,-1)
		           ret += fieldValueArray(1)
		           ret
		           }
		         case _ =>
		           ListBuffer[String]()
		   }
           ret
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
		           fieldValueArray(1)
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
		           fieldValueArray(1)
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

