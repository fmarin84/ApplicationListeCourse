package fr.examen.appnodejs.api

import java.io.Serializable

data class Item(var id:Int,
                var label:String,
                var quantity:Int,
                var checked:Boolean,
                var fk_id_list:Int
): Serializable
