package model

import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec.AnyWordSpecLike
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class CoffeeSpec extends AnyWordSpecLike with ScalaCheckDrivenPropertyChecks {
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
