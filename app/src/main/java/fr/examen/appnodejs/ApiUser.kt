package fr.examen.appnodejs

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiUser {
    private lateinit var apiService: NodeJsAPI

    fun getApiService(): NodeJsAPI {

        // Initialize ApiService if not initialized yet
        if (!::apiService.isInitialized) {
            val retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            apiService = retrofit.create(NodeJsAPI::class.java)
        }

        return apiService
    }


}