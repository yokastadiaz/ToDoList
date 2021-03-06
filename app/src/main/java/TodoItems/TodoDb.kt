package com.example.notas.TodoItems

import TodoItems.TodoItemData
import android.content.Context
import helpers.DataDbHelper


class TodoDb {
    abstract class TodoTable {
        companion object {
            val TABLE_NAME = "todos"
            val ID = "id"
            val TITLE = "title"
            val MESSAGE = "message"
            val DATE = "date"
            val IMAGE_LINK = "imageLink"
        }
    }
    class Controller(context: Context) {
        private val database:DataDbHelper

        init {
            database = DataDbHelper(context)
        }

        fun insert(item:TodoItemData) {
            database.values.put(TodoTable.TITLE, item.title)
            database.values.put(TodoTable.MESSAGE, item.message)
            database.values.put(TodoTable.DATE, item.date)
            database.values.put(TodoTable.IMAGE_LINK, item.imageUri)
            database.db.insert(
                TodoTable.TABLE_NAME,
                null,
                database.values
            )
        }        //actualizar
        fun update(item: TodoItemData){
            val selection = "${TodoTable.ID} LIKE ?"
             val selectParams = arrayOf(item.id.toString())
            database.values.put(TodoTable.TITLE, item.title)
            database.values.put(TodoTable.MESSAGE, item.message)
            database.values.put(TodoTable.DATE, item.date)
            database.values.put(TodoTable.IMAGE_LINK, item.imageUri)
            database.db.update(
                //table, values, whereClause, whereArgs
                TodoTable.TABLE_NAME,
                database.values,
                selection,
                selectParams
            )
        }
        //eliminar
        fun delete(item: TodoItemData){
            val selection = "${TodoTable.ID} LIKE ?"
            val selectParams = arrayOf(item.id.toString())
            val deleteRows = database.db.delete(TodoTable.TABLE_NAME, selection, selectParams)
        }

        fun getTodos():MutableList<TodoItemData>{
            var todoItems:MutableList<TodoItemData> = ArrayList()
            val cols = arrayOf(
                TodoTable.ID,
                TodoTable.TITLE,
                TodoTable.MESSAGE,
                TodoTable.DATE,
                TodoTable.IMAGE_LINK
            )
            val query = database.db.query(TodoTable.TABLE_NAME, cols, null, null, null, null, null)
            if(query.moveToFirst()){
                do {
                    todoItems.add(
                        TodoItemData(
                            query.getInt(0),
                            query.getString(1),
                            query.getString(2),
                            query.getString(3),
                            query.getString(4)
                        )
                    )
                }while(query.moveToNext())
            }
            return todoItems
        }
    }
}