package fr.examen.appnodejs.api

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PaymentRequest(

    @SerializedName("payment")
    var payment: Payment

): Serializable
