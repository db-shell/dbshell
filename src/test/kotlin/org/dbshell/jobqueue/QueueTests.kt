package org.dbshell.jobqueue

import org.dbshell.actions.RunQuery
import org.junit.Test

class QueueTests {
    @Test
    fun testQueue() {
        val sql = "SELECT * from dispensary"
        val rowLimit = 50L
        val rq = RunQuery(sql, rowLimit)
        val jobId = JobQueueWrapper.put(rq)

        val payload = JobQueueWrapper.get()
        val action = payload?.action
        val runQuery = action as? RunQuery

        assert(runQuery == rq)
    }
}