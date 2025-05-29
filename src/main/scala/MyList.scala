import MyListSyntax.MyListOps

abstract class MyList[+T]
case object MyEmpty extends MyList[Nothing]
case class MyCons[A](elem: A, tail: MyList[A]) extends MyList[A]

object MyListSyntax {
  implicit class MyListOps[A](v: A) {
    def <<>>(tail: MyList[A]): MyList[A] = MyCons(v, tail)
  }
}

object UsingMyList extends App{
  1 :: 2 :: 3 :: Nil
  1 <<>> (2 <<>> (3 <<>> MyEmpty) )
}
