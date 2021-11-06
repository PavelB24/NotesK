package ru.barinov.notes.domain

class Router {
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