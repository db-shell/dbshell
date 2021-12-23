package org.dbshell.jobqueue

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.dbshell.db.metadata.DatabasePrimitives
import org.junit.Test
import java.text.SimpleDateFormat

class SerializationTests {
    @Test
    fun testSerialize() {
        val kotlinModule = KotlinModule.Builder().build()
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm a z")
        val om = ObjectMapper().registerModule(kotlinModule).setDateFormat(df)

        val kvp = DatabasePrimitives.ConnectionEntries("test", "test")
        val testSet: Set<DatabasePrimitives.ConnectionEntries> = setOf(kvp)

        val typeRef = object: TypeReference<Set<DatabasePrimitives.ConnectionEntries>>(){}
        val ba = om.writeValueAsBytes(testSet)
        val newSet = om.readValue(ba, typeRef)
        assert(testSet == newSet)
    }
}