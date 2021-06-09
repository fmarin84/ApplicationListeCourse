package fr.examen.appnodejs

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import fr.examen.appnodejs.api.LoginRequest
import fr.examen.appnodejs.api.LoginResponse
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        apiClient = ApiClient()
        sessionManager = SessionManager(this)

        btConnect.setOnClickListener {

            val login = etLogin.text.toString()
            val password = etChallenge.text.toString()

            val intent = Intent(this, MainActivity::class.java)

            if( login.isBlank() || password.isBlank() ) {

                val toast = Toast.makeText(applicationContext, "Tous les champs sont obligatoires.", Toast.LENGTH_LONG)
                toast.show()
            } else {

//                apiClient.getApiService(this).login(LoginRequest(login = "user2@example.com", password = "azerty"))
                apiClient.getApiService(this).login(LoginRequest(login = login, password = password))
                    .enqueue(object : Callback<LoginResponse> {
                        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                            // Error logging in
                        }

                        override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                            val loginResponse = response.body()

                            if (response.code() == 200 && loginResponse != null) {
                                sessionManager.saveAuthToken(loginResponse.token)
                                startActivity(intent)
                            } else {
                                // Error logging in
                                val toast = Toast.makeText(applicationContext, " Identifiants invalides !", Toast.LENGTH_LONG)
                                toast.show()
                            }
                        }
                    })

            }

        }

    }
}