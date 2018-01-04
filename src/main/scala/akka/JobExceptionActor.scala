package akka

import akka.actor.Actor
import com.google.gson.Gson
import data.JobExceptions
import org.apache.log4j.Logger
import scheduler.JobScheduler
import task.JobMessages.{FlinkConfigRequest, FlinkJobExceptionsRequest}
import utils.HttpUtils

/**
  * Actor to handle /jobs/jobid/exceptions
  */
class JobExceptionActor(val path: String, val jobScheduler: JobScheduler) extends Actor {

  private val gson = new Gson()

  private val log = Logger.getLogger(this.getClass)

  override def receive: Receive = {
    case FlinkJobExceptionsRequest(jobID) => {
      val content = HttpUtils.getContent(s"$path/{$jobID}/exceptions")
      if (!content.equalsIgnoreCase("error")) {
        val jobExceptionResult = gson.fromJson(content, classOf[JobExceptions])
        log.info(jobExceptionResult)
      }
    }
    case _ => log.error(s"unknown message in ${this.getClass}")
  }
}
