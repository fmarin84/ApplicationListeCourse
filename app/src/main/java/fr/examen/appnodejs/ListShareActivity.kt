package fr.examen.appnodejs

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.examen.appnodejs.api.ListShop
import kotlinx.android.synthetic.main.activity_list_share.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListShareActivity : AppCompatActivity() {

    private var TAG = "ListShareActivity"
    lateinit var listShareAdapter: ListShareAdapter
    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient


    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_share)

        var context = this
        apiClient = ApiClient()
        sessionManager = SessionManager(this)

        initRecyclerView()

        //getting recyclerview from xml
        val recyclerView = findViewById<RecyclerView>(R.id.rcListShare)

        //adding a layoutmanager
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        fetchListsShare()
    }

    private fun initRecyclerView(){
        rcListShare.apply {
            listShareAdapter = ListShareAdapter(context, apiClient)
            adapter = listShareAdapter
        }
    }

    /**
     * Function to fetch lists
     */
    private fun fetchListsShare() {

        // Pass the token as parameter
        apiClient.getApiService(this).fetchListsShare()
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
                            listShareAdapter.list = ListShopResponse.toMutableList()
                            listShareAdapter.notifyDataSetChanged()
                        } else {
                            // Error logging in
                        }

                    }
                })
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == R.id.menu_deco){
            sessionManager = SessionManager(this)
            sessionManager.saveAuthToken("")
            var intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        if(item.itemId == R.id.menu_notif){
            var intent = Intent(this, NotificationsActivity::class.java)
            startActivity(intent)
        }

        if(item.itemId == R.id.menu_abonnement){
            var intent = Intent(this, SubscribActivity::class.java)
            startActivity(intent)
        }

        if(item.itemId == R.id.menu_historique){
            var intent = Intent(this, ListArchiveActivity::class.java)
            startActivity(intent)
        }

        if(item.itemId == R.id.menu_share){
            var intent = Intent(this, ListShareActivity::class.java)
            startActivity(intent)
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