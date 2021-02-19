package model

import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model._

import java.net.URI
import java.util.UUID
import scala.jdk.CollectionConverters.{MapHasAsJava, MapHasAsScala}

case class Message(id: String, text: String)
object Message {
  def apply(text: String): Message =
    Message(UUID.randomUUID().toString, text)
}

trait Messages {
  def add(message: Message)
  def get(id: String): Option[Message]
}

object MessagesOnDymamodb {
  private val tableName = "messages"

  def createTable(messages: MessagesOnDymamodb): Unit = {

    messages.client.createTable(
      CreateTableRequest
        .builder()
        .tableName(tableName)
        .attributeDefinitions(
          AttributeDefinition.builder
            .attributeName("id")
            .attributeType(ScalarAttributeType.S)
            .build()
        )
        .keySchema(
          KeySchemaElement.builder
            .attributeName("id")
            .keyType(KeyType.HASH)
            .build()
        )
        .provisionedThroughput(
          ProvisionedThroughput
            .builder()
            .readCapacityUnits(100)
            .writeCapacityUnits(100)
            .build()
        )
        .build()
    )
  }
}

case class MessagesOnDymamodb(endpoint: URI, region: Region) extends Messages {
  import MessagesOnDymamodb._
  private val client: DynamoDbClient = {
    DynamoDbClient
      .builder()
      .endpointOverride(endpoint)
      .region(region)
      .build()
  }

  override def add(message: Message): Unit = {
    val request = PutItemRequest
      .builder()
      .tableName(tableName)
      .item(
        Map(
          "id" -> message.id,
          "text" -> message.text
        ).view
          .mapValues(value => AttributeValue.builder().s(value).build())
          .toMap
          .asJava
      )
    client.putItem(request.build())
  }

  override def get(id: String): Option[Message] = {
    val key = AttributeValue.builder().s(id).build()
    val request =
      GetItemRequest
        .builder()
        .tableName(tableName)
        .key(Map("id" -> key).asJava)
        .build()
    val resultMap = client.getItem(request).item().asScala
    if (resultMap.isEmpty) {
      None
    } else {
      Some(
        Message(
          resultMap("id").s(),
          resultMap("text").s()
        )
      )
    }
  }
}
