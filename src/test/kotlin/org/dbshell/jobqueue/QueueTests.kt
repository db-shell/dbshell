package org.dbshell.jobqueue

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.dbshell.actions.RunQuery
import org.dbshell.environment.EnvironmentProps
import org.dbshell.environment.EnvironmentVars
import org.junit.Before
import org.junit.Test
import java.io.File
import java.util.*
import javax.naming.Context

class QueueTests {

    @Test
    fun testJobQueue() {
        val sql = "SELECT * from dispensary"
        val rowLimit = 50L
        val rq = RunQuery(sql, rowLimit)
        val jobId = JobQueueWrapper.put(rq)

        val payload = JobQueueWrapper.get()
        val action = payload?.action
        val runQuery = action as? RunQuery

        assert(runQuery == rq)
    }
    @Test
    fun testResultQueue() {

        if(!File("conf/jndi.properties").isFile) {
            System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.osjava.sj.SimpleContextFactory")
            System.setProperty("org.osjava.sj.jndi.shared", "true")
            System.setProperty("org.osjava.sj.root", "src/deploy/bin/conf/jndi")
            System.setProperty("org.osjava.sj.colon.replace", "--")
            System.setProperty("org.osjava.sj.delimiter", "/")
        }
        val ctxJndi = EnvironmentProps.getCurrentContextAndJndi()
        EnvironmentVars.setCurrentContextAndJndi(ctxJndi.context, ctxJndi.jndi)
        val catalog = EnvironmentProps.getCurrentCatalog()
        EnvironmentVars.setCurrentCatalog(catalog)
        val schema = EnvironmentProps.getCurrentSchema()
        EnvironmentVars.setCurrentSchema(schema)

        val uuid = UUID.randomUUID()
        val sql = "SELECT * from weed_scrape.dispensary"
        val rowLimit = 50L
        val rq = RunQuery(sql, rowLimit)
        val actionResult = rq.execute()
        ResultQueueWrapper.put(uuid, actionResult)
        assert(1 == 1)
    }
}