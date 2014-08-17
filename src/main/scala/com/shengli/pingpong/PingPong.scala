package com.shengli.pingpong

import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.actorRef2Scala
import akka.actor.PoisonPill

case class PING
case class PONG
class PingPongActor extends Actor {
	 import context._
	 var count = 0
	 def receive: Receive = {
	 case PING =>
	 	println("PING")
	 	count = count + 1
	 	Thread.sleep(1000)
	 	self ! PONG
	 	become {
	 		case PONG =>
	 		println("PONG")
	 		count = count + 1
	 		Thread.sleep(1000)
	 		self ! PING
	 		unbecome()
	 }
	 if(count > 10) {
//	     context.stop(self)
	     self ! PoisonPill
	  }
	 }
}

object PingPong extends App {
    val _system = ActorSystem("BecomeUnbecome")
	val pingPongActor = _system.actorOf(Props[PingPongActor])
	pingPongActor ! PING
	Thread.sleep(20000)
	_system.shutdown
  
}