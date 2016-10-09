reated by gregoirelejay on 10/06/2016.
  */
 import org.apache.spark.SparkContext
 import org.apache.spark.SparkContext._
 import org.apache.spark.SparkConf
 import org.apache.spark.rdd.RDD

 import org.apache.spark.streaming._
 //import org.apache.spark.streaming.kafka._
 //
 //import org.apache.spark.streaming.{Seconds, StreamingContext}
 //
 //import com.typesafe.config._
 //
 //object Main {
 //  def main(args: Array[String]) {
 //      if (args.length < 3) {
 //            System.err.println("Usage: SparkGrep <host> <input_file> <match_term>")
 //                  System.exit(1)
 //                      }
 //                          val appConf = ConfigFactory.load("dam")
 //                              val sparkConf = new SparkConf().setAppName("SparkKafka").setMaster(args(0))
 //                              //    val sc = new SparkContext(sparkConf)
 //
 //                                  val awsAccessKeyId = appConf.getString("s3.awsAccessKeyId")
 //                                      val awsSecretAccessKey = appConf.getString("s3.awsSecretAccessKey")
 //
 //                                      //    sc.hadoopConfiguration.set("fs.s3n.awsAccessKeyId", awsAccessKeyId)
 //                                      //    sc.hadoopConfiguration.set("fs.s3n.awsSecretAccessKey", awsSecretAccessKey)
 //
 //
 //                                          var streamingContext = new StreamingContext(sparkConf, Seconds(1))
 //
 //                                              val ssc = new StreamingContext("local[2]", "PageViewStream", Seconds(1),
 //                                                    System.getenv("SPARK_HOME"), StreamingContext.jarOfClass(this.getClass).toSeq)
 //
 //                                                        println(ssc)
 //
 //                                                            val lines = ssc.socketTextStream("localhost", 9999)
 //
 //                                                            //    val s3RDD = sc.textFile("s3n://dice-warehouse-dev/events/MRInventoryToS3__2014-08-01T0000_2014-09-01T0000.csv")
 //                                                            //      .map(line => line.split("\t"))
 //                                                            //      .map(n => n(0))
 //
 //                                                            //    s3RDD.takeSample(false, 3, 1).foreach(println)
 //                                                            //
 //                                                            //    println("Counting")
 //                                                            //    println(s3RDD.count)
 //                                                            //    print {
 //                                                            //      "Counted"
 //                                                            //    }
 //                                                            //
 //                                                            //    val inputFile: RDD[String] = sc.textFile(args(1), 2).cache()
 //                                                            //    val matchTerm : String = args(2)
 //                                                            //    val numMatches = inputFile.filter(line => line.contains(matchTerm)).count()
 //                                                            //    println("%s lines in %s contain %s".format(numMatches, args(1), matchTerm))
 //                                                            //    sc.stop()
 //                                                              }
 //                                                              }
 //                                                              /*
 //                                                               * Created by gregoirelejay on 08/10/2016.
 //                                                                */
 //
