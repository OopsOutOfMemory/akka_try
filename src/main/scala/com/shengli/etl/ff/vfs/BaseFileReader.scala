package com.shengli.etl.ff.vfs

import org.apache.commons.vfs.VFS

class BaseFileReader extends App {
	val fsManager = VFS.getManager();
	val f = fsManager.resolveFile("task.json")
	val instream = f.getContent().getInputStream()
	val r = scala.io.Source.fromInputStream(instream)
}



class HDFSFileReader extends BaseFileReader

