package com.shengli.etl.ff.vfs
import scala.concurrent.ops._

class PlainFileWriter(log_name : String, path : String) extends Runnable {
   override def run() {
    while (true) { 
      println(log_name)
      Thread.sleep(1000);
    }
   }
}

object TestThread extends App {
    val rnner = new Thread(new PlainFileWriter("ff14_item_get_log","./ff14_item_get_log.txt"))
    rnner.start()
    Thread.sleep(500);
	spawn {
	  while (true) { 
	    println("hi what?")
	    Thread.sleep(1000);
	  }
	}
}