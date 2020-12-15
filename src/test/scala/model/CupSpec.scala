package model

import org.scalacheck.Gen
import org.scalatest.OptionValues._
import org.scalatest.TryValues._
import org.scalatest.matchers.should.Matchers._
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.wordspec.AnyWordSpecLike
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

import scala.util.Try

class CupSpec
    extends AnyWordSpecLike
    with ScalaCheckDrivenPropertyChecks
    with TableDrivenPropertyChecks {
  "constructor" should {
    "throw exception by negative number" in {
      forAll(Gen.negNum[Int]) { capacity =>
        val cupT = Try(
          Cup(capacity)
        )
        cupT.failure.exception shouldBe a[IllegalArgumentException]
        cupT.failure.exception.getMessage should include(
          Cup.Failures.`capacity should be greeter 0`
        )
      }
    }

    val capacities = Table(
      ("capacity", "exception_message"),
      (0, Cup.Failures.`capacity should be greeter 0`),
      (Cup.MaxCapacity + 1, Cup.Failures.`capacity is maximum exceeded`)
    )
    forAll(capacities) { (capacity, message) =>
      s"throw exception cause by $message" in {
        val cupT = Try(Cup(capacity))
        cupT.failure.exception shouldBe a[IllegalArgumentException]
        cupT.failure.exception.getMessage should include(
          message
        )
      }
    }

  }

  "pourIn" should {
    "throw exception" in {
      val gen: Gen[(Int, Coffee)] = for {
        capacity <- Gen.choose(2, Cup.MaxCapacity)
        coffee <- Generators.coffee.gen()
      } yield (capacity, coffee)
      forAll(gen) {
        case (capacity, coffee) =>
          val cupT = Try(
            Cup(capacity).pourIn(
              coffee
            )
          )
          cupT.failure.exception shouldBe a[IllegalArgumentException]
          cupT.failure.exception.getMessage should include(
            Cup.Failures.`amount is capacity exceeded`
          )
      }
    }

    "coffee should be as same as mixed" in {
      val gen: Gen[(Int, Coffee, Coffee)] = for {
        capacity <- Gen.choose(2, Cup.MaxCapacity)
        one <- Generators.coffee.gen(maxAmount = capacity - 1)
        two <- Generators.coffee.gen(maxAmount = capacity - one.amount)
      } yield (capacity, one, two)
      forAll(gen) {
        case (capacity, coffee1, coffee2) =>
          noException should be thrownBy {
            Cup(capacity)
              .pourIn(coffee1)
              .pourIn(coffee2)
              .coffee
              .value shouldBe coffee1.mix(coffee2)
          }
      }
    }
  }
}
