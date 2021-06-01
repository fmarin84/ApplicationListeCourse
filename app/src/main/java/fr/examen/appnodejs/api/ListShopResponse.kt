package fr.examen.appnodejs.api

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ListShopResponse(
//    @SerializedName("status")
//    var status: Int,

//    @SerializedName("message")
//    var message: String,

    var lists: List<ListShop>
):Serializable
