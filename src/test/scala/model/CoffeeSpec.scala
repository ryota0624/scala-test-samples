package model

import org.scalacheck.Gen
import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec.AnyWordSpecLike
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks
import org.scalatest.prop.TableDrivenPropertyChecks

class CoffeeSpec extends AnyWordSpecLike {
  "constructor" should {
    "throw error when received 0" in {
      an[IllegalArgumentException] should be thrownBy {
        new Coffee(0)
      }
    }

    "throw error when received negative number" in {
      an[IllegalArgumentException] should be thrownBy {
        new Coffee(-1)
      }
    }

    "not throw error when receive positive number" in {
      noException should be thrownBy {
        new Coffee(1)
      }
    }
  }

  "amount" should {
    "be as same as constructor passed amount" in {
      val amount = 1
      new Coffee(amount).amount shouldBe amount
    }
  }
}

class CoffeeTableDrivenSpec
    extends AnyWordSpecLike
    with TableDrivenPropertyChecks {
  "constructor" should {
    val amounts = Table(
      ("statement", "amount"),
      ("amount is zero", 0),
      ("amount is negative number", -1)
    )

    forAll(amounts) { (statement, invalidAmount) =>
      {
        s"throw error when $statement" in {
          an[IllegalArgumentException] should be thrownBy {
            new Coffee(invalidAmount)
          }
        }
      }
    }
  }
}

class CoffeeProperSpec
    extends AnyWordSpecLike
    with ScalaCheckDrivenPropertyChecks {
  "constructor" should {
    val amountGen = Gen.posNum[Int]
    "be as same as constructor passed amount" in {
      forAll(amountGen) { amount =>
        {
          new Coffee(amount).amount shouldBe amount
        }
      }
    }
  }
}
