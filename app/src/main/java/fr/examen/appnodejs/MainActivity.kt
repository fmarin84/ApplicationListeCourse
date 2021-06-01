package fr.examen.appnodejs


import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.examen.appnodejs.api.ListShop
import fr.examen.appnodejs.api.ListShopResponse
import fr.examen.appnodejs.api.LoginResponse
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "http://ec2-23-20-194-1.compute-1.amazonaws.com:3333/"

class MainActivity : AppCompatActivity() {

    private var TAG = "MainActivity"
    lateinit var customAdapter: CustomAdapter
    private lateinit var sessionManager: SessionManager
    private lateinit var apiUser: ApiUser

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        apiUser = ApiUser()
        sessionManager = SessionManager(this)

        initRecyclerView()

        //getting recyclerview from xml
        val recyclerView = findViewById(R.id.recyclerView) as RecyclerView

        //adding a layoutmanager
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        fetchLists()
    }

    private fun initRecyclerView(){
        recyclerView.apply {
            customAdapter = CustomAdapter()
            adapter = customAdapter
        }
    }


    /**
     * Function to fetch posts
     */
    private fun fetchLists() {

        // Pass the token as parameter
        apiUser.getApiService().fetchLists(token = "Bearer ${sessionManager.fetchAuthToken()}")
            .enqueue(object : Callback<List<ListShop>> {

                override fun onFailure(call: Call<List<ListShop>>, t: Throwable) {
//                     Error fetching posts
                    println("onFailure")
                    println(t)
                    println("call")
                    println(call)
                }

                override fun onResponse(
                    call: Call<List<ListShop>>,
                    response: Response<List<ListShop>>
                ) {
                    // Handle function to display posts

                    val ListShopResponse = response.body()!!

                    if (response.code() == 200 ) {
                        customAdapter.list = ListShopResponse.toMutableList()
                        customAdapter.notifyDataSetChanged()
                    } else {
                        // Error logging in
                    }

                }
            })
    }

}