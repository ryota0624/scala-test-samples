package model

import scala.util.Try

case class Coffee(amount: Int) {
  require(amount > 0, "amount should be greeter 0")

  // コーヒーを混ぜ合わせる
  def mix(other: Coffee): Coffee = new Coffee(amount = amount + other.amount)
}

object Coffee {
  def apply(amount: Int): Try[Coffee] = Try(new Coffee(amount))
}
