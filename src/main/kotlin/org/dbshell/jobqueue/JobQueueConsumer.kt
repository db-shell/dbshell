package org.dbshell.jobqueue

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class JobQueueConsumer(consume: () -> PayLoad?) : BaseQueueConsumer<PayLoad>(consume) {
    companion object {
        val logger: Logger = LoggerFactory.getLogger(JobQueueConsumer::class.java)
    }
    init {
        logger.info("${this.javaClass.canonicalName}:: Initializing target consumer")
    }
    override fun process(data: PayLoad) {
        val uuid = data.id
        val action = data.action
        val result = action.execute()
        ResultQueueWrapper.put(uuid, result)
    }
}