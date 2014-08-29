package com.shengli.regex

import scala.util.matching.Regex

object RegexTest extends App{
//	val pattern = "]ItemRemove,".r
    val pattern = "stack_\\d+=\\d+".r
    val str = "2014-08-10	1	5	f1f3_20	2014-08-10 11:17:58  INFO  item - [game][item][game][gsys]ItemRemove,actor=Chara#19014409509679630 Entity#268672935,catalog=1003788,stack_1=1,stack_2=2,item_id=73464968925338692,by=unkonwn"
    pattern findFirstIn str  match {
	  case Some(target)=> println("got "+target)
	  case None => println("nothing got")
    }
	
	
	pattern findAllMatchIn(str) match {
	  case it : Iterator[_] => Console println it.toList
	  case _ => None
	}
	
	val s = RegexUtil.MatchMutipleRegex(pattern.toString, str)
	s foreach println
	
}