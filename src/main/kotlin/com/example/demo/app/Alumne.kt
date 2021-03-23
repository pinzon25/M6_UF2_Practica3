package com.example.demo.app

import javafx.beans.property.Property
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.Commit
import tornadofx.ItemViewModel

data class Alumne(var id:Int,var nom:String, var cognoms:String, var edat:Int) {

    val idProperty = SimpleIntegerProperty(id)

    val nomProperty = SimpleStringProperty(nom)

    val cognomsProperty = SimpleStringProperty(cognoms)

    val edatProperty = SimpleIntegerProperty(edat)
}



class AlumneModel(alumne: Alumne?) : ItemViewModel<Alumne>(alumne){
    val id = bind(Alumne::idProperty)
    val nom = bind(Alumne::nomProperty)
    val cognoms = bind(Alumne::cognomsProperty)
    val edat = bind(Alumne::edatProperty)
}




