package com.shengli.concurrent
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Await
import scala.concurrent.Future
import scala.concurrent.future

object Shell extends App {

  val tasks: Seq[Future[Int]] = for (i <- 1 to 10) yield 
  future {
    println("Executing task " + i)
    Thread.sleep(i * 500L)
    i * i
  }

  val aggregated: Future[Seq[Int]] = Future.sequence(tasks)

  val squares: Seq[Int] = Await.result(aggregated, 15.seconds)
  println("Squares: " + squares)

}