package fr.examen.appnodejs.api

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LoginResponse(
    @SerializedName("status")
    var status: Int,

    @SerializedName("token")
    var token: String,

//    @SerializedName("user")
//    var user: User

): Serializable
