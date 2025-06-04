object RefactorNulls {
  case class User(id: Int, name: String, email: String)
  //Precondición: Option, Union types, flatMap, map & for comprehensions


  private val users: Map[Int, User] = Map(
    1 -> User(1, "Alice", "alice@example.com"),
    2 -> User(2, "Bob", null),  // Missing email
    3 -> User(3, "Charlie", "charlie@example.com")
  )

  def findUserById(id: Int): Option[User] =
    users.get(id)

  def getEmail(user: User): Option[String] =
    Option(user.email)

  def printUserEmail(userId: Int): Unit = {
    val maybeEmail: Option[String] = for {
      user <- findUserById(userId)
      email <- getEmail(user)
    } yield email

    maybeEmail match {
      case Some(email) => println(s"User email: $email")
      case None =>
        findUserById(userId) match {
          case Some(_) => println("Email not available")
          case None    => println("User not found")
        }
    }
  }
}