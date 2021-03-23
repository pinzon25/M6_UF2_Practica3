package com.example.demo.app

import com.example.demo.controller.AlumneController
import javafx.collections.FXCollections
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.scene.layout.AnchorPane
import tornadofx.*
import javafx.scene.control.TableView
import javafx.scene.input.KeyCode
import javafx.stage.StageStyle
import java.awt.event.KeyEvent
import javax.swing.table.TableColumnModel
import kotlin.coroutines.experimental.ContinuationInterceptor

class AlumneEditor: View() {
    val botoAfegir: Button by fxid("Bt_Afegir")
    val botoEsborrar:Button by fxid("Bt_Esborrar")
    val campNom: TextField by fxid("Tf_Nom")
    val campCognom:TextField by fxid("Tf_Cognom")
    val campEdat:TextField by fxid("Tf_Edat")

    override val root: AnchorPane by fxml()
    var llistatAlumnes: MutableList<Alumne> = ArrayList()
    var t: javafx.scene.control.TableView<Alumne>? = null
    var model: TableViewEditModel<Alumne> by singleAssign()
    val controller: AlumneController by inject()
    //val model:AlumneModel = AlumneModel(null)
    var ind:Int?=null
    var item:Alumne?=null
    var alu:Alumne?=null
    var itemDirty:Boolean? = null



    init {
        llistatAlumnes = controller.carregaAlumnes()


        var a = FXCollections.observableArrayList(llistatAlumnes.observable())
        with(root) {

            t = tableview(a) {
                //column("Id", Alumne::idProperty)
                column("Nom", Alumne::nomProperty).makeEditable()
                column("Cognoms", Alumne::cognomsProperty).makeEditable()
                column("Edat", Alumne::edatProperty).makeEditable()

                contextmenu {
                    item("Esborrar").action {
                        selectedItem?.apply { esborrar() }
                    }
                    item("Crear alumne").action { crear() }

                    item("Actualitzar").action {
                        if(itemDirty as Boolean){ controller.actualitza(alu!!) }else{alert(Alert.AlertType.ERROR, "", "L'alumne no s'ha modificat!!!")}
                    }
                    item("Revertir").action {
                        selectedItem?.apply { model.rollbackSelected()
                            println("Has revertit el model.")
                        }
                    }
                    item("Afegir alumne").action{
                        a.add(Alumne(0,"Nou Alumne","Nou Alumne",0))
                    }
                }

                prefHeight = 365.0
                prefWidth = 345.0
                layoutX = 250.0
                layoutY = 100.0
                isEditable = true
                enableCellEditing()
                enableDirtyTracking()
                model = editModel
            }


            model.selectedItemDirtyState.onChange {

                var al:Alumne? =null
                al = t?.selectedItem

                item = al!!.copy(id=al.id,nom=al.nom,cognoms=al.cognoms,edat=al.edat) //Copiem les dades del objecte seleccionat a l'item Alumne, aixo ens permetra verificar canvis.
                ind = t?.selectionModel?.selectedIndex  //Ens permet saber l'index corresponent al alumne seleccionat al TableView.
                println("\n\nitem actual: " + item) //Ens mostra l'objecte avans de ser modificat.
                itemDirty = model.isDirty(al!!)
                println("L'objecte amb l'index " + ind +" Is dirty?--->"+itemDirty) //Ens mostra si el model ha detectat canvis a l'objecte seleccionat en aquell moment.

                al.nom = t!!.selectedItem!!.nomProperty.get()
                al.cognoms= t!!.selectedItem!!.cognomsProperty.get()
                al.edat = t!!.selectedItem!!.edatProperty.get()

                println("Alumne modificat: " + al) //Ens mostra l'objecte modificat.

                alu = al.copy(id=al.id,nom=al.nom,cognoms=al.cognoms,edat=al.edat)

                model.commit(alu!!)
            }

        }

    }

    fun crear(){
        var noualumne:Alumne?=null
        noualumne = t!!.selectedItem
        noualumne!!.nom = t!!.selectedItem!!.nomProperty.get()
        noualumne!!.cognoms= t!!.selectedItem!!.cognomsProperty.get()
        noualumne!!.edat = t!!.selectedItem!!.edatProperty.get()

        controller.CrearNouAlumne(noualumne)
    }

    fun esborrar(){
        try {
            item = t!!.selectedItem
            controller.esborraAlumne(item!!.id)
            t!!.items.removeAt(ind!!) //Esborra l'alumne del tableview a la posicio indicada per l'index.
        } catch (error: NullPointerException) {
            alert(Alert.AlertType.ERROR, "", "No has seleccionat cap alumne!!!")
        }
    }
}




