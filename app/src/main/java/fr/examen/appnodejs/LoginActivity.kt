package fr.examen.appnodejs

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import fr.examen.appnodejs.api.LoginRequest
import fr.examen.appnodejs.api.LoginResponse
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var apiUser: ApiUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        apiUser = ApiUser()
        sessionManager = SessionManager(this)

        btConnect.setOnClickListener {

            val login = etLogin.text.toString()
            val password = etChallenge.text.toString()

            val intent = Intent(this, MainActivity::class.java)

            if( (login !== "" ) && (password !== "" )) {
                apiUser.getApiService().login(LoginRequest(login = login, password = password))
                    .enqueue(object : Callback<LoginResponse> {
                        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                            // Error logging in


                            // Toast Mauvais identifients !
                        }

                        override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                            val loginResponse = response.body()

                            if (response.code() == 200 && loginResponse != null) {
                                sessionManager.saveAuthToken(loginResponse.token)
                                startActivity(intent)
                            } else {
                                // Error logging in
                            }
                        }
                    })


            } else {
                // Toast les champs sont obligatoirs
            }

        }

    }
}