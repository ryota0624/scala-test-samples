package model

import org.scalatest.OptionValues._
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

trait MessagesSpec
  extends AnyWordSpecLike
    with Matchers {

  val messages: Messages

  "messages" should {
    "追加済のメッセージを取得できる" in {
      val message = Message("hello")
      messages.add(message)
      val messageOpt = messages.get(message.id)
      messageOpt.value shouldBe message
    }
  }
}
