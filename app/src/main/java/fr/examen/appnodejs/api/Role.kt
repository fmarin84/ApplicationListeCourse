package fr.examen.appnodejs.api

import java.io.Serializable

data class Role(var id:Int,
                var label:String,
                var level:Int
): Serializable
