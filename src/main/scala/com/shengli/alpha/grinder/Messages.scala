package com.shengli.alpha.grinder

sealed trait Messages

case class Line(content : String) extends Messages
case class Log(log_name: String, value : String) extends Messages