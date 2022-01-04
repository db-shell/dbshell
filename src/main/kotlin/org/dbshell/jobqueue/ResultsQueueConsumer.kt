package org.dbshell.jobqueue

import org.dbshell.results.ResultsHashMap
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ResultsQueueConsumer(consume: () -> Result?): BaseQueueConsumer<Result>(consume) {
    companion object {
        val logger: Logger = LoggerFactory.getLogger(ResultsQueueConsumer::class.java)
    }
    init {
        logger.info("${this.javaClass.canonicalName}:: Initializing target consumer")
    }
    override fun process(data: Result) {
        val uuid = data.id
        val r = data.result
        ResultsHashMap.resultsMap[uuid] = r
    }
}