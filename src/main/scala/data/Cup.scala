package data

class Cup private (val capacity: Int, val coffee: Option[Coffee]) {
  require(coffee.fold(0)(_.amount) < capacity, "capacity exceeded")
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
  def apply(capacity: Int): Cup = new Cup(capacity, None)
}
