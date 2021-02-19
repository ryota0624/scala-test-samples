package model

import java.util.UUID

case class Message(id: String, text: String)
object Message {
  def apply(text: String): Message =
    Message(UUID.randomUUID().toString, text)
}

trait Messages {
  def add(message: Message)
  def get(id: String): Option[Message]
}
