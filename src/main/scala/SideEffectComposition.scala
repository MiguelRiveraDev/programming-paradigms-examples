object SideEffectComposition {

  def addOne(x: Int): Int = x + 1

  def addTwo(x: Int): Int = addOne(addOne(x))

  val addTwoScala = (addOne _) andThen addOne



  // Composición con side effects
  def addOneWithSideEffect(x: Int): Int = {
    val result = x + 1
    println(s"Result: $result")
    result
  }

  def addTwoWithSideEffects(x: Int): Int = ???


}
