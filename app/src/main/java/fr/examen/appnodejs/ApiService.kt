package fr.examen.appnodejs

import fr.examen.appnodejs.api.*
import retrofit2.Call
import retrofit2.http.*


interface ApiService {

    @Headers("Content-Type:application/json")
    @PUT(Constants.ITEM_URL)
    fun updateItem(@Body request: ItemRequestUpdate ):  Call<Item>

    @GET("item/list/{listId}")
    fun fetchItems(@Path("listId") listId: Int):  Call<kotlin.collections.List<Item>>

    @GET(Constants.LIST_URL)
    fun fetchLists():  Call<kotlin.collections.List<ListShop>>

    //@FormUrlEncoded
    @Headers("Content-Type:application/json")
    @POST(Constants.LOGIN_URL)
    fun login(@Body request: LoginRequest ): Call<LoginResponse>

}