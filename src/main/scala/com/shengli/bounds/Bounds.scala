package com.shengli.bounds

object Bounds extends App{
	val p  = new Pair("A","B")
//	Console println p.smaller
	
	val s1 = new Student("C")
	val s2 = new Student("D")
	
	val pair1 = new Pair(s1,s2)
	val p1 = new Person("AA")
	println(pair1.replaceFirst(p1))
}

//T是Comparable的子类型
//class Pair[T<:Comparable[T]] (val first : T, val second :T){
//  def smaller = if(first.compareTo(second)>0)  second else first 
//  

//隐式转换
//class Pair[T<%Comparable[T]] (val first : T, val second :T){
class Pair[T] (val first : T, val second :T){
  def replaceFirst[R>: T](newFirst : R) = new Pair(newFirst,second)
}
//T =:= U 判断类型是否相等
//T <:< U 判断T是不是U的子类型
//T <%< U 判断T是否能隐式转换为U

//型变
//协变
//逆变

//new X[+T]  外面的类型变化X[T]，和内部类型T继承关系一样的。
//协变的情况下，T不能作为参数
//逆变是用来做参数的-T，    +T协变是用来做返回值
// <? extends AAA> 类型通配符
// [_ <: AAA]
class FF[-T,+R]

class Person(var name : String)
class Student(name :String) extends Person(name)