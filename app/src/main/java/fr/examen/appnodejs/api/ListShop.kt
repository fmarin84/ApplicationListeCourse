package fr.examen.appnodejs.api

import java.io.Serializable

data class ListShop(var id:Int,
           var shop:String,
           var date:String,
           var archived:Boolean,
           var useraccount_id:Int,
           var state:Int
           ):Serializable