package com.shengli.akka.helloworld

import akka.actor.Actor

object GoodBye {
  case object GoodBye;
  case object GoodByeDone;
  case object NoGoodBye;
  case object NoGoodDone;
  case object WhatYouSay;
}


class GoodBye extends Actor {
  def receive = {
    case GoodBye.GoodBye => { 
      println(self+"good bye!") 
      sender ! GoodBye.GoodByeDone
    }
    case GoodBye.NoGoodBye => { 
      println(self+"no good by!")
      sender ! GoodBye.GoodByeDone
    }
    case _ => { 
      println(self+"what u say?")
      sender ! GoodBye.WhatYouSay
    }
  }
}