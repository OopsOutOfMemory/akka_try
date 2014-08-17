package com.shengli.test

import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers

case class Album( name : String, age : Int, artist : Artist)
case class Artist( name : String, age : Int)

class AlbumTest extends FunSpec with ShouldMatchers {
	 describe("An Album from shengli") {
	 it ("test age") {
	 val album = new Album("OopsOutOfMemory", 2014, new Artist("shengli",1989))
	 album.artist.age should be (5)
	 }
	}
 }