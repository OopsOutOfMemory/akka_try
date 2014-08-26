package com.shengli.etl.ff.vfs

import org.apache.commons.vfs.VFS
import java.io.File
import org.apache.commons.vfs.FileSystemManager
import com.shengli.etl.ff.log.Logging

/** 
* @ClassName: BaseFileReader 
* @Description: TODO
* @author shengli.victor 盛利
* @date 2014年8月25日  
*/ 
abstract class BaseFile extends Logging{
    def vfs() : FileSystemManager = {
      val fsManager = VFS.getManager()
      fsManager
    }
   
    def readLines(path : String) : Seq[String]
}

/**
 * Read file from plain text
 */
class PlainFile extends BaseFile {
   override def readLines(path : String) : Seq[String] = {
     logInfo("resolve plain file from path->"+path)
     val fileObj = vfs.resolveFile(path)
     val instream = fileObj.getContent().getInputStream()
     val source = scala.io.Source.fromInputStream(instream,"UTF-8")
     source.getLines.toSeq
   }
}


/**
 * Read file from hdfs, should provide path like : hdfs://xxxx:xx/xxx/xx
 */
class HDFSFile extends BaseFile {
   override def readLines(path : String) : Seq[String] = {
     logInfo("resolve hdfs file from path->"+path)
     throw new RuntimeException("not yet implement!")
   }
}

