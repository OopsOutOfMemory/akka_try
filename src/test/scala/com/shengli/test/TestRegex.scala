package com.shengli.test

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FunSpec
import com.shengli.regex.RegexUtil

class TestRegex extends FunSpec with ShouldMatchers {
  describe("test a line whether match the given regex") {
    it("test regex") {
      val line = "2014-08-10	1	5	f1f3_20	2014-08-10 11:17:58  INFO  item - [game][item][game][gsys]ItemRemove,actor=Chara#19014409509679630 Entity#268672935,catalog=1003788,stack=1,item_id=73464968925338692,by=unkonwn"
      val regex = "ItemRemove"
      val result = RegexUtil.isMatchRegex(regex, line)
      val segment = line.split(line, -1);
      segment.foreach(println)
      result should be(true)
    }
  }
}

object Test extends App {
  val line = "2014-08-10	1	5	f1f3_20	2014-08-10 11:17:58  INFO  item - [game][item][game][gsys]ItemRemove,actor=Chara#19014409509679630 Entity#268672935,catalog=1003788,stack=1,item_id=73464968925338692,by=unkonwn"
  val segment = line.split("\t", -1);
  println(segment(0))
}


class Test {
  def extractField( strategy : String, regex : String) {
    
  }
}


