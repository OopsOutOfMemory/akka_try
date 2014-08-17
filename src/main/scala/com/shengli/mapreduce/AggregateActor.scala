package com.shengli.mapreduce

import scala.collection.mutable.HashMap

import akka.actor.Actor

class AggregateActor extends Actor {
	val finalReducedMap = new HashMap[String, Int]
	 def receive: Receive = {
	 case ReduceData(reduceDataMap) =>
	 	aggregateInMemoryReduce(reduceDataMap)
	 case Result =>
	 	sender ! finalReducedMap.toString()
	 }
	 def aggregateInMemoryReduce(reducedList: Map[String, Int]): Unit = {
	 for ((key,value) <- reducedList) {
		 if (finalReducedMap contains key)
		 finalReducedMap(key) = (value + finalReducedMap.get(key).get)
	 else
		 finalReducedMap += (key -> value)
	 	}
	 }
}