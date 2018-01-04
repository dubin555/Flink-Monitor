package scheduler

import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock

/**
  * Code for running for a while, either running until some timestamp or running forever.
  */
class ContextWaiter {

  private val lock = new ReentrantLock()
  private val condition = lock.newCondition()

  private var error: Throwable = null

  private var stopped: Boolean = false

  def notifyError(e: Throwable): Unit = {
    lock.lock()
    try {
      error = e
      condition.signalAll()
    } finally {
      lock.unlock()
    }
  }

  def notifyStop(): Unit = {
    lock.lock()
    try {
      stopped = true
      condition.signalAll()
    } finally {
      lock.unlock()
    }
  }

  def waitForStopOrError(timeout: Long = -1): Boolean = {
    lock.lock()
    try {
      if (timeout < 0) {
        while (!stopped && error == null) {
          condition.await()
        }
      } else {
        var nanos = TimeUnit.MILLISECONDS.toNanos(timeout)
        while (!stopped && error == null && nanos > 0) {
          nanos = condition.awaitNanos(nanos)
        }
      }

      if (error != null) throw error
      stopped
    } finally {
      lock.unlock()
    }
  }


}
