package task

/**
  * The messages for represent the Flink monitor job type
  */

object JobMessages {

  sealed trait JobSchedulerEvent

  case object FlinkConfigRequest extends JobSchedulerEvent
  case object FlinkOverviewRequest extends JobSchedulerEvent
  case object FlinkJobsRequest extends JobSchedulerEvent
  case class FlinkJobExceptionsRequest(jobID: String) extends JobSchedulerEvent
}

