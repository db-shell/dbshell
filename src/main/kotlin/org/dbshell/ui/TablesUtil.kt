package org.dbshell.ui

import org.springframework.shell.table.*
import java.util.*

class TablesUtil {
    companion object {
        fun <T> renderAttributeTable(headers: LinkedHashMap<String, Any>, iter: Iterable<T>) {
            val model = BeanListTableModel(iter, headers)
            renderTable(model)
        }
        fun renderAttributeTable(data: Array<Array<Any>>) {
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