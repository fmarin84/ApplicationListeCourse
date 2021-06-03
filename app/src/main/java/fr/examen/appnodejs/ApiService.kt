package fr.examen.appnodejs

import fr.examen.appnodejs.api.*
import retrofit2.Call
import retrofit2.http.*


interface ApiService {


    @GET(Constants.LIST_ARCHIVE_URL)
    fun fetchListsArchive():  Call<kotlin.collections.List<ListShop>>

    @Headers("Content-Type:application/json")
    @POST(Constants.ITEM_URL)
    fun insertItem(@Body request: ItemRequest ):  Call<Item>

    @DELETE("item/{id}")
    fun deleteItem(@Path("id") id: Int ):  Call<Item>

    @Headers("Content-Type:application/json")
    @PUT(Constants.ITEM_URL)
    fun updateItem(@Body request: ItemRequest ):  Call<Item>

    @GET("item/list/{listId}")
    fun fetchItems(@Path("listId") listId: Int):  Call<kotlin.collections.List<Item>>


    @Headers("Content-Type:application/json")
    @POST(Constants.LIST_URL)
    fun insertList(@Body request: ListShopRequest ):  Call<ListShop>

    @DELETE("list/{id}")
    fun deleteList(@Path("id") id: Int ):  Call<ListShop>

    @Headers("Content-Type:application/json")
    @PUT(Constants.LIST_URL)
    fun updateList(@Body request: ListShopRequest ):  Call<ListShop>

    @GET(Constants.LIST_URL)
    fun fetchLists():  Call<kotlin.collections.List<ListShop>>

    //@FormUrlEncoded
    @Headers("Content-Type:application/json")
    @POST(Constants.LOGIN_URL)
    fun login(@Body request: LoginRequest ): Call<LoginResponse>

}