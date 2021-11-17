package ru.barinov.notes.domain

class Router {
    var isLogged = false
    private var id: String? = null

    fun setId(id: String){
        this.id= id
    }

    fun getId(): String?{
        return id
    }

    fun resetId(){
        id= null
    }
}