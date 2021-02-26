package model

import com.dimafeng.testcontainers.{ForAllTestContainer, LocalStackContainer}
import org.scalatest.BeforeAndAfterAll
import org.testcontainers.containers.localstack.{
  LocalStackContainer => JavaLocalStackContainer
}
import software.amazon.awssdk.regions.Region

import java.net.URI

class LocalStackContainerMessagesSpec
    extends MessagesSpec
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
        container
          .endpointConfiguration(JavaLocalStackContainer.Service.DYNAMODB)
          .getServiceEndpoint
      ),
      Region.of("ap-northeast-1")
    )
  }
}
