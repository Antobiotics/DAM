name := "dam"

version := "1.0"

scalaVersion := "2.11.8"

val sparkVersion = "2.0.1"

libraryDependencies ++= Seq(
  "com.typesafe" % "config" % "1.3.0",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0",
  "com.typesafe.play" %% "play-json" % "2.4.0",
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-streaming" % sparkVersion,
  "org.apache.spark" %% "spark-streaming-kafka-0-8" % sparkVersion
)