package org.dbshell.ui

import org.springframework.shell.table.BeanListTableModel
import org.springframework.shell.table.BorderStyle
import org.springframework.shell.table.TableBuilder
import java.util.*

class TablesUtil {
    companion object {
        fun <T> renderAttributeTable(headers: LinkedHashMap<String, Any>, iter: Iterable<T>) {
            val model = BeanListTableModel(iter, headers)
            val tableBuilder = TableBuilder(model)
            tableBuilder.addInnerBorder(BorderStyle.fancy_light)
            tableBuilder.addHeaderBorder(BorderStyle.fancy_double)
            println(tableBuilder.build().render(80))
        }
    }
}