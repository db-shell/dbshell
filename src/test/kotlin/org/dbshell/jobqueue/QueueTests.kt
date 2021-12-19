package org.dbshell.jobqueue

import org.dbshell.actions.sql.RunQuery
import org.dbshell.environment.EnvironmentVars
import org.junit.Test
import org.junit.jupiter.api.TestInstance
import java.io.File
import java.util.*
import javax.naming.Context

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class QueueTests {

    init {

        if (!File("conf/jndi.properties").isFile) {
            System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.osjava.sj.SimpleContextFactory")
            System.setProperty("org.osjava.sj.jndi.shared", "true")
            System.setProperty("org.osjava.sj.root", "src/deploy/bin/conf/jndi")
            System.setProperty("org.osjava.sj.colon.replace", "--")
            System.setProperty("org.osjava.sj.delimiter", "/")
        }

        val context = "default_ds"
        val jndi = "SqliteChinook"

        EnvironmentVars.currentContextAndJndi(context, jndi)
    }

    @Test
    fun testResultQueue() {

        val uuid = UUID.randomUUID()
        val sql = "SELECT * from albums"
        val rowLimit = 50L
        val rq = RunQuery(sql, rowLimit)
        val actionResult = rq.execute()
        ResultQueueWrapper.put(uuid, actionResult)

        val payload = ResultQueueWrapper.get()
        val action = payload?.result

        assert(action != null)
        assert(action?.isRight!!)
    }
}