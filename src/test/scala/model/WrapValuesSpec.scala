package model

import org.scalatest.OptionValues._
import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec.{
  AnyWordSpecLike,
  AsyncWordSpec,
  AsyncWordSpecLike
}

import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.Try

case class SampleData(number: Int) {
  require(number > 0, "number should be positive")
}

object SampleData {
  def tryCreate(n: Int): Try[SampleData] = Try(SampleData(n))
  def createOpt(n: Int): Option[SampleData] = Try(SampleData(n)).toOption
  def createEither(n: Int): Either[Throwable, SampleData] =
    Try(SampleData(n)).toEither
  def createFuture(
      n: Int
  )(implicit ctx: ExecutionContext): Future[SampleData] = Future(SampleData(n))
}

class WrapValuesSpec extends AnyWordSpecLike {
  "SampleData" should {
    "createOpt" in {
      val number = -1
      val data = SampleData.createOpt(number)
      data.get shouldBe number
    }

    "createOpt use OptionValues" in {
      import org.scalatest.OptionValues._
      val number = -1
      val data = SampleData.createOpt(number)
      data.value shouldBe number
    }

    "createTry use TryValues" in {
      import org.scalatest.TryValues._
      val number = -1
      val data = SampleData.tryCreate(number)
      data.success.value shouldBe number
    }

    "createTry use EitherValues" in {
      import org.scalatest.EitherValues._
      val number = -1
      val data = SampleData.createEither(number)
      data.swap.left.value shouldBe number
    }

    import scala.concurrent.ExecutionContext.Implicits.global

    "createFuture" in {
      val number = -1
      val dataF = SampleData.createFuture(number)
      val result = Await.result(dataF, 500.millis)
      result.number shouldBe number
    }

    "createFuture using ScalaFutures" in {
      import org.scalatest.concurrent.ScalaFutures._

      val number = -1
      val dataF = SampleData.createFuture(number)
      dataF.futureValue.number shouldBe number
    }

    "assertion createFuture exception using ScalaFutures" in {
      import org.scalatest.concurrent.ScalaFutures._
      val number = -1
      val dataF = SampleData.createFuture(number)
      val exception = dataF.failed.futureValue
      exception shouldBe a[IllegalArgumentException]
      exception.getMessage should include("number should be positive")
    }

    "createFuture using whenReady" in {
      import org.scalatest.concurrent.ScalaFutures._

      val number = -1
      val dataF = SampleData.createFuture(number)
      whenReady(dataF) { data =>
        data.number shouldBe number
      }
    }
  }
}

class AsyncWrapValuesSpec extends AsyncWordSpec {
  "createFuture using AsyncWordSpec" in {
    val number = -1
    for {
      data <- SampleData.createFuture(number)
    } yield data.number shouldBe number
  }

  "createFuture using AsyncWordSpec catch exception" in {
    val number = -1
    for {
      data <- SampleData.createFuture(number)
    } yield data.number shouldBe number
  }

  "assertion createFuture exception using AsyncWordSpec" in {
    val number = -1
    an[IllegalArgumentException] should be thrownBy {
      for {
        data <- SampleData.createFuture(number)
      } yield data.number shouldBe number
    }
  }
}
