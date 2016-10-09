import kafka.producer.ProducerConfig
import java.util.Properties

import scala.util.Random
import kafka.producer.Producer
import kafka.producer.KeyedMessage
import java.util.Date

import play.api.libs.json._
import app.SKApp

import scala.collection.mutable.Buffer

object LogProducer extends SKApp {

  val PropertyTypeChoices = Map[Int, JsValue](
    0 -> JsNumber(1),
    1 -> JsString("a"),
    2 -> JsBoolean(true)
  )

  def randomString(length: Int): String = {
    Random.alphanumeric.take(length).mkString
  }

  def randomValueForPropertyType(rnd: Random, value: JsValue): JsValue = {
       value match {
        case _: JsNumber => JsNumber(rnd.nextInt(100))
        case _: JsBoolean => JsBoolean(rnd.nextBoolean())
        case _: JsString => JsString(rnd.nextString(5))
      }
  }

  def buildRandomProperty(rnd: Random): JsObject = {
    val propertySize = rnd.nextInt(5)
    val properties = Buffer[(String, JsValue)]()

    for(i <- Range(0, propertySize)) {
      val choice = rnd.nextInt(2)
      val propertyType = PropertyTypeChoices(choice)
      val propertyValue = randomValueForPropertyType(rnd, propertyType)
      properties += (i.toString -> propertyValue)
    }

    JsObject(properties.toSeq)
  }

  def buildProperties(): Properties = {
    val props = new Properties()
    props.put("metadata.broker.list", Quorum)
    props.put("serializer.class", "kafka.serializer.StringEncoder")
    props.put("producer.type", "async")
    props
  }

  def main(args: Array[String]): Unit = {
    val Array(events, topic) = args

    val rnd = new Random()
    val config = new ProducerConfig(buildProperties())

    val producer = new Producer[String, String](config)
    val t = System.currentTimeMillis()
    for (nEvents <- Range(0, events.toInt)) {
      val uid = JsString(randomString(24))
      val log = JsObject(
        Seq(
          "_t" -> JsNumber(System.currentTimeMillis / 1000),
          "_p" -> uid,
          "_u" -> JsString("device"),
          "properties" -> buildRandomProperty(rnd)
        )
      )

      val logString = log.toString()
      logger.warn(logString)

      val data = new KeyedMessage[String, String](topic, uid.toString(), logString)
      producer.send(data)
    }

    System.out.println("sent per second: " + events.toInt * 1000 / (System.currentTimeMillis() - t))
    producer.close()
  }
}
