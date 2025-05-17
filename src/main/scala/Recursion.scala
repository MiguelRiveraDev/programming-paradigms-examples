import scala.annotation.tailrec

object Recursion  extends App {

  def impFibonacci(n: Int): List[Int] = {
    val arr: Array[Int] = new Array(n)
    for(i <- 0 until n){
      if(i == 0){
        arr(0) = 0
      }
      else if(i == 1){
        arr(1) = 1
      }
      else{
        arr(i) = arr(i-1) + arr(i-2)
      }
    }
    arr.toList
  }

  def recFibonacci(n: Int): List[Int] = {
    @tailrec
    def fibo(i: Int, a: Int, b: Int, acc: List[Int]): List[Int] = {
      if (i >= n) acc.reverse
      else fibo(i + 1, b, a + b, a :: acc)
    }

    fibo(0, 0, 1, Nil)
  }

  def lazyFibonacci(): LazyList[Int] = {
    def fibo(a: Int, b: Int): LazyList[Int] = a #:: fibo(b, a + b)
    fibo(0,1)
  }


  assert(impFibonacci(10) == recFibonacci(10))
  assert(impFibonacci(10) == lazyFibonacci().take(10))

  val a = impFibonacci(10)

  val b = recFibonacci(10)

  val c = lazyFibonacci().take(10)
  println()
}