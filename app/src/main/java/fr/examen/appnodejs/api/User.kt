package fr.examen.appnodejs.api

import java.io.Serializable

data class User(var id:Int,
                var displayname:String,
                var login:String,
                var challenge:String,
                var isactived:Boolean
): Serializable
