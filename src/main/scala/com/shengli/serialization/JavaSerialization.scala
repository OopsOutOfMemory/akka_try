package com.shengli.serialization

import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ByteArrayInputStream
import java.io.ObjectOutputStream
import com.shengli.etl.ff.strategies.OutputStrategy


 class Person(name : String, age : Int) extends Serializable
 

object JavaSerialization extends App{

  
  val rs = serialize(sayHello)
  println(rs)
  val ds = deserialize(rs)
  println(ds)

def sayHello = {
    val obj = new Person("shengli",26)
    obj
  }

def serialize[T](o: T): Array[Byte] = {
    val bos = new ByteArrayOutputStream()
    val oos = new ObjectOutputStream(bos)
    oos.writeObject(o)
    oos.close()
    bos.toByteArray
  }
	
	
	
	
/** Deserialize an object using Java serialization */
  def deserialize[T](bytes: Array[Byte]): T = {
    val bis = new ByteArrayInputStream(bytes)
    val ois = new ObjectInputStream(bis)
    ois.readObject.asInstanceOf[T]
  }
}



