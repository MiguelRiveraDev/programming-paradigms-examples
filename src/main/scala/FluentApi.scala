
//PRE: case classes, collections (List, Map), functions: map, flatMap, filter, etc...

object Data {
  case class Dimensions(x: Int, y: Int, z: Int) {
    def volume: Long = x * y * z
  }

  case class Product(ean: String, price: Int, units: Int)

  case class Box(barcode: String, dimensions: Dimensions, products: List[Product])


  val boxList = List(
    Box("box01", Dimensions(2,2,2), Product("ean01", 2, 3) :: Product("ean02", 3, 40) :: Nil),
    Box("box02", Dimensions(2,5,5), Product("ean04", 1, 54) :: Product("ean05", 30, 33) :: Product("ean06", 7, 1) :: Nil),
    Box("box03", Dimensions(6,3,2), Product("ean01", 2, 5) :: Product("ean02", 3, 10) :: Nil),
    Box("box04", Dimensions(6,3,2), Nil),
  )
}

object FluentApi {

  import Data._

  // 1- Filtrar cajas vacías
  val nonEmptyBoxes: List[Box] = boxList.filter(_.products.nonEmpty)

  // 2- Obtener una lista de productos únicos. Lista de ean's
  val uniqueEans: List[String] = boxList.flatMap(_.products.map(_.ean)).distinct

  // 3- Valor del stock de cada producto: Map[String, Int]
  val stockValuePerProduct: Map[String, Int] =
    boxList
      .flatMap(_.products)
      .groupBy(_.ean)
      .view
      .mapValues(products => products.map(p => p.price * p.units).sum)
      .toMap

  // 4- La caja de mas valor
  val mostValuableBox: Option[Box] =
    boxList
      .map(box => box -> box.products.map(p => p.price * p.units).sum)
      .maxByOption(_._2)
      .map(_._1)
}

