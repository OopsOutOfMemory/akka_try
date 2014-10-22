package com.shengli.asm

import com.esotericsoftware.reflectasm.MethodAccess
import com.shengli.asm.Match

object Simple extends App {
	val someObject = new Match()
	val access = MethodAccess.get(someObject.getClass());
	val res = access.invoke(someObject, "add");
	Console println res
}