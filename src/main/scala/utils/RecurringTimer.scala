package utils

import org.apache.log4j.Logger

/**
  * Inspired by Apache Spark, Timer to run some task.
  */
class RecurringTimer(clock: Clock, period: Long, callback: (Long) => Unit, name: String) {

  val log = Logger.getLogger(this.getClass)

  private val thread = new Thread("RecurringTimer - " + name) {
    setDaemon(true)
    override def run() {loop}
  }

  @volatile private var prevTime = -1L
  @volatile private var nextTime = -1L
  @volatile private var stopped = false

  def getStartTime(): Long = {
    (math.floor(clock.getTimeMillis().toDouble / period) + 1).toLong * period
  }

  def start(startTime: Long): Long = synchronized {
    nextTime = startTime
    thread.start()
    log.info(s"Started timer for $name at time $nextTime")
    nextTime
  }

  def start(): Long = {
    start(getStartTime())
  }

  def stop(interruptTimer: Boolean): Long = synchronized {
    if (!stopped) {
      stopped = true
      if (interruptTimer) {
        thread.interrupt()
      }
      thread.join()
      log.info(s"Stopped timer for $name after time $prevTime")
    }
    prevTime
  }

  private def triggerActionForNextInterval(): Unit = {
    clock.waitTillTime(nextTime)
    callback(nextTime)
    prevTime = nextTime
    nextTime += period
    log.debug(s"Callback for $name called at time $prevTime")
  }

  private def loop(): Unit = {
    try {
      while (!stopped) {
        triggerActionForNextInterval()
      }
      triggerActionForNextInterval()
    } catch {
      case e: InterruptedException =>
    }

  }

}
