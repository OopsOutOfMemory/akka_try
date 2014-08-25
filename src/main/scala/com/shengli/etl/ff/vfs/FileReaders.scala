package com.shengli.etl.ff.vfs

import org.apache.commons.vfs.VFS
import java.io.File
import org.apache.commons.vfs.FileSystemManager
import com.shengli.etl.ff.log.Logging

object BaseFileReaders extends App {
     val  path ="""E:\akka_try\item_log.txt"""
     val  reader = new PlainFileReader
     val lines = reader.readLines(path)
     lines.filter(_.contains("GatheringResult")).foreach(println)
}

/** 
* @ClassName: BaseFileReader 
* @Description: TODO
* @author shengli.victor 盛利
* @date 2014年8月25日  
*/ 
abstract class BaseFileReader extends Logging{
    def vfs() : FileSystemManager = {
      val fsManager = VFS.getManager()
      fsManager
    }
   
    def readLines(path : String) : Seq[String]
}


class PlainFileReader extends BaseFileReader {
   override def readLines(path : String) : Seq[String] = {
     logInfo("resolve file from path->"+path)
     val fileObj = vfs.resolveFile(path)
     val instream = fileObj.getContent().getInputStream()
     val source = scala.io.Source.fromInputStream(instream)
     source.getLines.toSeq
   }
}

//class HDFSFileReader extends BaseFileReader

