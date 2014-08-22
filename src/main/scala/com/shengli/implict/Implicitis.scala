package com.shengli.implict

import java.io.File

object Implicitis extends App {
  import Context._
//  new File("xxx").read(new File)
//AAA.print("Jack")
 println(1.add2)
 val line1 = new Line(1)
 val line2 = new Line(2)
 implicit object Line extends LineOrdering
 val p = new Pair(1,2) 
 val p2 = new Pair("C","B")
 val p3 = new Pair[Line](line1,line2)
 

 println(p.smaller)
 println(p2.smaller)
}

class RichFile(val file : File){
  def read = scala.io.Source.fromFile(file.getPath().toString())
}

class Pair[T:Ordering] (val first : T, val second :T) {
  def smaller(implicit ord:Ordering[T]) = 
    if (ord.compare(first, second)<0) first else second
}

class Line(val len : Double)  {
  override def toString = "Length is : " + len
}

trait LineOrdering extends Ordering[Line] {
  override def compare(x : Line, y : Line) = {
    if(x.len > y.len) 1 
    else if(x.len < y.len) -1 
    else 0
  }
}


object Context {
  implicit def file2RichFile(f : File) = new RichFile(f)
  implicit val ccc : String = "Hello"
  //隐式类不能在顶层对象中声明
   //隐式类 就是 为了参数的类型即Int而扩展方法
  implicit class HH(x:Int)  {
	  def add2 = x + 2
  }
}
//若有同名方法，不会使用隐式转换

object AAA{
  def print(content :String) (implicit prefix : String ) { //寻找隐式类型参数
    println(prefix + " : " + content)
  }
}


//def smaller[T](a:T,b:T)(implicit order : T => Ordered[T]) = if (a<b) a else b
//def smaller[T](a:T,b:T)(implicit order : T => Ordered[T]) = if (order(a)<b) a else b

