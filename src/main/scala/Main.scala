import _root_.kafka.serializer.StringDecoder
import com.typesafe.config.{Config, ConfigFactory}
import com.typesafe.scalalogging.LazyLogging
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.spark.streaming.kafka.KafkaUtils

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.{Seconds, StreamingContext}


object Main extends LazyLogging {

  val ConfigFilePrefix = "dam"

  def loadAppConfiguration(file: String): Config = ConfigFactory.load(file)

  def getCheckpointDirectory(configuration: Config): String =
    configuration
      .getString("streaming.checkpointDirectory")

  def buildKafkaParameters(configuration: Config): Map[String, String] = {
    Map[String, String](
      ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> configuration.getString("streaming.quorum"),
      ConsumerConfig.GROUP_ID_CONFIG -> configuration.getString("streaming.groupId")
    )
  }

  def createContext(configuration: Config): StreamingContext = {
    logger.warn("Creating new Context")

    val sparkMaster = configuration.getString("spark.master")
    val sparkAppName = configuration.getString("spark.appName")
    val checkpointDirectory = getCheckpointDirectory(configuration)
    val topics = {
      configuration
        .getString("streaming.topics")
        .split(",")
        .toSet
    }
    val kafkaParameters = buildKafkaParameters(configuration)

    val sparkConf = new SparkConf()
      .setAppName(sparkAppName)
      .setMaster(sparkMaster)

    val ssc = new StreamingContext(sparkConf, Seconds(60))
    ssc.checkpoint(checkpointDirectory)

    val messagesDStream: InputDStream[(String, String)] = {
      KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParameters, topics)
    }

    messagesDStream.foreachRDD { rdd =>
      rdd.collect().foreach(println)
    }

    ssc
  }

  def main(args: Array[String]) {
    val configuration = loadAppConfiguration("dam")
    val checkpointDirectory = getCheckpointDirectory(configuration)

    val ssc = StreamingContext.getOrCreate(checkpointDirectory,
      () => createContext(configuration))

    ssc.start()             // Start the computation
    ssc.awaitTermination()  // Wait for the computation to terminate

    ssc.stop(stopSparkContext = true, stopGracefully = true)
  }
}

