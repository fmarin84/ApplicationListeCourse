package fr.examen.appnodejs.api

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ItemRequest(

    @SerializedName("item")
    var item: Item

):Serializable
