package org.dbshell.connectioninfoutil

import org.dbshell.environment.EnvironmentVars
import org.dbshell.shellmethods.dto.ConnectionInfoUtil
import org.junit.Test
import org.junit.jupiter.api.TestInstance
import java.io.File
import javax.naming.Context

@TestInstance(TestInstance.Lifecycle.PER_CLASS)

class TestCredentials {

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
    fun testGetCredentials() {
        val credentials = ConnectionInfoUtil.getCredentialsFromCurrentConnection()
        assert(credentials != null)
    }
}