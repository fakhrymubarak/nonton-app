package com.fakhry.nonton.home.setting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.fakhry.nonton.R
import com.fakhry.nonton.sign.signin.User
import com.fakhry.nonton.utils.Preferences
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_edit_profile.*

class EditProfileActivity : AppCompatActivity() {

    private lateinit var preferences: Preferences

    lateinit var sUsername: String
    lateinit var sPassword: String
    lateinit var sNama: String
    lateinit var sEmail: String

    private lateinit var mFirebaseDatabase: DatabaseReference
    private lateinit var mFirebaseInstance: FirebaseDatabase
    private lateinit var mDatabase: DatabaseReference



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        mFirebaseInstance = FirebaseDatabase.getInstance()
        mDatabase = FirebaseDatabase.getInstance().getReference()
        mFirebaseDatabase = mFirebaseInstance.getReference("User")

        preferences = Preferences(applicationContext)

        Glide.with(this)
            .load(preferences.getValues("url"))
            .apply(RequestOptions.circleCropTransform())
            .into(iv_profile)

        et_username.setText(preferences.getValues("username"))
        et_password.setText(preferences.getValues("password"))
        et_nama.setText(preferences.getValues("nama"))
        et_email.setText(preferences.getValues("email"))

        btn_save_edit.setOnClickListener{
            sUsername = et_username.text.toString()
            sPassword = et_password.text.toString()
            sNama = et_nama.text.toString()
            sEmail = et_email.text.toString()

            if (sUsername.equals("")) {
                et_username.error = "Silahkan isi Username"
                et_username.requestFocus()
            } else if (sPassword.equals("")) {
                et_password.error = "Silahkan isi Password"
                et_password.requestFocus()
            } else if (sNama.equals("")) {
                et_nama.error = "Silahkan isi Nama"
                et_nama.requestFocus()
            } else if (sEmail.equals("")) {
                et_email.error = "Silahkan isi Email"
                et_email.requestFocus()
            } else {
                saveUserEdit(sUsername, sPassword, sNama, sEmail)
            }
        }
        iv_back.setOnClickListener {
            finish()
        }
    }

    private fun saveUserEdit(sUsername: String, sPassword: String, sNama: String, sEmail: String) {

        val user = User()

        user.username = sUsername
        user.password = sPassword
        user.nama = sNama
        user.email = sEmail
        user.saldo = preferences.getValues("saldo").toString()
        user.url = preferences.getValues("url").toString()


        checkingUsername(sUsername, user)
    }
    private fun checkingUsername(iUsername: String, data: User) {
        mFirebaseDatabase.child(iUsername).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val user = dataSnapshot.getValue(User::class.java)

                val pUsername = preferences.getValues("username").toString()     //PREFERENCES USERNAME



                //kalau username nya sama, maka tidak usah edit username

                //kalau username nya beda, maka edit username

                if (user == null || iUsername == pUsername) {

                    if (iUsername != pUsername){  //kalau usernamenya tidak sama dengan username sebelumnya, berarti mau dia ubah

                        mFirebaseDatabase.child(iUsername).setValue(data)
                        mFirebaseDatabase.child(pUsername).removeValue()
                        preferences.setValues("username", data.username.toString())
                    }

                    mFirebaseDatabase.child(iUsername).setValue(data)

                    preferences.setValues("username", data.username.toString())
                    preferences.setValues("password", data.password.toString())
                    preferences.setValues("nama", data.nama.toString())
                    preferences.setValues("email", data.email.toString())

                    Toast.makeText(this@EditProfileActivity, "Berhasil Edit Profil", Toast.LENGTH_LONG)
                        .show()

                } else {
                    Toast.makeText(this@EditProfileActivity, "User sudah digunakan", Toast.LENGTH_LONG)
                        .show()

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@EditProfileActivity, "Database Error" + error.message, Toast.LENGTH_LONG)
                    .show()
            }
        })
    }

}
