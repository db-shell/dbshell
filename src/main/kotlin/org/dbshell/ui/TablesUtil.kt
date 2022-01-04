package org.dbshell.ui

import org.springframework.shell.table.*

class TablesUtil {
    companion object {
        fun renderAttributeTable(data: Array<Array<Any?>>) {
            val model = ArrayTableModel(data)
            renderTable(model)
        }
        private fun renderTable(model: TableModel) {
            val tableBuilder = TableBuilder(model)
            tableBuilder.addInnerBorder(BorderStyle.fancy_light)
            tableBuilder.addHeaderBorder(BorderStyle.fancy_double)
            println(tableBuilder.build().render(80))
        }
    }
}