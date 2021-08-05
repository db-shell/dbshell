package org.dbshell.jobqueue

import org.dbshell.results.ResultsHashMap
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ResultsQueueConsumer: Runnable {
    companion object {
        val logger: Logger = LoggerFactory.getLogger(ResultsQueueConsumer::class.java)
    }
    init {
        logger.info("${this.javaClass.canonicalName}:: Initializing target consumer")
    }
    private fun processResult(result: Result) {
        val uuid = result.id
        val r = result.result
        ResultsHashMap.resultsMap[uuid] = r
    }
    override fun run() {
        while(true) {
            val result = ResultQueueWrapper.get()
            result?.let {r ->
                processResult(r)
            }
        }
    }
}