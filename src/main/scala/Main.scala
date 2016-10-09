import _root_.kafka.serializer.StringDecoder
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.spark.streaming.kafka.KafkaUtils

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.{Seconds, StreamingContext}

import app.SKApp

object Main extends SKApp {

  def buildKafkaParameters(): Map[String, String] = {
    Map[String, String](
      ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> Quorum,
      ConsumerConfig.GROUP_ID_CONFIG -> Configuration.getString("streaming.groupId")
    )
  }

  def createContext(): StreamingContext = {

    val sparkMaster = Configuration.getString("spark.master")
    val sparkAppName = Configuration.getString("spark.appName")

    logger.warn(s"Creating new Context on master: ${sparkMaster} with name: ${sparkAppName}")

    val topics = {
      Configuration
        .getString("streaming.topics")
        .split(",")
        .toSet
    }
    val kafkaParameters = buildKafkaParameters()

    val sparkConf = new SparkConf()
      .setAppName(sparkAppName)
      .setMaster(sparkMaster)

    val ssc = new StreamingContext(sparkConf, Seconds(60))
    ssc.checkpoint(CheckpointDirectory)

    val messagesDStream: InputDStream[(String, String)] = {
      KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParameters, topics)
    }

    messagesDStream.foreachRDD { rdd =>
      rdd.collect().foreach(println)
    }

    ssc
  }

  def main(args: Array[String]) {
    val ssc = StreamingContext.getOrCreate(CheckpointDirectory,
      () => createContext())

    ssc.start()             // Start the computation
    ssc.awaitTermination()  // Wait for the computation to terminate

    ssc.stop(stopSparkContext = true, stopGracefully = true)
  }
}

