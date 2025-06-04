

abstract class MyList[+T] {
  def >>:[B>:T](elem: B): MyList[B] = MyCons(elem, this)
}
case object MyEmpty extends MyList[Nothing]
case class MyCons[A](elem: A, tail: MyList[A]) extends MyList[A]



object UsingMyList extends App{
  1 :: 2 :: 3 :: Nil



  List(1, 2, 3) match {
    case ::(head, next) => ???
    case Nil => ???
  }


 val l = 3 >>: MyEmpty
  val a: MyList[Int] = 2 >>: 3 >>: MyEmpty
  1 >>: 2 >>: 3 >>: MyEmpty


}
