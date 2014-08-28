package com.shengli.alpha.grinder

import com.shengli.etl.ff.log.Logging
import akka.actor.Actor
import java.io.FileOutputStream
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.io.File

class FileWriterActor(target : String) extends Actor with Logging {
    var count = 0
    var f : FileChannel = _
    var appendable = false
    
    override def preStart() : Unit = {
        //we use append mode  
        appendable = true
        val file = new File(target)
        if(file.exists()) {
          file.delete()
        }
        f = new FileOutputStream(target, appendable).getChannel
    }
    
	def receive : Receive = {
	  case Line(content) => 
	    count += 1
	    logInfo(self.path.name +" receive line->%s".format(content))
	    logInfo(self.path.name +" current count->%s".format(count))
	    f write ByteBuffer.wrap((content+"\n").getBytes)
	}
    
    override def postStop() : Unit = {
       println("stop actor %s with count  %s".format(self.path.name, count))
       f close
    }
}