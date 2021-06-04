package fr.examen.appnodejs

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import fr.examen.appnodejs.api.Item
import fr.examen.appnodejs.api.ItemRequest
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

        val list = intent.getSerializableExtra("list") as ListShop


        initRecyclerView(list.archived)

        //adding a layoutmanager
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        fetchItems()
//        if(list.archived){
//            fetchItemsDisable()
//        } else {
//            fetchItems()
//        }

        val btAdd = findViewById(R.id.addItem) as FloatingActionButton

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

//                    itemAdapter.notifyDataSetChanged()
                    finish()
                    startActivity(getIntent());

                    qte.text.clear()
                    label.text.clear()
                    dlg.dismiss()
                }
            }
        }

    }

    private fun initRecyclerView(isArchive: Boolean){
        rcItem.apply {
            itemAdapter = ItemAdapter(context, apiClient, isArchive)
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