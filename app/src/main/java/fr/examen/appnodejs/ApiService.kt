package fr.examen.appnodejs

import fr.examen.appnodejs.api.*
import retrofit2.Call
import retrofit2.http.*


interface ApiService {


    @GET(Constants.LIST_SHARE_URL)
    fun fetchListsShare():  Call<kotlin.collections.List<ListShop>>

    @DELETE(Constants.LIST_SHARE_DELETE_URL)
    fun deleteListShare(@Path("listId") listId: Int , @Path("userId") userId: Int  ):  Call<ListShop>

    @GET(Constants.USERS_URL)
    fun fetchUsers():  Call<kotlin.collections.List<User>>

    @GET(Constants.CURRENT_USER_URL)
    fun fetchCurrentUser():  Call<User>

    @Headers("Content-Type:application/json")
    @POST(Constants.LIST_SHARE_ADD_URL)
    fun addListShare(@Path("listId") listId: Int , @Path("userId") userId: Int , @Path("state") state: Int  ):  Call<ListShop>

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