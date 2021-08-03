package org.dbshell.jobqueue

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.dbshell.actions.Action
import java.util.*

data class PayLoad<T>(val id: UUID, val action: Action<T>)
data class Results<T>(val id: UUID, val action: Action<T>, val results: T)

class JobQueueWrapper {
    companion object {

        private val om = ObjectMapper().registerModule(KotlinModule())

        fun <T> put(action: Action<T>): UUID {
            val uuid = UUID.randomUUID()
            val payload = PayLoad(uuid, action)
            val data = om.writeValueAsBytes(payload)
            JobQueue.jobQueue.enqueue(data)
            return uuid
        }

        fun get(): PayLoad<*> {
            val data = JobQueue.jobQueue.dequeue()
            val payload = om.readValue(data, PayLoad::class.java)!!
            return payload
        }
    }
}