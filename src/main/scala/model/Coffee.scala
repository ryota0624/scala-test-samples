package model

class Coffee(val amount: Int) {
  require(amount > 0, "amount should be greeter 0")

  // コーヒーを混ぜ合わせる
  def mix(other: Coffee): Coffee = new Coffee(amount = amount + other.amount)
}
