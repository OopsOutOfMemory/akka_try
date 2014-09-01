package com.shengli.regex

object RegexUtil {
  
	def isMatchRegex(regex : String, line : String) : Boolean = {
	  val pattern = regex.r
	  pattern findFirstIn line match {
	    case Some(matched)=> true
	    case None => false
	  }
	}
	
	
	
	def MatchRegex(regex : String, line : String) : String = {
	  val pattern = regex.r
	  val res =  pattern findFirstIn line match {
	    case Some(matched)=> Some(matched).get
	    case None => None
	  }
	  res.toString()
	}
	
	def MatchMutipleRegex(regex : String, line : String) : List[String] = {
	  val pattern = regex.r
	  val res =  pattern.findAllMatchIn(line).toList
	  res.map(matcher=>matcher.toString)
	}
}
