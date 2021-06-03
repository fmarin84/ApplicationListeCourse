package fr.examen.appnodejs


import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import fr.examen.appnodejs.api.Item
import fr.examen.appnodejs.api.ListShop
import fr.examen.appnodejs.api.ListShopRequest
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

        // user

        val btAdd = findViewById(R.id.addList) as FloatingActionButton
        btAdd.setOnClickListener {

            val dlg = Dialog(context)
            dlg.setContentView(R.layout.list_edit)


            val tvItemTitle = dlg.findViewById<TextView>(R.id.tvListTitle)
            tvItemTitle.setText( "Ajout d'une liste de course")

            val btAdd = dlg.findViewById<Button>(R.id.btEditAdd)
            btAdd.setText( "Ajouter")

            val etDate = dlg.findViewById<EditText>(R.id.editTextDate)
            val etShop = dlg.findViewById<EditText>(R.id.editShop)

            dlg.show()

            dlg.findViewById<Button>(R.id.btEditCancel).setOnClickListener {
                dlg.dismiss()
            }

            dlg.findViewById<Button>(R.id.btEditAdd).setOnClickListener {
                if(!etShop.text.isBlank() && !etDate.text.isBlank()) {

                    val obj = ListShop(0,  etShop.text.toString(), etDate.text.toString(), false, 0)

                    apiClient.getApiService(context).insertList(ListShopRequest(list = obj))
                        .enqueue(object : Callback<ListShop> {
                            override fun onFailure(call: Call<ListShop>, t: Throwable) {
                            }

                            override fun onResponse(
                                call: Call<ListShop>,
                                response: Response<ListShop>
                            ) {
                            }

                        })

//                    customAdapter.notifyDataSetChanged()
                    finish()
                    startActivity(getIntent());

                    etDate.text.clear()
                    etShop.text.clear()
                    dlg.dismiss()
                }
            }
        }

    }

    private fun initRecyclerView(){
        recyclerView.apply {
            customAdapter = CustomAdapter(context, apiClient)
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