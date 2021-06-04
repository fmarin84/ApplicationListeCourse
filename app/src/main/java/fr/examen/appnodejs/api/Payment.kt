package fr.examen.appnodejs.api

import java.io.Serializable

data class Payment(var id:Int,
                   var nom:String,
                   var prenom:String,
                   var fk_useraccount_id:Int,
                   var created_at:String
): Serializable

