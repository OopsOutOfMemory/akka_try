package com.shengli.alpha.grinder

import com.shengli.etl.ff.log.Logging
import akka.actor.Actor

class FileReaderActor extends Actor with Logging{
	def receive : Receive = {
	  case Line => 
	    logInfo(self.path.name + " receive line->%s".format(Line))
	}
}