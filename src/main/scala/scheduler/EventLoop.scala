package scheduler

import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.{BlockingQueue, LinkedBlockingDeque}

import org.apache.log4j.Logger

import scala.util.control.NonFatal

/**
  * Inspired by Apache Spark
  *
  * Note: The event queue will grow indefinitely. So subclass should make sure `onReceive` can handle events in time.
  * Mainly for dispatch Flink Monitor Job, won't be grow so much, also, is self monitored, if the queue explosion happened, something goes wrong when monitor running.
  */
private[scheduler] abstract class EventLoop[E](name: String) {

  private val log = Logger.getLogger(this.getClass)

  private val eventQueue: BlockingQueue[E] = new LinkedBlockingDeque[E]()

  private val stopped = new AtomicBoolean(false)

  private val eventThread = new Thread(name) {

    setDaemon(true)

    override def run(): Unit = {
      try {
        while (!stopped.get()) {
          val event = eventQueue.take()
          try {
            onReceive(event)
          } catch {
            case NonFatal(e) =>
              try {
                onError(e)
              } catch {
                case NonFatal(e) => log.error("Unexpected error in " + name, e)
              }
          }
        }
      } catch {
        case ie: InterruptedException =>
        case NonFatal(e) => log.error("Unexpected error in " + name, e)
      }
    }
  }

  def start(): Unit = {
    if (stopped.get()) {
      throw new IllegalStateException(name + " has already been stopped")
    }
    // Call onStart before starting the event loop, also before onReceive
    onStart()
    eventThread.start()
  }

  def stop(): Unit = {
    if (stopped.compareAndSet(false, true)) {
      eventThread.interrupt()
      var onStopCalled = false
      try {
        eventThread.join()
        // Call onStop after the event thread exits
        onStopCalled = true
        onStop()
      } catch {
        case ie: InterruptedException =>
          Thread.currentThread().interrupt()
          if (!onStopCalled) {
            // thrown from eventThread.join, need to call onStop() again
            onStop()
          }
      }
    } else {

    }
  }

  /**
    * Put event to the event queue.
    */
  def post(event: E): Unit = {
    eventQueue.put(event)
  }

  def isActive: Boolean = eventThread.isAlive

  /**
    * Invoked when `start()` is called but before the event thread starts
    */
  def onStart(): Unit = {}

  /**
    * Invoked when `stop()` is called but after the event thread ends
    */
  def onStop(): Unit = {}

  /**
    * Invoked in the event thread when polling events from the event queue
    */
  def onReceive(event: E): Unit

  /**
    * Invoked if `onReceive` throws any non fatal error
    */
  def onError(e: Throwable): Unit

}
