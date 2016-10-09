import org.apache.spark.rdd.RDD
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka._
import org.apache.spark.streaming.{Seconds, StreamingContext}

import _root_.kafka.serializer.StringDecoder

import com.typesafe.config._
import com.typesafe.scalalogging.{LazyLogging}
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.log4j.{Level, LogManager, PropertyConfigurator}
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}

object Main extends LazyLogging {

  def createContext(args: Array[String])
    : StreamingContext = {
    logger.info("Creating new Context")
    val Array(master, zkQuorum, groupId, topics, numThreads) = args

    val sparkConf = new SparkConf()
      .setAppName("SparkKafka")
      .setMaster(master)

    val ssc = new StreamingContext(sparkConf, Seconds(60))
    ssc.checkpoint("tmp")


    val topicsSeq = topics.split(",").toSet
    val kafkaParams = Map[String, String](
      ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> zkQuorum,
      ConsumerConfig.GROUP_ID_CONFIG -> groupId
    )

    val messagesDStream: InputDStream[(String, String)] = {
      KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topicsSeq)
    }

    messagesDStream.foreachRDD { rdd =>
      rdd.collect().foreach(println)
    }

    ssc
  }

  def main(args: Array[String]) {
    if (args.length < 2) {
      System.err.println("Usage: KafkaWordCount <zkQuorum> <group> <topics> <numThreads>")
      System.exit(1)
    }
    println(args)
    println(args(0))
    val Array(master, zkQuorum, group, topics, numThreads) = args
    val appConf = ConfigFactory.load("dam")
    val checkpointDirectory = "tmp"

    val ssc = StreamingContext.getOrCreate(checkpointDirectory,
      () => createContext(args))

    ssc.start()             // Start the computation
    ssc.awaitTermination()  // Wait for the computation to terminate

    ssc.stop(stopSparkContext = true, stopGracefully = true)
  }
}

