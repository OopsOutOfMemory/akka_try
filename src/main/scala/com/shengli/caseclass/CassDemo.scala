package com.shengli.caseclass

abstract class Person

case class Teacher(name:String) extends Person

case class Student(name:String, age : Int) extends Person

object CassDemo extends App {

  def m(p : Person){
     p match {
       case Teacher(_) => println("A teacher")
       case Student("D", 20) => println("A student")
       case _ => Console println "unkown"
     }
  }
  
  val mp = Map(1->2)
  //一般用getOrElse
  mp.get(1) match {
    case Some(v) => Console println v
    case None => Console println "no such key"
  }
  
  
  val p  = new Teacher("God")
  m(p)
}