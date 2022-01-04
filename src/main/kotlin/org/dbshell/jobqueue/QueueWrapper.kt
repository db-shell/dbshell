package org.dbshell.jobqueue

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.vavr.jackson.datatype.VavrModule
import org.dbshell.actions.Action
import org.dbshell.actions.ActionResult
import java.text.SimpleDateFormat
import java.util.*

data class PayLoad(val id: UUID, val action: Action)
data class Result(val id: UUID, val result: ActionResult)

class JobQueueWrapper {
    companion object {
        private val kotlinModule = KotlinModule.Builder().build()
        private val df = SimpleDateFormat("yyyy-MM-dd HH:mm a z")
        private val om =
            ObjectMapper()
                .registerModule(kotlinModule)
                .setDateFormat(df)

        fun put(action: Action): UUID {
            val uuid = UUID.randomUUID()
            val payload = PayLoad(uuid, action)
            val data = om.writeValueAsBytes(payload)
            JobQueue.jobQueue.enqueue(data)
            return uuid
        }

        @Synchronized fun get(): PayLoad? {
            val check = JobQueue.jobQueue.peek()
            val payload =
            if(check != null) {
                val data = JobQueue.jobQueue.dequeue()
                if(data != null) {
                    om.readValue(data, PayLoad::class.java)!!
                } else {
                    null
                }
            } else {
                null
            }
            return payload
        }
    }
}

class ResultQueueWrapper {
    companion object {

        private val kotlinModule = KotlinModule.Builder().build()
        private val df = SimpleDateFormat("yyyy-MM-dd HH:mm a z")

        private val om =
            ObjectMapper()
                .registerModule(kotlinModule)
                .registerModule(VavrModule())
                .setDateFormat(df)

        fun put(uuid: UUID, actionResult: ActionResult) {
            val result = Result(uuid, actionResult)
            val data = om.writeValueAsBytes(result)
            JobQueue.resultsQueue.enqueue(data)
        }

        @Synchronized fun get(): Result? {
            val check = JobQueue.resultsQueue.peek()
            val result =
                if(check != null) {
                    val data = JobQueue.resultsQueue.dequeue()
                    if(data != null) {
                        om.readValue(data, Result::class.java)!!
                    } else {
                        null
                    }
                } else {
                    null
                }
            return result
        }
    }
}
