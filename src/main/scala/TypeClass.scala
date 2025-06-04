
//PRE: implicitos como segunda lista de paramertos

trait JsonSerializable[T] {
  def toJson(v: T): String
}


case class User(name: String, age: Int)
case class House(address: String, price: BigDecimal)

object JsonSerializableInstances {

  implicit val userInstance = new JsonSerializable[User] {
    override def toJson(v: User): String = s"{ ... }"
  }
  implicit val houseInstance = new JsonSerializable[House] {
    override def toJson(v: House): String = s"{ ... }"
  }

  implicit def option[T](implicit s: JsonSerializable[T])
  = new JsonSerializable[Option[T]] {
    override def toJson(v: Option[T]): String = v match {
      case Some(value) => s.toJson(value)
      case None => "{}"
    }
  }

}

object JsonSerializableSyntax {
  implicit class UserSerializableOps(u: User) {
    def toJson(implicit s: JsonSerializable[User]) : String = s.toJson(u)
  }

  implicit class OptionSerializableOps[T](v: Option[T]) {
    def toJson(implicit s: JsonSerializable[Option[T]]): String = s.toJson(v)
  }

}


object TypeClass {
  import JsonSerializableInstances._
  import JsonSerializableSyntax._
  val user = User("Bernat", 44)
  user.toJson

  Option(user).toJson

  def doSomething[T](element: T)(implicit s: JsonSerializable[T]): Unit = {
    s.toJson(element)
  }

  def doSomething2[T: JsonSerializable](element: T) :Unit = {
    val s = implicitly[JsonSerializable[T]]
    s.toJson(element)
  }
}

// - Open Closed principle: Si quiero que un tipo sea serializable a json ni lo toco
// - Composition over inheritance. User no es un JsonSerializable semánticamente, uso herencia cuando no debería
// - Puedes extender funcionalidad para tipos que no son tuyos
// - AddHoc polymorphism: base on type resolution

///////////////////////////////// Ejercicio
trait Eq[A] {
  def eqv(x: A, y: A): Boolean
}

trait Monoid[A] {
  def empty: A
  def combine(x: A, y: A): A
}

object Monoid {
  def combineAll[A: Monoid](list: List[A]): A = {
    val m = implicitly[Monoid[A]]
    list.foldLeft(m.empty)(m.combine)
  }
}
////////////////////////////////////////////////////////



object FilterFold {
  def myFilter[E](lst: List[E], predicate: E => Boolean): List[E] = {
    lst.foldLeft(List.empty[E]){ case (lst, e) =>
      if(predicate(e)){
        e :: lst
      }else{
        lst
      }
    }
  }
}

trait MyCollection[F[_]] {
  def emptyCol[E]: F[E]
  def append[E](lst: F[E], ele: E): F[E]
  def foldLeft[A,E](lst: F[E], acc:A)(f: (A,E) => A): A
}

case class OtraColeccion[A]()

object MyCollectionInstances {
  implicit val  listColInstance = new MyCollection[List] {
    override def emptyCol[E]: List[E] = List.empty

    override def append[E](lst: List[E], ele: E): List[E] = ele :: lst

    override def foldLeft[A, E](lst: List[E],acc: A)(f: (A, E) => A): A =  lst.foldLeft(acc)(f)
  }



  implicit val otraColeccionInstance = new MyCollection[OtraColeccion] {
    override def emptyCol[E]: OtraColeccion[E] = OtraColeccion[E]()

    override def append[E](lst: OtraColeccion[E], ele: E): OtraColeccion[E] = OtraColeccion[E]()

    override def foldLeft[A, E](lst: OtraColeccion[E], acc: A)(f: (A, E) => A): A = ???
  }

}



object MyFilter {
  def myFilter[F[_], A](col: F[A], predicate: A => Boolean)(implicit ev: MyCollection[F]): F[A] =
    ev.foldLeft(col, ev.emptyCol[A]) { (acc, e) =>
      if (predicate(e)) ev.append(acc, e)
      else acc
    }

//  def myFilter[F[_] :MyCollection, A](col: F[A], predicate: A => Boolean): F[A] = {
//    val ev = implicitly[MyCollection[F]]
//    ev.foldLeft(col, ev.emptyCol[A]) { (acc, e) =>
//      if (predicate(e)) ev.append(acc, e)
//      else acc
//    }
//  }
}

object MyFilterSyntax {
 implicit class MyCollectionOps[F[_],E](lst: F[E]) {
   def mySuperFiler(predicate: E => Boolean)(implicit e: MyCollection[F]): F[E] =
     MyFilter.myFilter(lst, predicate)
 }
}

object Uso {
  import MyFilterSyntax._
  import MyCollectionInstances._
  List(1,2,3).mySuperFiler(_ > 2)
}