package fr.examen.appnodejs

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.examen.appnodejs.api.Item
import fr.examen.appnodejs.api.ListShop
import kotlinx.android.synthetic.main.activity_item.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ItemActivity : AppCompatActivity() {

    private var TAG = "ItemActivity"
    lateinit var itemAdapter: ItemAdapter
    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item)

        apiClient = ApiClient()
        sessionManager = SessionManager(this)

        //getting recyclerview from xml
        val recyclerView = findViewById<RecyclerView>(R.id.rcItem)

        initRecyclerView()

        //adding a layoutmanager
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        fetchItems()

    }

    private fun initRecyclerView(){
        rcItem.apply {
            itemAdapter = ItemAdapter()
            adapter = itemAdapter
        }
    }

    /**
     * Function to fetch items
     */
    private fun fetchItems() {

        val list = intent.getSerializableExtra("list") as ListShop

        // Pass the token as parameter
        apiClient.getApiService(this).fetchItems(listId = list.id)
            .enqueue(object : Callback<List<Item>> {

                override fun onFailure(call: Call<List<Item>>, t: Throwable) {
                    println("onFailure")
                    println(t)
                    println("call")
                    println(call)
                }

                override fun onResponse(call: Call<List<Item>>, response: Response<List<Item>>) {

                    val ItemResponse = response.body()!!

                    println("ItemResponse")
                    println(ItemResponse)


                    if (response.code() == 200 ) {

//                        var itemsList: List<String> = emptyList()
//                        for (item in itemsList) {
//                            if(item.fk_id_list === 1){
//                                itemsList.toMutableList().add(item)
//                            }
//                        }

                        itemAdapter.item = ItemResponse.toMutableList()
                        itemAdapter.notifyDataSetChanged()
                    } else {
                        // Error logging in
                    }
                }
            })
    }

}