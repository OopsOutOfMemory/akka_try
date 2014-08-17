package com.shengli.akka.helloworld

import akka.actor.Actor
import akka.actor.Props
import akka.actor.actorRef2Scala

class HelloWorld extends Actor with Logging {

  override def preStart(): Unit = {
    // create the greeter actor
    val greeter = context.actorOf(Props[Greeter], "greeter")
    val goodbyer = context.actorOf(Props[GoodBye],"goodbyer")
    // tell it to perform the greeting
    val s = scala.io.Source.fromFile("item_log.txt")
    val tenLines = s.getLines().take(10)
    greeter ! Greeter.Greet
    goodbyer ! tenLines
    
  }

  def receive = {
 
    // when the greeter is done, stop this actor and with it the application
    case Greeter.Done => {
      println(self+"got greet!")
      logDebug("recieved a log!")
//      context.stop(self)
    }
    case GoodBye.GoodByeDone => println(self+"ok, got good bye!")    
    case GoodBye.WhatYouSay => println(self+"ok, i say wrong message!")
  }
}