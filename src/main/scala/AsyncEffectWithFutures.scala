//import scala.concurrent.ExecutionContext.Implicits.global
import java.util.concurrent.Executors
import scala.concurrent.{Await, ExecutionContext, Future, Promise}
import scala.concurrent.duration.DurationInt

object AsyncEffectWithFutures extends App {
  val threadPoolSize = 4
  val fixedThreadPool = Executors.newFixedThreadPool(threadPoolSize)
  implicit val ec: ExecutionContext = ExecutionContext.fromExecutorService(fixedThreadPool)



  def doAsyncWithCallback(param: Int)( callback: Either[Throwable, String] => Unit): Unit = ???

  def doAsync(param: Int): Future[String]= {
    val p = Promise[String]

    doAsyncWithCallback(param) {
      case Left(throwable) => p.failure(throwable)
      case Right(str) => p.success(str)
    }

    p.future
  }

  doAsyncWithCallback(23){
    case Right(s) =>
    case Left(throwable) =>
  }

  val a: Future[String] = Future{
    Thread.sleep(2000)
    23
  }.map(x => x.toString)

    val b = Future{
      println("Executing async task")

      (1 to 10).foreach { i =>
        Thread.sleep(1000)
        println(s"Step $i")
      }
      println("Task executed")
    }

  def countingTask(id: String, n: Int): Unit = {
    println(s"Executing async task id")

    (1 to n).foreach { i =>
      Thread.sleep(1000)
      println(s"Task $id, Step $i")
    }
    println(s"Task $id executed")
  }

//  val b = Future(countingTask("A", 5))
//  Thread.sleep(500)
//  Future(countingTask("B", 10))
//
//  Await.result(b, 50.seconds)



  val r: Future[String] = Future{
    countingTask("A", 5)
    23
  }.zip(
    Future {
      countingTask("B", 10)
      2
    }
  ).map { case (x, y) =>

    println(x+y)
    (x+y).toString
  }

  Await.result(r, 1.minute)

}
