package com.team67x.kotlin_crud

class biodata (var ids : Long, var names : String){
    var id : Long = 0
    var name : String = ""

        get() = field
        set(value)
        {
            id = ids
            name = names
        }
}