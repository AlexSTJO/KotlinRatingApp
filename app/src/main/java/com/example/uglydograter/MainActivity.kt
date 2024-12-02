package com.example.uglydograter

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.uglydograter.R
import com.example.uglydograter.api.RetrofitClient
import com.example.uglydograter.models.LoginRequest
import com.example.uglydograter.models.LoginResponse
import com.example.uglydograter.models.RegisterRequest
import com.example.uglydograter.models.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val emailInput = findViewById<EditText>(R.id.emailInput)
        val passwordInput = findViewById<EditText>(R.id.passwordInput)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val registerButton = findViewById<Button>(R.id.registerButton)


        loginButton.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                performLogin(email, password)
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }


        registerButton.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                performRegister(email, password)
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun performLogin(email: String, password: String) {
        val loginRequest = LoginRequest(email, password)
        RetrofitClient.apiService.login(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse?.success == 1) {
                        runOnUiThread{
                        Toast.makeText(this@MainActivity, "Succesful Login", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        runOnUiThread{
                            Toast.makeText(this@MainActivity, loginResponse?.errormessage, Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    println("Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                println("API Call Failed: ${t.message}")
            }
        })

    }

    private fun performRegister(email: String, password: String) {
        val registerRequest = RegisterRequest(email, password)
        RetrofitClient.apiService.register(registerRequest).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                if (response.isSuccessful) {
                    val registerResponse = response.body()
                    if (registerResponse?.success == 1) {
                        runOnUiThread{
                            Toast.makeText(this@MainActivity, "Registration Successful!", Toast.LENGTH_SHORT).show()
                        }

                    } else {
                        runOnUiThread {
                            Toast.makeText(this@MainActivity, "Registration Failed: ${registerResponse?.errormessage}", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "Error: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
