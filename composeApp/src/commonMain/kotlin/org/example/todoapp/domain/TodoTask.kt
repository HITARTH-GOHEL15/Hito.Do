package org.example.todoapp.domain

import io.realm.kotlin.types.RealmObject
import org.mongodb.kbson.ObjectId

class TodoTask: RealmObject {
    var _id : ObjectId = ObjectId()
    var title: String = ""
    var description: String = ""
    var favorite: Boolean = false
    var Completed: Boolean = false
}