package model

import com.dimafeng.testcontainers.{ForAllTestContainer, GenericContainer}
import org.scalatest.BeforeAndAfterAll
import org.testcontainers.images.builder.ImageFromDockerfile
import software.amazon.awssdk.regions.Region

import java.net.URI

class GenericContainerMessagesSpec
    extends MessagesSpec
    with BeforeAndAfterAll
    with ForAllTestContainer {
//  override val container: GenericContainer =
//    GenericContainer(
//      new ImageFromDockerfile().withDockerfile(
//        Path.of(getClass.getResource("/Dockerfile").getFile)
//      ),
//      env = Map(
//        "DEFAULT_REGION" -> "ap-northeast-1"
//      ),
//      exposedPorts = Seq(4566)
//    )

  override val container: GenericContainer =
    GenericContainer(
      new ImageFromDockerfile()
        .withDockerfileFromBuilder(builder =>
          builder
            .from("localstack/localstack")
            .env("SERVICES", "dynamodb")
            .build()
        ),
      env = Map(
        "DEFAULT_REGION" -> "ap-northeast-1",
      ),
      exposedPorts = Seq(4566)
    )
//
//  override val container: GenericContainer =
//    GenericContainer(
//      "localstack/localstack",
//      env = Map(
//        "DEFAULT_REGION" -> "ap-northeast-1",
//        "SERVICES" -> "dynamodb"
//      ),
//      exposedPorts = Seq(4566)
//    )

  override def beforeAll(): Unit = {
    super.beforeAll()

    MessagesOnDymamodb.createTable(messages)
  }

  lazy val messages: MessagesOnDymamodb = {
    val (localstackContainerHost, localstackContainerHostPort) =
      (
        container.host,
        container.mappedPort(4566)
      )

    MessagesOnDymamodb(
      new URI(
        s"http://${localstackContainerHost}:${localstackContainerHostPort}"
      ),
      Region.of("ap-northeast-1")
    )
  }
}
