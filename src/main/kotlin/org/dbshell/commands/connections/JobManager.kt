package org.dbshell.commands.connections

import org.dbshell.providers.JobValueProvider
import org.dbshell.results.ResultsHashMap
import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import org.springframework.shell.standard.ShellOption
import java.util.*

@ShellComponent
class JobManager: UIManager {
    @ShellMethod("Get job results")
    fun getJobResults(@ShellOption(valueProvider = JobValueProvider::class)uuid: UUID) {
        val result = ResultsHashMap.resultsMap[uuid]!!
        renderResult(result)
    }
}