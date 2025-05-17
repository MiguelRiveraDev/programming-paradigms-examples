// PRE: traits, inheritance
object TimeStampProblem {

  def addTimestamp(str: String): String = {
    val t = System.currentTimeMillis()
    t.toString + ":" + str
  }
}