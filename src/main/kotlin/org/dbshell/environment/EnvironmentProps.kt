package org.dbshell.environment

import java.io.File
import java.util.*

class EnvironmentProps {

    data class QueueInfo(val path: String, val name: String)

    companion object {

        private val f = File("conf/settings.properties")
        private val exists = f.exists()

        fun getCurrentContextAndJndi(): ContextAndJndi {
            if (exists) {
                val p = Properties()
                f.inputStream().use { fis ->
                    p.load(fis)
                    return ContextAndJndi(p.getProperty("currentContext"), p.getProperty("currentJndi"))
                }
            } else {
                return ContextAndJndi("", "")
            }
        }

        fun setCurrentContextandJndi(context: String, jndi: String) {
            if (exists) {
                f.inputStream().use { fis ->
                    val p = Properties()
                    p.load(fis)
                    p.setProperty("currentContext", context)
                    p.setProperty("currentJndi", jndi)
                    f.outputStream().use { fos ->
                        p.store(fos, null)
                    }
                }
            }
        }

        fun getCurrentCatalog(): String {
            if (exists) {
                val p = Properties()
                f.inputStream().use { fis ->
                    p.load(fis)
                    return p.getProperty("currentCatalog")
                }
            } else {
                return ""
            }
        }

        fun setCurrentCatalog(catalog: String) {
            if (exists) {
                f.inputStream().use { fis ->
                    val p = Properties()
                    p.load(fis)
                    p.setProperty("currentCatalog", catalog)
                    f.outputStream().use { fos ->
                        p.store(fos, null)
                    }
                }
            }
        }

        fun getCurrentSchema(): String {
            if (exists) {
                val p = Properties()
                f.inputStream().use { fis ->
                    p.load(fis)
                    return p.getProperty("currentSchema")
                }
            } else {
                return ""
            }
        }

        fun setCurrentSchema(schema: String) {
            if (exists) {
                f.inputStream().use { fis ->
                    val p = Properties()
                    p.load(fis)
                    p.setProperty("currentSchema", schema)
                    f.outputStream().use { fos ->
                        p.store(fos, null)
                    }
                }
            }
        }

        private fun getQueueInfo(queuePath: String, queueName: String): QueueInfo {
            if(exists) {
                val p = Properties()
                f.inputStream().use { fis ->
                    p.load(fis)
                    return QueueInfo(p.getProperty(queuePath), p.getProperty(queueName))
                }
            } else {
                val p = Properties()
                val f = File("src/deploy/lib/conf/settings.properties")
                f.inputStream().use { fis ->
                    p.load(fis)
                    return QueueInfo(p.getProperty(queuePath), p.getProperty(queueName))
                }
            }
        }

        fun getJobQueueInfo(): QueueInfo {
            return getQueueInfo("jobqueue.path", "jobqueue.data.name")
        }
        fun getResultsQueueInfo(): QueueInfo {
            return getQueueInfo("jobqueue.results", "jobqueue.results.name")
        }
    }
}