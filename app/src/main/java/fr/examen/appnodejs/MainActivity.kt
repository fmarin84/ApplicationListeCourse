package fr.examen.appnodejs


import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import fr.examen.appnodejs.api.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.list_share_edit.*
import retrofit2.*
import java.util.*


class MainActivity : AppCompatActivity() {

    private var TAG = "MainActivity"
    private var isAbonne: Boolean = false
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

        reAuth()
        fetchLists()

        // user
        val btAdd = findViewById<FloatingActionButton>(R.id.addList)
        btAdd.setOnClickListener {

            val dlg = Dialog(context)
            dlg.setContentView(R.layout.list_edit)

            val tvItemTitle = dlg.findViewById<TextView>(R.id.tvListTitle)
            tvItemTitle.setText("Ajout d'une liste de course")

            val btAdd = dlg.findViewById<Button>(R.id.btEditAdd)
            btAdd.setText("Ajouter")

//            val etDate = dlg.findViewById<EditText>(R.id.editTextDate)
            val etShop = dlg.findViewById<EditText>(R.id.editShop)


            val etDate = dlg.findViewById<EditText>(R.id.editTextDate)
            etDate.setOnClickListener(View.OnClickListener {
                val cldr = Calendar.getInstance()
                val day = cldr[Calendar.DAY_OF_MONTH]
                val month = cldr[Calendar.MONTH]
                val year = cldr[Calendar.YEAR]
                // date picker dialog
                val picker = DatePickerDialog(
                        context,
                        { view, year, monthOfYear, dayOfMonth -> etDate.setText(dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year) },
                        year,
                        month,
                        day
                )
                picker.show()
            })


            dlg.show()

            dlg.findViewById<Button>(R.id.btEditCancel).setOnClickListener {
                dlg.dismiss()
            }

            dlg.findViewById<Button>(R.id.btEditAdd).setOnClickListener {
                if(!etShop.text.isBlank() && !etDate.text.isBlank()) {

                    val obj = ListShop(0, etShop.text.toString(), etDate.text.toString(), false, 0, 0)

                    apiClient.getApiService(context).insertList(ListShopRequest(list = obj))
                        .enqueue(object : Callback<ListShop> {
                            override fun onFailure(call: Call<ListShop>, t: Throwable) {
                            }

                            override fun onResponse(call: Call<ListShop>, response: Response<ListShop>) {
                            }

                        })

                    val toast = Toast.makeText(context, "La liste a bien été ajoutée.", Toast.LENGTH_LONG)
                    toast.show()
                    etDate.text.clear()
                    etShop.text.clear()
                    dlg.dismiss()
                    fetchLists()

                } else {
                    val toast = Toast.makeText(context, "Tous les champs sont obligatoires.", Toast.LENGTH_LONG)
                    toast.show()
                }
                fetchLists()
            }
            fetchLists()
        }
    }

    private fun initRecyclerView(){
        recyclerView.apply {
            customAdapter = CustomAdapter(context, apiClient)
            adapter = customAdapter
        }
    }



    /**
     * Function to fetch users
     */
    private fun fetchUsers() {
        // Pass the token as parameter
        apiClient.getApiService(this).fetchUsers()
            .enqueue(object : Callback<List<User>> {

                override fun onFailure(call: Call<List<User>>, t: Throwable) {
                    TODO("Not yet implemented")
                }

                override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {

                    val UsersResponse = response.body()!!

                    if (response.code() == 200) {
                        val adapter: ArrayAdapter<User> = ArrayAdapter(
                                this@MainActivity,
                                android.R.layout.simple_spinner_item,
                                UsersResponse.toMutableList()
                        )
                        spUser.adapter = adapter
                    }
                }
            })
    }

    private fun reAuth(){

        apiClient.getApiService(this).fetchCurrentUser()
            .enqueue(object : Callback<User> {

                override fun onFailure(call: Call<User>, t: Throwable) {
                }

                override fun onResponse(call: Call<User>, response: Response<User>) {
                    val UsersResponse = response.body()!!

                    apiClient.getApiService(this@MainActivity).reauth(LoginRequest(login = UsersResponse.login, password = UsersResponse.challenge))
                            .enqueue(object : Callback<LoginResponse> {
                                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                                    // Error logging in
                                }

                                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                                    val loginResponse = response.body()

                                    if (response.code() == 200 && loginResponse != null) {
                                        sessionManager.saveAuthToken(loginResponse.token)
                                    } else {
                                        // Error logging in
                                        val toast = Toast.makeText(applicationContext, " Identifiants invalides !", Toast.LENGTH_LONG)
                                        toast.show()
                                    }
                                }
                            })
                }
            })
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

                    if (response.code() == 200) {

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