import java.sql.DriverManager
import scala.concurrent.duration.DurationInt

// classes, case classes, generics, HoF, lambdas

object EffectSystemIntuition extends App{

  case class IO[T](func: () => T)

  case class User(id: String, name: String, age: Int)

  def readFromDatabase(id: String): IO[User] =
    IO { () =>

      // bad code!! Connection should be closed, no exceptions handled, etc ...
      val connection = DriverManager.getConnection("jdbc:h2:mem:test")
      val statement = connection.createStatement()
      val resultSet = statement.executeQuery(s"SELECT id, name, age FROM users where id = '$id''")

      val name = resultSet.getString("name")
      val age = resultSet.getInt("age")

      User(id, "John", 34)
    }



  def isAdult(user: User): Boolean = user.age >= 18

  def isAdultById(userId: String):IO[Boolean] ={
    val user: IO[User] = readFromDatabase("1234")
    ???
  }

  /////////////////////////////////////////////

  case class IOComposable[A](func: () => A) {
    def map[B](f: A => B): IOComposable[B] = IOComposable(() => f(func()))
  }

  def readFromDatabaseComposable(id: String): IOComposable[User] =
    IOComposable { () =>

      // bad code!! Connection should be closed, no exceptions handled, etc ...
      val connection = DriverManager.getConnection("jdbc:h2:mem:test")
      val statement = connection.createStatement()
      val resultSet = statement.executeQuery(s"SELECT id, name, age FROM users where id = '$id''")

      val name = resultSet.getString("name")
      val age = resultSet.getInt("age")

      User(id, name, age)
    }


  def isAdultByIdCmp(userId: String):IOComposable[Boolean] = {
    val user: IOComposable[User] = readFromDatabaseComposable("1234")
    user.map( (u: User) => isAdult(u) )
  }

  def isAdultByIdCmp2(userId: String):IOComposable[Boolean] =
    readFromDatabaseComposable("1234").map(isAdult)

  // Simulación


  def readFromDatabaseComposableSimulado(id: String): IOComposable[User] =
    IOComposable{
      () =>
        println("Cargando usuario de  base de datos...")
        Thread.sleep(1.seconds.toMillis)
        println("Usuario cargado")

        User(id, "John", 34)
    }

  val isAdultProgram: IOComposable[Boolean] = readFromDatabaseComposableSimulado("1234").map(isAdult)


  val isAdultResult = isAdultProgram.func()

  println(s"isAdult: $isAdultResult")


  val testPrgrm = isAdultProgram.map(assert)
  testPrgrm.func()
}
