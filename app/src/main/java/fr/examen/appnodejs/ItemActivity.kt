package fr.examen.appnodejs

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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
            itemAdapter = ItemAdapter(context, apiClient)
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
                }

                override fun onResponse(call: Call<List<Item>>, response: Response<List<Item>>) {

                    val ItemResponse = response.body()!!

                    if (response.code() == 200 ) {
                        itemAdapter.item = ItemResponse.toMutableList()
                        itemAdapter.notifyDataSetChanged()
                    } else {
                        // Error logging in
                    }
                }
            })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == R.id.menu_deco){
            var intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        if(item.itemId == R.id.menu_abonnement){
//            var intent = Intent(this, Historique::class.java)
//            startActivity(intent)
        }

        if(item.itemId == R.id.menu_historique){
//            var intent = Intent(this, Historique::class.java)
//            startActivity(intent)
        }

        if(item.itemId == R.id.menu_share){
//            var intent = Intent(this, Historique::class.java)
//            startActivity(intent)
        }

        if(item.itemId == R.id.menu_accueil){
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.listofcourse_menu, menu)
        return true
    }

}