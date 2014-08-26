package com.shengli.alpha.grinder

import com.shengli.etl.ff.log.Logging
import akka.actor.Actor

class FileWriterActor extends Actor with Logging {
    var count = 0
	def receive : Receive = {
	  case Line(content) => 
	    count += 1
	    logInfo(self.path.name +" receive line->%s".format(content))
	    logInfo(self.path.name +" current count->%s".format(count))
	}
    
    override def postStop() : Unit = {
       println("stop actor %s with count  %s".format(self.path.name, count))
    }
}