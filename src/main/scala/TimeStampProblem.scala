// PRE: traits, inheritance
object TimeStampProblem {

  trait IClock {
    def getMillis(): Long
  }

  class ClockImpl extends IClock {
    override def getMillis(): Long = System.currentTimeMillis()
  }

  class TestClock(ticks: Long) extends IClock {
    override def getMillis(): Long = ticks
  }

  def addTimestamp(str: String, clock: IClock): String = {
    val t = clock.getMillis()
    t.toString + ":" + str
  }

  val fixedClock = new TestClock(23)

  addTimestamp("hola", fixedClock)
}