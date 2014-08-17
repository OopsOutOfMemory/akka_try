package com.shengli.test.givenwhenthen

import org.scalatest.FunSpec
import org.scalatest.GivenWhenThen
import org.scalatest.matchers.ShouldMatchers


case class Album( name : String, age : Int, artist : Artist)
case class Artist( firstName : String, lastName : String)


class AlbumSpec extends FunSpec with ShouldMatchers with GivenWhenThen {
  describe("An Album") {
    it("can add an Artist to the album at construction time") {
      given("The album Thriller by Michael Jackson")
      val album = new Album("Thriller", 1981, new Artist("Michael", "Jackson"))
      when("the album\'s artist is obtained")
      val artist = album.artist
      then("the artist obtained should be an instance of Artist")
      artist.isInstanceOf[Artist] should be(true)
      and("the artist's first name and last name should be Michael Jackson")
      artist.firstName should be("Michael")
      artist.lastName should be("Jackson")
      artist.lastName should equal("Jackson")
    }
  }
}