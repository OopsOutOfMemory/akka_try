package com.shengli.alpha.grinder

import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.PoisonPill

object GrinderApplication  extends App {
	val _system = ActorSystem("Grinder")
	val readerActor = _system.actorOf(Props[FileReaderActor], name = "reader")
	val writerActor1 = _system.actorOf(Props[FileWriterActor], name = "writer1")
	val writerActor2 = _system.actorOf(Props[FileWriterActor], name = "writer2")
	val sources = scala.io.Source.fromFile("item_log.txt")
	sources.getLines.foreach {line=> 
	  if(line.contains("[gsys]SetStack,")) 
	    writerActor1 ! Line(line)
	  else if(line.contains("[evnt]CraftLeveNpcTrade")) 
	    writerActor2 ! Line(line)
	}
	writerActor1 ! PoisonPill 
	writerActor2 ! PoisonPill 
//	readerActor ! PoisonPill 
	if(writerActor1.isTerminated && writerActor2.isTerminated ){
	  _system.shutdown
	}
}