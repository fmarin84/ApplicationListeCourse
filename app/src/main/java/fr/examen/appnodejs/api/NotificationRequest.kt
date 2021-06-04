package fr.examen.appnodejs.api

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class NotificationRequest(

    @SerializedName("notification")
    var notification: Notification

): Serializable
