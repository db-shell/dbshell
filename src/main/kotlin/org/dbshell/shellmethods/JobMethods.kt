package org.dbshell.shellmethods

import org.dbshell.actions.ActionRenderer
import org.dbshell.providers.JobValueProvider
import org.dbshell.results.ResultsHashMap
import org.slf4j.LoggerFactory
import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import org.springframework.shell.standard.ShellOption
import java.util.*

@ShellComponent
class JobMethods: ActionRenderer {
    companion object {
        private val logger = LoggerFactory.getLogger(JobMethods::class.java)
    }
    @ShellMethod("Get job results")
    fun getJobResults(@ShellOption(valueProvider = JobValueProvider::class)uuid: UUID) {
        val result = ResultsHashMap.resultsMap[uuid]
        if(result == null) {
            println(
                "Job id $uuid not found in job queue. Either the id is incorrect or has been deleted from the queue."
            )
        } else {
            renderAction(result)
        }
    }
}