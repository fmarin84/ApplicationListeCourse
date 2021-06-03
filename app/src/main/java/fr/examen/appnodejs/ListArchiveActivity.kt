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
import kotlinx.android.synthetic.main.activity_list_archive.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListArchiveActivity : AppCompatActivity() {

    private var TAG = "ListArchiveActivity"
    lateinit var listArchiveAdapter: ListArchiveAdapter
    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_archive)

        var context = this
        apiClient = ApiClient()
        sessionManager = SessionManager(this)

        initRecyclerView()

        //getting recyclerview from xml
        val recyclerView = findViewById<RecyclerView>(R.id.rcListArchive)

        //adding a layoutmanager
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        fetchListsArchive()
    }

    private fun initRecyclerView(){
        rcListArchive.apply {
            listArchiveAdapter = ListArchiveAdapter(context, apiClient)
            adapter = listArchiveAdapter
        }
    }

    /**
     * Function to fetch lists
     */
    private fun fetchListsArchive() {

        // Pass the token as parameter
        apiClient.getApiService(this).fetchListsArchive()
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
                        listArchiveAdapter.list = ListShopResponse.toMutableList()
                        listArchiveAdapter.notifyDataSetChanged()
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

        if(item.itemId == R.id.menu_abonnement){
//            var intent = Intent(this, Historique::class.java)
//            startActivity(intent)
        }

        if(item.itemId == R.id.menu_historique){
            var intent = Intent(this, ListArchiveActivity::class.java)
            startActivity(intent)
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
