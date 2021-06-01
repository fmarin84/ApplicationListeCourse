package fr.examen.appnodejs

import fr.examen.appnodejs.api.ListShop
import fr.examen.appnodejs.api.ListShopResponse
import fr.examen.appnodejs.api.LoginRequest
import fr.examen.appnodejs.api.LoginResponse
import retrofit2.Call
import retrofit2.http.*


interface NodeJsAPI {

//    @GET("List")
//    fun getList(@Header("Authorization") authHeader: String): Call<kotlin.collections.List<List>>

//    @GET(Constants.LIST_URL)
//    fun fetchLists(@Header("Authorization") token: String): Call<kotlin.collections.List<ListShopResponse>>

    @GET(Constants.LIST_URL)
    fun fetchLists(@Header("Authorization") token: String):  Call<kotlin.collections.List<ListShop>>

    //@FormUrlEncoded
    @Headers("Content-Type:application/json")
    @POST(Constants.LOGIN_URL)
    fun login(@Body request: LoginRequest ): Call<LoginResponse>

}