package fr.examen.appnodejs


import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.examen.appnodejs.api.Item
import fr.examen.appnodejs.api.ItemRequestUpdate
import fr.examen.appnodejs.api.ListShop
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.*

class MainActivity : AppCompatActivity() {

    private var TAG = "MainActivity"
    lateinit var customAdapter: CustomAdapter
    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var context = this
        apiClient = ApiClient()
        sessionManager = SessionManager(this)

        initRecyclerView()

        //getting recyclerview from xml
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        //adding a layoutmanager
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        fetchLists()

    }

    private fun initRecyclerView(){
        recyclerView.apply {
            customAdapter = CustomAdapter(context)
            adapter = customAdapter
        }
    }


    /**
     * Function to fetch lists
     */
    private fun fetchLists() {

        // Pass the token as parameter
        apiClient.getApiService(this).fetchLists()
            .enqueue(object : Callback<List<ListShop>> {

                override fun onFailure(call: Call<List<ListShop>>, t: Throwable) {
//                     Error fetching posts
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