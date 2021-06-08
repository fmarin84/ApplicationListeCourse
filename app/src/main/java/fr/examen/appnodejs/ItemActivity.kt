package fr.examen.appnodejs

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import fr.examen.appnodejs.api.*
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

        val list = intent.getSerializableExtra("list") as ListShop

        val btAdd = findViewById<FloatingActionButton>(R.id.addItem)

        if(list.archived){
            btAdd.visibility = View.GONE
        }

        apiClient.getApiService(this).fetchCurrentUser()
                .enqueue(object : Callback<User> {

                    override fun onFailure(call: Call<User>, t: Throwable) {
                    }

                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        val UsersResponse = response.body()!!
                        println("UsersResponse.id")
                        println(UsersResponse.id)


                        if(UsersResponse.id == list.useraccount_id){
                            fetchItems()
                            initRecyclerView(list.archived, false)
                        } else {
                            fetchShareItems()

                            if(list.state == 0) {
                                btAdd.visibility = View.GONE
                                initRecyclerView(list.archived, true)
                            } else {
                                btAdd.visibility = View.VISIBLE
                                initRecyclerView(list.archived, false)
                            }
                        }

                    }
                })


        //adding a layoutmanager
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)


        btAdd.setOnClickListener {

            val dlg = Dialog(this)
            dlg.setContentView(R.layout.item_edit)

            val tvItemTitle = dlg.findViewById<TextView>(R.id.tvItemTitle)
            tvItemTitle.setText( "Ajout d'un acrticle")

            val btAdd = dlg.findViewById<Button>(R.id.btEditAdd)
            btAdd.setText( "Ajouter")

            val qte = dlg.findViewById<EditText>(R.id.editQte)
            val label = dlg.findViewById<EditText>(R.id.editLabel)

            dlg.show()

            dlg.findViewById<Button>(R.id.btEditCancel).setOnClickListener {
                dlg.dismiss()
            }

            dlg.findViewById<Button>(R.id.btEditAdd).setOnClickListener {
                if(!label.text.isBlank() && !qte.text.isBlank()) {

                    val obj = Item(0,  label.text.toString(), qte.text.toString().toInt(), false, list.id )

                    apiClient.getApiService(this).insertItem(ItemRequest(item = obj))
                        .enqueue(object : Callback<Item> {

                            override fun onFailure(call: Call<Item>, t: Throwable) {
                            }

                            override fun onResponse(call: Call<Item>, response: Response<Item>) {
                            }

                        })

                    qte.text.clear()
                    label.text.clear()
                    dlg.dismiss()
                    fetchShareItems()
                }
            }
            fetchShareItems()
        }

    }

    private fun initRecyclerView(isArchive: Boolean, isShareEdit: Boolean){
        rcItem.apply {
            itemAdapter = ItemAdapter(context, apiClient, isArchive, isShareEdit)
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

    /**
     * Function to fetch items
     */
    private fun fetchShareItems() {

        val list = intent.getSerializableExtra("list") as ListShop

        // Pass the token as parameter
        apiClient.getApiService(this).fetchShareItems(listId = list.id)
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