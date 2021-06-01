package fr.examen.appnodejs.api

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LoginRequest(
    @SerializedName("login")
    var login: String,

    @SerializedName("password")
    var password: String
):Serializable
