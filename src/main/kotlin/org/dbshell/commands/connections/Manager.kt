package org.dbshell.commands.connections

import org.dbshell.actions.Action
import org.dbshell.jobqueue.QueueWrapper

interface Manager {
    fun <T> processAction(action: Action<T>, async: Boolean) {
        if(async) {
            QueueWrapper.put(action)
        } else {
            action.process()
        }
    }
}