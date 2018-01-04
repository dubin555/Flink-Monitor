package akka

import akka.actor.Actor
import com.google.gson.Gson
import data.Jobs
import org.apache.log4j.Logger
import scheduler.JobScheduler
import task.JobMessages.{FlinkJobExceptionsRequest, FlinkJobsRequest}
import utils.HttpUtils

/**
  * Actor to handle /jobs
  */
class JobsActor(val path: String, val jobScheduler: JobScheduler) extends Actor {

  private val gson = new Gson()

  private val log = Logger.getLogger(this.getClass)

  override def receive: Receive = {
    case FlinkJobsRequest => {
      val content = HttpUtils.getContent(path)
      if (!content.equalsIgnoreCase("error")) {
        val jobsResult = gson.fromJson(content, classOf[Jobs])
        jobsResult.jobsFailed.toList.foreach(jobID => jobScheduler.post(FlinkJobExceptionsRequest(jobID)))
        log.info(jobsResult)
      }
    }
    case _ => log.error(s"unknown message in ${this.getClass}")
  }
}
