package akka

import akka.actor.Actor
import com.google.gson.Gson
import data.Config
import org.apache.log4j.Logger
import scheduler.JobScheduler
import task.JobMessages.FlinkConfigRequest
import utils.HttpUtils

/**
  * Actor to handle /config
  */
class ConfigActor(val path: String, val jobScheduler: JobScheduler) extends Actor {

  private val log = Logger.getLogger(this.getClass)

  private val gson = new Gson()

  override def receive: Receive = {
    case FlinkConfigRequest => {
      val content = HttpUtils.getContent(path)
      if (!content.equalsIgnoreCase("error")) {
        val configResult = gson.fromJson(content, classOf[Config])
        log.info(configResult)
      }
    }
    case _ => log.error(s"unknown message in ${this.getClass}")
  }
}
