
object RefactorNulls {
  case class User(id: Int, name: String, email: String)
  //Precondición: Option, Union types, flatMap, map & for comprehensions


  private val users = Map(
    1 -> User(1, "Alice", "alice@example.com"),
    2 -> User(2, "Bob", null),  // Missing email
    3 -> User(3, "Charlie", "charlie@example.com")
  )

  def findUserById(id: Int): User =
    if (users.contains(id)) users(id) else null

  def getEmail(user: User): String =
    if (user != null && user.email != null) user.email else null

  def printUserEmail(userId: Int): Unit = {
    val user = findUserById(userId)
    if (user != null) {
      val email = getEmail(user)
      if (email != null) {
        println(s"User email: $email")
      } else {
        println("Email not available")
      }
    } else {
      println("User not found")
    }
  }

}