package scheduler

import com.typesafe.config.ConfigFactory
import org.apache.log4j.Logger
import task.JobMessages.{FlinkConfigRequest, FlinkJobsRequest, FlinkOverviewRequest}
import utils.{RecurringTimer, SystemClock}

/**
  * Job Generator to generate some FlinkMonitorJob for JobScheduler
  */
class JobGenerator(jobScheduler: JobScheduler) {

  private val log = Logger.getLogger(this.getClass)

  private val config = ConfigFactory.load()

  private val clock = new SystemClock

  private val flinkConfigRequestTimer = new RecurringTimer(clock, config.getLong("flink.monitor.interval.config") * 1000L,
    longTime => jobScheduler.post(FlinkConfigRequest), "FlinkConfigJobGenerator")

  private val flinkOverviewRequestTimer = new RecurringTimer(clock, config.getLong("flink.monitor.interval.overview") * 1000L,
    longTime => jobScheduler.post(FlinkOverviewRequest), "FlinkOverviewJobGenerator")

  private val flinkJobsRequestTimer = new RecurringTimer(clock, config.getLong("flink.monitor.interval.jobs") * 1000L,
    longTime => jobScheduler.post(FlinkJobsRequest), "FlinkJobsJobGenerator")

  def start() = {
    flinkConfigRequestTimer.start()
    flinkOverviewRequestTimer.start()
    flinkJobsRequestTimer.start()
  }

  def stop() = {
    flinkConfigRequestTimer.stop(true)
    flinkOverviewRequestTimer.stop(true)
    flinkJobsRequestTimer.stop(true)
  }


}
