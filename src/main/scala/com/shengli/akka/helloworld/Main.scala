package com.shengli.akka.helloworld

object Main {

  def main(args: Array[String]): Unit = {
    println(classOf[HelloWorld].getName)
    akka.Main.main(Array(classOf[HelloWorld].getName))
  }
}