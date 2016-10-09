package app

import com.typesafe.config.{Config, ConfigFactory}
import com.typesafe.scalalogging.LazyLogging

class SKApp extends LazyLogging{
  val ConfigFilePrefix: String = "dam"
  val Configuration: Config = ConfigFactory.load(ConfigFilePrefix)

  val Quorum: String = {
    Configuration
      .getString("streaming.quorum")
  }

  val CheckpointDirectory = {
    Configuration
      .getString("streaming.checkpointDirectory")
  }
}
