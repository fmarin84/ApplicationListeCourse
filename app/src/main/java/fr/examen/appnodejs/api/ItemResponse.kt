package fr.examen.appnodejs.api

import android.content.ClipData
import java.io.Serializable

data class ItemResponse(
//    @SerializedName("status")
//    var status: Int,

//    @SerializedName("message")
//    var message: String,

    var items: List<ClipData.Item>
): Serializable