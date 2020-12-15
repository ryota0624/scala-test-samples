package model

case class Cup private (capacity: Int, coffee: Option[Coffee]) {
  import model.Cup.Failures._
  require(capacity > 0, `capacity should be greeter 0`)
  require(capacity < Cup.MaxCapacity, `capacity is maximum exceeded`)
  require(coffee.fold(0)(_.amount) < capacity, `amount is capacity exceeded`)

  // コーヒーを注ぐ
  def pourIn(pured: Coffee): Cup = {
    coffee match {
      case Some(coffee) =>
        new Cup(capacity, Some(coffee.mix(pured)))
      case None =>
        new Cup(capacity, Some(pured))
    }
  }
}

object Cup {
  object Failures {
    val `capacity should be greeter 0` = "capacity should be greeter 0"
    val `capacity is maximum exceeded` = "capacity is maximum exceeded"
    val `amount is capacity exceeded` = "amount is capacity exceeded"
  }

  val MaxCapacity: Int = 5000000
  def apply(capacity: Int): Cup = new Cup(capacity, None)
}
