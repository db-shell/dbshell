package org.dbshell.jobqueue

abstract class BaseQueueConsumer<T>(val consume: () -> T?): Runnable {
    abstract fun process(data: T)
    override fun run() {
        val payload = consume()
        payload?.let { p ->
            process(p)
        }
    }
}