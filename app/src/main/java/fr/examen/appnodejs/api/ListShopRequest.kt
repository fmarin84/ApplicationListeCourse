package fr.examen.appnodejs.api

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ListShopRequest(

    @SerializedName("list")
    var list: ListShop

): Serializable