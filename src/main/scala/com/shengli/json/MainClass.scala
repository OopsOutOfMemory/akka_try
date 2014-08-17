package com.shengli.json

import org.json4s._
import org.json4s.jackson.JsonMethods._
import com.shengli.regex.RegexUtil

case class Strategy(name:String, expression:String)
case class FieldDesc(fieldName:String, fieldIndex : Int, strategy : Strategy,  splitor : String, extract : String)


object MainClass extends App{ 
   
  
   implicit val formats = DefaultFormats // Brings in default date formats etc.
   val line = "2014-08-10	1	5	f1f3_20	2014-08-10 11:17:58  INFO  item - [game][item][game][evnt]CraftLeveNpcTrade,leve_id=107,handler=EventHandlerType#14 EventHandlerIndex#0 EventHandlerId#917504,actor=Chara#19014409509679630 Entity#268672935,pc_name=培尔贝瑞,x=168.161713,y=8.718561,z=-48.012917,target=Kind#3 BaseId#1001220 LayoutId#2257677 Chara#0 Entity#1073742005,target_name=朱利昂贝尔"
   val jsonSrc = scala.io.Source.fromFile("task.json").getLines.mkString;
   println(jsonSrc)
   val json = parse(jsonSrc)
   val fieldDesc = json.extract[FieldDesc]
   println(json)
   println(fieldDesc)
   
   val optionArr = line.split("\t")
   val valueArr = optionArr(4).split(",",-1)
   val fieldString = valueArr(fieldDesc.fieldIndex)
   println(fieldString)
   
   val strategy = fieldDesc.strategy.name match {
     case "regex" => 
       println("regex strategy")
       RegexUtil.isMatchRegex(fieldDesc.strategy.name,fieldString)
     case _ => println("error")
   }
   
   
   
   
}