package model

import com.dimafeng.testcontainers.{DockerComposeContainer, ExposedService, ForAllTestContainer}
import org.scalatest.BeforeAndAfterAll
import software.amazon.awssdk.regions.Region

import java.io.File
import java.net.URI

class DockerComposeMessagesSpec
  extends MessagesSpec
    with BeforeAndAfterAll
    with ForAllTestContainer {
  override val container: DockerComposeContainer =
    new DockerComposeContainer(
      composeFiles =
        new File(getClass.getResource("/docker-compose.yml").getFile),
      Seq(
        ExposedService("localstack", 4566)
      )
    )

  lazy val messages: Messages = {
    val (localstackContainerHost, localstackContainerHostPort) =
      (
        container.getServiceHost("localstack", 4566),
        container.getServicePort("localstack", 4566)
      )

    val messages = MessagesOnDymamodb(
      new URI(s"http://${localstackContainerHost}:${localstackContainerHostPort}"),
      Region.of("ap-northeast-1")
    )

    MessagesOnDymamodb.createTable(messages)

    messages
  }

}
