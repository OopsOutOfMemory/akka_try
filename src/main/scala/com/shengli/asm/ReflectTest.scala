package com.shengli.asm

import java.io.OutputStream
import java.io.InputStream
import java.io.ByteArrayOutputStream
import org.objectweb.asm.ClassReader
import java.io.ByteArrayInputStream
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Type
import org.objectweb.asm.MethodVisitor

class Match {
  val v = 0
  def add(it: Int) = {
    v + it
  }
}

object ReflectTest extends App {

  val cleaner = new ClosureCleaner()
  val m = new Match()
  val cls_reader = cleaner.getClassReader(m.getClass())
  Console println cleaner.getOuterClasses(m)
  val outerClasses = cleaner.getOuterClasses(m)
  val outerObjects = cleaner.getOuterObjects(m)

  val accessedFields = Map[Class[_], Set[String]]()

//  cleaner.getClassReader(func.getClass).accept(new ReturnStatementFinder(), 0)
}

class ClosureCleaner {

  //   def getInnerClasses(obj: AnyRef): List[Class[_]] = {
  //    val seen = Set[Class[_]](obj.getClass)
  //    var stack = List[Class[_]](obj.getClass)
  //    while (!stack.isEmpty) {
  //      val cr = getClassReader(stack.head)
  //      stack = stack.tail
  //      val set = Set[Class[_]]()
  //      cr.accept(new InnerClosureFinder(set), 0)
  //      for (cls <- set -- seen) {
  //        seen += cls
  //        stack = cls :: stack
  //      }
  //    }
  //    return (seen - obj.getClass).toList
  //  }

  def getOuterObjects(obj: AnyRef): List[AnyRef] = {
    for (f <- obj.getClass.getDeclaredFields if f.getName == "$outer") {
      f.setAccessible(true)
      if (isClosure(f.getType)) {
        return f.get(obj) :: getOuterObjects(f.get(obj))
      } else {
        return f.get(obj) :: Nil // Stop at the first $outer that is not a closure
      }
    }
    Nil
  }
  def getOuterClasses(obj: AnyRef): List[Class[_]] = {
    for (f <- obj.getClass.getDeclaredFields if f.getName == "$outer") {
      f.setAccessible(true)
      if (isClosure(f.getType)) {
        return f.getType :: getOuterClasses(f.get(obj))
      } else {
        return f.getType :: Nil // Stop at the first $outer that is not a closure
      }
    }
    Nil
  }
  // Check whether a class represents a Scala closure
  def isClosure(cls: Class[_]): Boolean = {
    cls.getName.contains("$anonfun$")
  }

  def getClassReader(cls: Class[_]): ClassReader = {
    // Copy data over, before delegating to ClassReader - else we can run out of open file handles.
    val className = cls.getName.replaceFirst("^.*\\.", "") + ".class"
    val resourceStream = cls.getResourceAsStream(className)
    // todo: Fixme - continuing with earlier behavior ...
    if (resourceStream == null) return new ClassReader(resourceStream)

    val baos = new ByteArrayOutputStream(128)
    copyStream(resourceStream, baos, true)
    new ClassReader(new ByteArrayInputStream(baos.toByteArray))
  }

  def copyStream(in: InputStream,
    out: OutputStream,
    closeStreams: Boolean = false) {
    val buf = new Array[Byte](8192)
    var n = 0
    while (n != -1) {
      n = in.read(buf)
      if (n != -1) {
        out.write(buf, 0, n)
      }
    }
    if (closeStreams) {
      in.close()
      out.close()
    }
  }

}


