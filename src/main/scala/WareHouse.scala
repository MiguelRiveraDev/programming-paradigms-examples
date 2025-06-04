object WareHouse {

  sealed trait BoxType
  case object Little extends BoxType
  case object Big extends BoxType

  case class Dimensions(length: Double, width: Double, height: Double)

  case class Box(
                  barcode: String,
                  dimensions: Dimensions,
                  weight: Double,
                  boxType: BoxType,
                  ean: String
                )

  sealed trait ConveyorStep
  case object InitialScale extends ConveyorStep
  case object VolumetricScanner extends ConveyorStep
  case object OverWeightReject extends ConveyorStep
  case object SizeReject extends ConveyorStep

  sealed trait PickupId
  case object Pickup1 extends PickupId
  case object Pickup2 extends PickupId

  sealed trait Location
  case class Conveyor(step: ConveyorStep) extends Location
  case class Pickup(id: PickupId) extends Location
  case class Cell(x: Int, y: Int) extends Location

  sealed trait TransportStatus
  case object Waiting extends TransportStatus
  case object Executing extends TransportStatus
  case object Finished extends TransportStatus

  case class Transport(
                        from: Location,
                        to: Location,
                        status: TransportStatus
                      )

  sealed trait Rotation
  case object Alta extends Rotation
  case object Baja extends Rotation

  trait AisleMap {
    def get(x: Int, y: Int): Option[Box]
    def getFreeCells: List[Cell]
  }

  trait PlacementStrategy {
    def chooseCell(
                    freeCells: List[Cell],
                    box: Box,
                    eanRotationMap: Map[String, Rotation]
                  ): Option[Cell]
  }

  class RotationBasedPlacement(dropOffCell: Cell) extends PlacementStrategy {

    private def distance(cell: Cell, ref: Cell): Int =
      Math.abs(cell.x - ref.x) + Math.abs(cell.y - ref.y)

    override def chooseCell(
                             freeCells: List[Cell],
                             box: Box,
                             eanRotationMap: Map[String, Rotation]
                           ): Option[Cell] = {

      val rotation = eanRotationMap.getOrElse(box.ean, Baja)

      val sorted = rotation match {
        case Alta => freeCells.sortBy(distance(_, dropOffCell))
        case Baja => freeCells.sortBy(distance(_, dropOffCell)).reverse
      }

      sorted.headOption
    }
  }

  class FastEntryPlacement(pickupCell: Cell) extends PlacementStrategy {

    private def distance(cell: Cell, ref: Cell): Int =
      Math.abs(cell.x - ref.x) + Math.abs(cell.y - ref.y)

    override def chooseCell(
                             freeCells: List[Cell],
                             box: Box,
                             eanRotationMap: Map[String, Rotation]
                           ): Option[Cell] = {
      freeCells.sortBy(distance(_, pickupCell)).headOption
    }
  }

    class BoxRouter(
                     maxWeight: Int,
                     maxVolume: Long,
                     placementStrategy: PlacementStrategy
                   ) {
      def boxInLocation(
                         location: Location,
                         box: Box,
                         bigBoxAisle: AisleMap,
                         littleBoxAisle: AisleMap,
                         eanRotationMap: Map[String, Rotation]
                       ): Option[Cell] = {

        val volume = box.dimensions.length * box.dimensions.width * box.dimensions.height
        if (box.weight > maxWeight || volume > maxVolume) return None

        val aisleMap = box.boxType match {
          case Big => bigBoxAisle
          case Little => littleBoxAisle
        }

        val freeCells = aisleMap.getFreeCells
        placementStrategy.chooseCell(freeCells, box, eanRotationMap)
      }
    }
}
