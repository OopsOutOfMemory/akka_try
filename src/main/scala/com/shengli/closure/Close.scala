package com.shengli.closure

class Close {
  var n = 5
  def method(i: Int) = i+n
  def function = (i: Int) => i+5
  def closure = (i: Int) => i+n
  def mixed(m: Int) = (i: Int) => i+m
}