package org.dbshell.reflection.utils

import org.bradfordmiller.simplejndiutils.JNDIUtils
import org.junit.Test

class DatabaseMetadataUtilTest {
    @Test
    fun testDbMdGetSchemas() {

        val connection = JNDIUtils.getJndiConnection("SqlLiteTestInput", "default_ds")
        val dbmd = connection.metaData

        //DatabaseMetadataUtil.getSchemas(dbmd)
    }
}