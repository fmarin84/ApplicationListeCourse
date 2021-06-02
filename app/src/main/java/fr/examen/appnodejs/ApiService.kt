package fr.examen.appnodejs

import fr.examen.appnodejs.api.*
import retrofit2.Call
import retrofit2.http.*


interface ApiService {

//    @GET("List")
//    fun getList(@Header("Authorization") authHeader: String): Call<kotlin.collections.List<List>>

//    @GET(Constants.LIST_URL)
//    fun fetchLists(@Header("Authorization") token: String): Call<kotlin.collections.List<ListShopResponse>>

    @GET("item/list/{listId}")
    fun fetchItems(@Path("listId") listId: Int):  Call<kotlin.collections.List<Item>>

    @GET(Constants.LIST_URL)
    fun fetchLists():  Call<kotlin.collections.List<ListShop>>
//@Header("Authorization") token: String

    //@FormUrlEncoded
    @Headers("Content-Type:application/json")
    @POST(Constants.LOGIN_URL)
    fun login(@Body request: LoginRequest ): Call<LoginResponse>




}