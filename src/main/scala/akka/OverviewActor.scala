package akka

import akka.actor.Actor
import com.google.gson.Gson
import data.Overview
import org.apache.log4j.Logger
import scheduler.JobScheduler
import task.JobMessages.FlinkOverviewRequest
import utils.HttpUtils

/**
  * Actor to handle /overview
  */
class OverviewActor(val path: String, val jobScheduler: JobScheduler) extends Actor{

  private val log = Logger.getLogger(this.getClass)

  private val gson = new Gson()

  override def receive: Receive = {
    case FlinkOverviewRequest => {
      val content = HttpUtils.getContent(path)
      if (!content.equalsIgnoreCase("error")) {
        val overviewResult = gson.fromJson(content, classOf[Overview])
        log.info(overviewResult)
      }
    }
    case _ => log.error(s"unknown message in ${this.getClass}")
  }
}
