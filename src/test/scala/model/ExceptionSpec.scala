package model

import org.scalatest.wordspec.AnyWordSpecLike
import org.scalatest.matchers.should.Matchers._

class ExceptionSpec extends AnyWordSpecLike {
  class ClassData(val n: Int)
  case class CaseClassData(n: Int)
  "value" should {
    "equal defined case class" in {
      val data1 = CaseClassData(1)
      val data2 = CaseClassData(1)
      data1 shouldBe data2
    }

    "equal defined class" in {
      val data1 = new ClassData(1)
      val data2 = new ClassData(1)
      data1 shouldBe data2
    }

    "equal exception" in {
      val exception1 = new IllegalArgumentException("name")
      val exception2 = new IllegalArgumentException("name")
      exception1 shouldBe exception2
    }
  }
}
