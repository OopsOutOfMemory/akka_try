package com.shengli.etl.ff.strategies

import com.shengli.regex.RegexUtil

case class FieldDesc(fieldName:String, segmentIndex:Int, fieldIndex : Int, strategy : Strategy,  splitor : String, extract : String)
case class Strategy(name:String, expression:String)

  
//use for different strategies to matching fields
abstract case class BaseStrategy {
  def apply(): String
}

class RegexStrategy(fieldString : String,fieldDesc :FieldDesc) extends BaseStrategy {
  override def apply() : String = {
     val rt = RegexUtil.MatchRegex(fieldDesc.strategy.expression, fieldString)
	        //extract
		   val fieldValueArray = rt.split(fieldDesc.splitor,-1)
		   val fieldValue = fieldDesc.extract match {
		         case "right" =>
		           fieldValueArray(1)
		         case _ =>
		           None
		   }
           fieldValue.toString()
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

