import scala.util.{Failure, Success, Try}

sealed trait TipoDeCaja
case object CajaGrande extends TipoDeCaja
case object CajaPequena extends TipoDeCaja

//sealed trait User
//case class Basic(name: String, age: Option[Int]) extends User
//case class Advanced(name: String, age: Int, score: Long) extends User
//
//object CaseClasses {
//  val p: User = Basic("a", Some(45))
//  p match {
//    case Basic(_, Some(age)) if age > 18 =>
//      println("")
//    case Advanced(name, _, score) if score < 10 =>
//      println(name)
//    case _ =>
//  }
//}




object ForComprehension {
  def getIntValor(): Option[Int] = ???
  def getStringValor(x: Int): Option[String] = ???
  def getLong(s: String): Option[Long] = ???

  val result = for {
    valorEntero <- getIntValor()
    valorString <- getStringValor(valorEntero)
    valorLong   <- getLong(valorString)
  }yield valorLong + 1

  val l = getIntValor()
    .flatMap( x =>
      getStringValor(x)
        .flatMap(y => getLong(y))
          .map(r => r + x + 1)
    )

}

object ListUsage extends App {

  val r: List[Int] = List(1,2,3,4)

  def duplica(x: Int): List[Int] = List(x,x)

  val f: List[Int] = r.flatMap(duplica)

  println(f)

}

object EitherUsage {


  case class Error(msg: String)


  def getIntValue(x: Int): Either[Error,Int] = {
    if(x > 3){
      Right(23)
    }else{
      Left(Error("error fatal: x menor que 3"))
    }
  }

  val rr: Either[Error, String] = getIntValue(24).map(_ + 1).map(_.toString)

  getIntValue(24) match {
    case Left(value) => ???
    case Right(value) => ???
  }

}

object TryUsage {



  def getValue(): Int =
    throw new Exception("fallaste!")



  val a: Try[Int] = Try{
    println("estoy dentro del try")
    getValue()
  }

  def myToString(x: Int): Try[String] = ???

  val f = a.map(myToString).flatten
  val t: Try[String] = a.flatMap(myToString)

  a match {
    case Failure(exception) =>
      getValue()
    case Success(value) =>
      val l: Int = value
  }

  val b = Success(23)




}

object UseOption extends App {

  case class User(name: String, age: Int)

  def getUserFromBd(id: String): Option[User] = {
    //
    Some(User("bernat", 44))

    None
  }





  val user = getUserFromBd("1234")


  val l = user.map(x => x.age + 1)



  val rst: Int = user match {
    case Some(value) => value.age
    case None =>
      println("")
      0
  }


  val r: Option[Int] = user.map(x => x.age)


  val e: Option[Int] = Option(23)


  val p: Option[Int] = None

  e match {
    case Some(value) => ???
    case None => ???
  }




}
