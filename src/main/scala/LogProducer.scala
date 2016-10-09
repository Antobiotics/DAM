import kafka.producer.ProducerConfig
import java.util.Properties

import scala.util.Random
import kafka.producer.Producer
import kafka.producer.KeyedMessage
import java.util.Date


import app.SKApp

object LogProducer extends SKApp {

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
      val runtime = new Date().getTime
      val ip = "192.168.2." + rnd.nextInt(255)
      val msg = runtime + "," + nEvents + ",www.example.com," + ip
      val data = new KeyedMessage[String, String](topic, ip, msg)
      producer.send(data)
    }

    System.out.println("sent per second: " + events.toInt * 1000 / (System.currentTimeMillis() - t))
    producer.close()
  }
}
