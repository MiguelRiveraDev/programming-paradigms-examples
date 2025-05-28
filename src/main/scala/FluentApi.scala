
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
  // 1- Filtrar cajas vacías
  // 2- Obtener una lista de productos únicos. Lista de ean's
  // 3- Valor del stock de cada caja: Map[String, Int]
  // 4- La caja de mas valor
}
