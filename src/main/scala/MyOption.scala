
sealed trait MyOption[+A] {
  def map[B](f: A => B): MyOption[B] = this match {
    case MySome(value) => MySome(f(value))
    case MyNone => MyNone
  }

  def flatMap[B](f: A => MyOption[B]): MyOption[B] = this match {
    case MySome(value) => f(value)
    case MyNone => MyNone
  }
}
case class MySome[T](value: T) extends MyOption[T]
case object MyNone extends MyOption[Nothing]

object MyOption {
  def apply[T](value: T): MyOption[T] = if (value == null) MyNone else MySome(value)


}

object MyOptionUsage {

  val r = for{
    a <- MyOption(23)
    b <- MyOption(44)
  }yield (a+b).toString



}




















//sealed trait MyOption[+T] {
//  def map[B](f: T => B): MyOption[B] =
//    this match {
//      case Empty => Empty
//      case SomeValue(v) => SomeValue(f(v))
//    }
//
//  def flatMap[B](f: T => MyOption[B]): MyOption[B] =
//    this match {
//      case Empty => Empty
//      case SomeValue(v) =>
//        f(v) match {
//          case Empty => Empty
//          case SomeValue(v2) => SomeValue(v2)
//        }
//    }
//
//}

//case object Empty extends MyOption[Nothing]
//case class SomeValue[T](v: T) extends MyOption[T]
//
//
//object MyOption {
//  def apply[T](value: T):MyOption[T]={
//    if(value == null) Empty // if we want Empty to be subtype of MyOption we need MyOption to be covariant with T
//    else SomeValue(value)
//  }
//}
//
//object MyOptionDemo extends App {
//
//  // 1- Construction
//  val a = MyOption{
//    println("buildiing myoption")
//    null
//  }
//
//  a match {
//    case Empty =>
//      println("is empty")
//    case SomeValue(v) =>
//      println(s"has value $v")
//  }
//
//  // 2- map
//  def getUserIdFromHttpHeader(headers: Map[String, String]): MyOption[Int] = MyOption(34)
//
//
//  val idStr: MyOption[String] = getUserIdFromHttpHeader(Map.empty).map(x => x.toString)
//
//  // 3- flatmap
//  def getUserNameById(id: Int): MyOption[String] = ???
//
//  //val b: MyOption[String] = getUserIdFromHttpHeader(Map.empty).map(getUserNameById)
//
//  val b: MyOption[String] = getUserIdFromHttpHeader(Map.empty).flatMap(getUserNameById)
//
//
//  // 4- For comprehension
//  def getPassword(userName: String): MyOption[String] = ???
//
//
//  getUserIdFromHttpHeader(Map.empty)
//    .flatMap(id =>
//      getUserNameById(id)
//        .flatMap(userName =>
//          getPassword(userName)
//            .map(password => s"UserName for $id is $userName. Password: $password")
//        )
//    )
//
//  for{
//    id <- getUserIdFromHttpHeader(Map.empty)
//    userName <- getUserNameById(id)
//    password <- getPassword(userName)
//  } yield s"UserName for $id is $userName. Password: $password"
//
//}
