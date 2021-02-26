package model

import com.dimafeng.testcontainers.{ForAllTestContainer, LocalStackContainer}
import org.scalatest.BeforeAndAfterAll
import org.scalatest.OptionValues._
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import org.testcontainers.containers.localstack.{
  LocalStackContainer => JavaLocalStackContainer
}
import software.amazon.awssdk.regions.Region

import java.net.URI

class LocalStackContainerMessagesSpec
    extends AnyWordSpecLike
    with Matchers
    with BeforeAndAfterAll
    with ForAllTestContainer {
  override val container: LocalStackContainer = LocalStackContainer(services =
    Seq(JavaLocalStackContainer.Service.DYNAMODB)
  )
  override def beforeAll(): Unit = {
    super.beforeAll()

    MessagesOnDymamodb.createTable(messages)
  }

  lazy val messages: MessagesOnDymamodb = {
    MessagesOnDymamodb(
      new URI(
        container.endpointConfiguration(JavaLocalStackContainer.Service.DYNAMODB).getServiceEndpoint
      ),
      Region.of("ap-northeast-1")
    )
  }

  "messages" should {
    "追加済のメッセージを取得できる" in {
      val message = Message("hello")
      messages.add(message)
      val messageOpt = messages.get(message.id)
      messageOpt.value shouldBe message
    }
  }
}
