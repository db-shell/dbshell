package org.dbshell.jobqueue

import com.leansoft.bigqueue.BigQueueImpl
import org.dbshell.environment.EnvironmentProps

class JobQueue {
    companion object {
        private val jobQueueInfo = EnvironmentProps.getJobQueueInfo()
        private val resultsQueueInfo = EnvironmentProps.getResultsQueueInfo()
        val jobQueue = BigQueueImpl(jobQueueInfo.path, jobQueueInfo.name)
        val resultsQueue = BigQueueImpl(resultsQueueInfo.path, resultsQueueInfo.name)
    }
}