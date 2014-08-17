package com.shengli.mapreduce

import scala.collection.immutable.Map
import scala.collection.mutable.ArrayBuffer
import scala.concurrent.Await
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.actorRef2Scala
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._
import akka.actor.PoisonPill

sealed trait MapReduceMessage
case class WordCount(word: String, count: Int) extends MapReduceMessage
case class MapData(dataList: ArrayBuffer[WordCount]) extends MapReduceMessage
case class ReduceData(reduceDataMap: Map[String, Int]) extends MapReduceMessage
case class Result extends MapReduceMessage

object MapReduceApplication extends App {
     var start = System.currentTimeMillis();
	 val _system = ActorSystem("MapReduceApp")
	 val master = _system.actorOf(Props[MasterActor], name = "master")
	 implicit val timeout = Timeout(5 seconds)
	 val sources = scala.io.Source.fromFile("item_log.txt")
	 master ! PoisonPill
	 master ! "The quick brown fox tried to jump over the lazy dog and fell on the dog"
	 master ! "Dog is man's best friend"
	 master ! "Dog and Fox belong to the same family"
//	 sources.getLines.foreach(line=> master ! line)
	 Thread.sleep(500)
	 val future = (master ? Result).mapTo[String]
	 val result = Await.result(future, timeout.duration)
	 var end = System.currentTimeMillis();
	 val duration = (end-start) 
//	 println("total time is "+ (end-start) / 1000 +"s")
	 println("total time is "+ duration +"s And result is :" + result)
	 _system.shutdown
}
