package org.dbshell.actions

interface Action<T> {
    fun execute(): T
    fun render(data: T)
    fun process() {
        val data = execute()
        render(data)
    }
}