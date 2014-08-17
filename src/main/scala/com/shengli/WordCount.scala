package com.shengli

import scala.collection.mutable.HashMap
import scala.collection.mutable.ArrayBuffer

case class WC(word : String, count : Int)

object WordCount extends App{
    val start = System.currentTimeMillis();
	val sources = scala.io.Source.fromFile("wordcount.txt")
	val iter = sources.getLines
	var hmap = new HashMap[String,Int]()
	sources.getLines.foreach{line=>
		line.split("""\s+""").foldLeft(ArrayBuffer.empty[WC]){
		 (index, word) => 
		   index
		}
	}
	val end = System.currentTimeMillis();
	val duration = (end - start) / 1000
	println("total duration is %s s".format(duration))
	println(hmap.foreach(println))
}