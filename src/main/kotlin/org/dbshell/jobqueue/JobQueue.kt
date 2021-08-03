package org.dbshell.jobqueue

import com.leansoft.bigqueue.BigQueueImpl
import org.dbshell.environment.EnvironmentProps

class JobQueue {
    companion object {
        val jobQueueInfo = EnvironmentProps.getJobQueueInfo()
        val jobQueue = BigQueueImpl(jobQueueInfo.path, jobQueueInfo.name)
        val resultsQueueInfo = EnvironmentProps.getResultsQueueInfo()
        val resultsQueue = BigQueueImpl(resultsQueueInfo.path, resultsQueueInfo.name)
    }
}