package com.shengli.etl.ff.strategies

import com.shengli.regex.RegexUtil
import com.shengli.etl.ff.log.Logging

case class FieldDesc(fieldName:String, segmentIndex:Int,  strategy : Strategy,  splitor : String, extract : String)
case class Strategy(name:String, expression:String)

  
//use for different strategies to matching fields
abstract case class BaseStrategy extends Logging {
  def apply(): String
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

