package com.shengli.regex

object RegexUtil {
  
	def isMatchRegex(regex : String, line : String) : Boolean = {
	  val pattern = regex.r
	  pattern findFirstIn line match {
	    case Some(matched)=> true
	    case None => false
	  }
	}
}