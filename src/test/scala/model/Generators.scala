package model

import org.scalacheck.Gen

object Generators {
  def coffee: CoffeeGenerator.type = CoffeeGenerator
}

object CoffeeGenerator {
  def gen(maxAmount: Int = Int.MaxValue): Gen[Coffee] =
    coffee(Gen.choose(1, maxAmount))
  private def coffee(genInt: Gen[Int]): Gen[Coffee] = genInt.map(new Coffee(_))
}
