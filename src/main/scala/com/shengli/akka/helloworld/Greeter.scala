package com.shengli.akka.helloworld

import akka.actor.Actor

object Greeter {
  case object Greet
  case object Done
}

class Greeter extends Actor {
  def receive = {
    case Greeter.Greet =>
      println(self+"Hello World!")
      sender() ! Greeter.Done
  }
}