package etl.ff

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FunSpec
import com.shengli.etl.ff.vfs.PlainFile
import com.shengli.etl.ff.log.Logging

object ConsoleTestVFS extends App {
   val  path ="""E:\akka_try\item_log.txt"""
	      val pFile = new PlainFile
	      val lines = pFile.readLines(path)
//	      val count = lines.size
//	      println(count)
	      Console println lines.filter(_.contains("[gsys]ItemGet,")).take(1).mkString
	      Console println lines.filter(_.contains("[gsys]SetStack,")).take(1).mkString
}

class TestVFS extends FunSpec with ShouldMatchers with Logging {
	describe("text read file with absolute path"){
	  it("test plain file"){
	      val  path ="""E:\akka_try\item_log.txt"""
	      val pFile = new PlainFile
	      val lines = pFile.readLines(path)
	      val count = lines.size
	      logDebug("size is :" + count)
	      count should not be(0)
	  }
	  
	  it("test hdfs file"){
	    //TODO:
	  }
	}
	
}