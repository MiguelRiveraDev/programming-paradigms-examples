object SideEffectComposition extends App{

  def addOne(x: Int): Int = x + 1

  def addTwo(x: Int): Int = addOne(addOne(x))


  val addTwoScala: Int => Int = (addOne _) andThen addOne





  // Composición con side effects
  def addOneWithSideEffect(x: Int): (Int, String) = {
    val result = x + 1
    (result, "Result")
  }

  def addTwoWithSideEffects(x: Int): (Int,List[String]) = {
      val result1 = addOneWithSideEffect(x)
      val result2 = addOneWithSideEffect(result1._1)
    (result2._1, List(result1._2,result2._2) )
  }


  val r: (Int, List[String]) = addTwoWithSideEffects(8)
  println(r._2.last)


}
