package scheduler

import akka.actor.{ActorSystem, Props}
import akka.{ConfigActor, JobExceptionActor, JobsActor, OverviewActor}
import com.typesafe.config.ConfigFactory
import org.apache.log4j.Logger
import task.JobMessages._
import utils.HttpUtils.concatHttpFullPath

/**
  * Flink Monitor JobScheduler
  */
class JobScheduler {

  private val log = Logger.getLogger(this.getClass)

  private var eventLoop: EventLoop[JobSchedulerEvent] = null

  private var jobGenerator = new JobGenerator(this)

  private val conf = ConfigFactory.load()

  implicit val host = conf.getString("flink.monitor.restful.host")

  val system = ActorSystem("ActorSystem")
  val configActor = system.actorOf(Props(new ConfigActor(concatHttpFullPath("/config"), this)), name = "configActor")
  val overviewActor = system.actorOf(Props(new OverviewActor(concatHttpFullPath("/overview"), this)), name = "overviewActor")
  val jobsActor = system.actorOf(Props(new JobsActor(concatHttpFullPath("/jobs"), this)), name = "jobsActor")
  val jobExceptionActor = system.actorOf(Props(new JobExceptionActor(concatHttpFullPath("/jobs"), this)), name = "jobExceptionActor")


  def start(): Unit = synchronized {
    if (eventLoop != null) return

    log.debug("Start JobScheduler")
    eventLoop = new EventLoop[JobSchedulerEvent]("JobScheduler") {
      override def onError(e: Throwable): Unit = reportError("Error in job scheduler", e)

      override def onReceive(event: JobSchedulerEvent): Unit = processEvent(event)
    }

    eventLoop.start()

    jobGenerator.start()
  }

  def post(jobSchedulerEvent: JobSchedulerEvent): Unit = {
    eventLoop.post(jobSchedulerEvent)
  }

  private def processEvent(event: JobSchedulerEvent): Unit = {
    try {
      event match {
        case FlinkConfigRequest => handleFlinkConfigRequest()
        case FlinkOverviewRequest => handleFlinkOverviewRequest()
        case FlinkJobsRequest => hanleFlinkJobsRequest()
        case FlinkJobExceptionsRequest(jobID) => handleFlinkJobExceptionsRequest(jobID)
      }
    } catch {
      case e: Throwable =>
        reportError("Error in job scheduler", e)
    }

  }

  private def handleFlinkConfigRequest(): Unit = {
    log.info("Get Flink /config request")
    configActor ! FlinkConfigRequest
  }

  private def handleFlinkOverviewRequest(): Unit = {
    log.info("get Flink /overview request")
    overviewActor ! FlinkOverviewRequest
  }

  private def hanleFlinkJobsRequest(): Unit = {
    log.info("get Flink /jobs request")
    jobsActor ! FlinkJobsRequest
  }

  private def handleFlinkJobExceptionsRequest(jobID: String): Unit = {
    log.info(s"get Flink job exceptions requst -> $jobID")
    jobExceptionActor ! FlinkJobExceptionsRequest(jobID)
  }

  private def reportError(msg: String, e: Throwable): Unit = {
    log.error(msg, e)
  }

}
