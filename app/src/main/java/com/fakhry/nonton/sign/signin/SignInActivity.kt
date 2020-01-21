package com.fakhry.nonton.sign.signin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.fakhry.nonton.home.HomeActivity
import com.fakhry.nonton.R
import com.fakhry.nonton.sign.signup.SignUpActivity
import com.fakhry.nonton.utils.Preferences
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity() {

    lateinit var iUsername: String
    lateinit var iPassword: String

    lateinit var mDatabase: DatabaseReference
    lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        mDatabase = FirebaseDatabase.getInstance().getReference("User")
        preferences = Preferences(this)

        preferences.setValues("onboarding", "1")

        //START - PENGECEKAN USERNAME DAN PASSWORD KETIKA TOMBOL LOGIN DI TEKAN
        btn_login.setOnClickListener {
            iUsername = et_username.text.toString()
            iPassword = et_password.text.toString()

            if (iUsername.equals("")) {
                et_username.error = "Username tidak boleh kosong"
                et_username.requestFocus()
            } else if (iPassword.equals("")) {
                et_password.error = "Password tidak boleh kosong"
                et_password.requestFocus()
            } else {
                pushLogin(iUsername, iPassword) //METHOD UNTUK LOGIN
            }
        }
        //END - PENGECEKAN USERNAME DAN PASSWORD KETIKA TOMBOL LOGIN DI TEKAN

        btn_daftar.setOnClickListener {
            val intent = Intent(
                this@SignInActivity,
                SignUpActivity::class.java
            )
            startActivity(intent)
        }

        if (preferences.getValues("status").equals("1")) {
            finishAffinity()
            val intent = Intent(
                this@SignInActivity,
                HomeActivity::class.java
            )
            startActivity(intent)
        }
    }

    //START - METHOD LOGIN, MENGAMBIL DATA USER
    private fun pushLogin(iUsername: String, iPassword: String) {
        mDatabase.child(iUsername).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val user = dataSnapshot.getValue(User::class.java)

                if (user == null) {
                    Toast.makeText(this@SignInActivity, "Username tidak ditemukan", Toast.LENGTH_LONG)
                        .show()

                } else {
                    if (user.password.equals(iPassword)) {

                        preferences.setValues("nama", user.nama.toString())
                        preferences.setValues("username", user.username.toString())
                        preferences.setValues("password", user.password.toString())
                        preferences.setValues("url", user.url.toString())
                        preferences.setValues("email", user.email.toString())
                        preferences.setValues("saldo", user.saldo.toString())

                        preferences.setValues("status", "1") //SET STATUS PREFERENCES


                        val nama = preferences.getValues("nama")
                        Toast.makeText(this@SignInActivity, "Selamat Datang '$nama'", Toast.LENGTH_LONG)
                            .show()

                        finishAffinity()

                        val intent = Intent(
                            this@SignInActivity,
                            HomeActivity::class.java
                        )
                        startActivity(intent)

                    } else {
                        Toast.makeText(
                            this@SignInActivity,
                            "Password Anda Salah",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@SignInActivity, "" + error.message, Toast.LENGTH_LONG).show()
            }
        })
    }
    //END - METHOD LOGIN, MENGAMBIL DATA USER

}