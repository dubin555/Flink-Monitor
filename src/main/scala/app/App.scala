package app

import scheduler.{ContextWaiter, JobScheduler}

/**
  * Main App to run the application, running forever until manually stop it.
  */
object App {

  def main(args: Array[String]): Unit = {
    new JobScheduler().start()
    val waiter = new ContextWaiter
    waiter.waitForStopOrError()
  }

}
