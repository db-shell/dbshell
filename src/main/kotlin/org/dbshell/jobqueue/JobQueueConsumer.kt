package org.dbshell.jobqueue

import org.dbshell.actions.Action
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class JobQueueConsumer : Runnable {
    companion object {
        val logger: Logger = LoggerFactory.getLogger(JobQueueConsumer::class.java)
    }
    init {
        logger.info("${this.javaClass.canonicalName}:: Initializing target consumer")
    }
    private fun processAction(action: Action<*>) {

    }
    override fun run() {
        while(true) {
            val payload = JobQueueWrapper.get()
            val action = payload.action
            processAction(action)
        }
    }
}