package com.toDoList.todolist_v20.dataBase.dbContent

import android.provider.BaseColumns

object DbContentTable {

    const val TABLE_NAME = "content_table"
    const val COLUMN_TITLE = "title"
    const val COLUMN_SUBTITLE = "subtitle"
    const val COLUMN_TAGS = "tag"
    const val COLUMN_IMAGE_URI = "image_uri"
    const val COLUMN_ACCOUNTS = "user"
    const val ID_NOTIFICATION = "id_notification"
    const val DATA_NOTIFICATION = "data_notification"
    const val TIME_NOTIFICATION = "time_notification"

    const val DATABASE_VERSION = 1
    const val DATABASE_NAME = "DataBaseContent"

    const val CREATE_TABLE = "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "$COLUMN_TITLE TEXT," +
            "$COLUMN_SUBTITLE TEXT," +
            "$COLUMN_TAGS TEXT," +
            "$COLUMN_IMAGE_URI TEXT," +
            "$COLUMN_ACCOUNTS TEXT," +
            "$DATA_NOTIFICATION TEXT," +
            "$TIME_NOTIFICATION TEXT," +
            "$ID_NOTIFICATION INTEGER)"

    const val DELETE_TABLE = "DROP TABLE IF NOT EXISTS $TABLE_NAME"

}