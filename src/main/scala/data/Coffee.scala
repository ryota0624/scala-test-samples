package data

class Coffee(val amount: Int) {
  // コーヒーを混ぜ合わせる
  def mix(other: Coffee): Coffee = new Coffee(amount = amount + other.amount)
}
