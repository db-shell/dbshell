package org.dbshell.jobqueue

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class JobQueueConsumer : Runnable {
    companion object {
        val logger: Logger = LoggerFactory.getLogger(JobQueueConsumer::class.java)
    }
    init {
        logger.info("${this.javaClass.canonicalName}:: Initializing target consumer")
    }
    private fun processPayload(payload: PayLoad) {
        val uuid = payload.id
        val action = payload.action
        val result = action.execute()
        result?.let {r ->
            ResultQueueWrapper.put(uuid, r)
        }
    }
    override fun run() {
        val payload = JobQueueWrapper.get()
        payload?.let { p ->
            processPayload(p)
        }
    }
}