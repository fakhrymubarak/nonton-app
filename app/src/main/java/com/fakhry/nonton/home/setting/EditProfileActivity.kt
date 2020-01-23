package com.fakhry.nonton.home.setting

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.fakhry.nonton.R
import com.fakhry.nonton.home.HomeActivity
import com.fakhry.nonton.sign.signin.User
import com.fakhry.nonton.utils.Preferences
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.popup_alert.*
import kotlinx.android.synthetic.main.popup_alert.view.*
import java.util.*

class EditProfileActivity : AppCompatActivity(), PermissionListener {

    var statusAdd: Boolean = true
    lateinit var filePath: Uri
    private lateinit var storage: FirebaseStorage
    private lateinit var storageReference: StorageReference

    private lateinit var preferences: Preferences

    lateinit var sUsername: String
    lateinit var sPassword: String
    lateinit var sNama: String
    lateinit var sEmail: String
    lateinit var sUrl: String

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

        storage = FirebaseStorage.getInstance()
        storageReference = storage.getReference()

        var profilePicture = preferences.getValues("url").toString()

        if (profilePicture == "") {
            statusAdd = false
            iv_add.setImageResource(R.drawable.ic_btn_upload)
            iv_profile.setImageResource(R.drawable.user_pic)

        } else {
            statusAdd = true
            Glide.with(this)
                .load(profilePicture)
                .apply(RequestOptions.circleCropTransform())
                .into(iv_profile)
        }

        //START - KONDISI KETIKA BUTTON ADD FOTO DI TEKAN
        iv_add.setOnClickListener {
            if (statusAdd) {
                statusAdd = false
                iv_add.setImageResource(R.drawable.ic_btn_upload)
                iv_profile.setImageResource(R.drawable.user_pic)
            } else {
                ImagePicker.with(this)
                    .cameraOnly()
                    .crop()                    //Crop image(Optional), Check Customization for more option
                    .compress(1024)            //Final image size will be less than 1 MB(Optional)
                    .maxResultSize(
                        1080,
                        1080
                    )    //Final image resolution will be less than 1080 x 1080(Optional)
                    .start()
            }
            btn_save_edit_profile_pict.visibility = View.VISIBLE
            btn_save_edit.visibility = View.INVISIBLE
        }
        //END - KONDISI KETIKA BUTTON ADD FOTO DI TEKAN

        et_username.setText(preferences.getValues("username"))
        et_password.setText(preferences.getValues("password"))
        et_nama.setText(preferences.getValues("nama"))
        et_email.setText(preferences.getValues("email"))

        btn_save_edit_profile_pict.setOnClickListener{
            //START - PROFILE PICTURE EDIT
            if (!statusAdd) {
                preferences.setValues("url", "")
                mFirebaseDatabase.child(preferences.getValues("username").toString()).child("url").setValue("")
                btn_save_edit_profile_pict.visibility = View.INVISIBLE
                btn_save_edit.visibility = View.VISIBLE

            }else {
                val progressDialog = ProgressDialog(this)
                progressDialog.setTitle("Uploading...")
                progressDialog.show()

                val ref = storageReference.child("images/" + UUID.randomUUID().toString())
                ref.putFile(filePath)
                    .addOnSuccessListener {
                        progressDialog.dismiss()
                        ref.downloadUrl.addOnSuccessListener {
                            Log.v("berani", "pUrl 2 = " + it.toString())

                            preferences.setValues("url", it.toString())
                            mFirebaseDatabase.child(preferences.getValues("username").toString()).child("url").setValue(it.toString())

                            Toast.makeText(
                                this@EditProfileActivity,
                                "Berhasil menyimpan foto profil baru",
                                Toast.LENGTH_LONG
                            ).show()
                            btn_save_edit.visibility = View.VISIBLE
                            btn_save_edit_profile_pict.visibility = View.INVISIBLE

                        }
                    }
                    .addOnFailureListener { e ->
                        progressDialog.dismiss()
                        Toast.makeText(
                            this@EditProfileActivity,
                            "Failed " + e.message,
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                    .addOnProgressListener { taskSnapshot ->
                        val progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot
                            .totalByteCount
                        progressDialog.setMessage("Uploaded " + progress.toInt() + "%")
                    }
            }
            //END - PROFILE PICTURE EDIT
        }

        btn_save_edit.setOnClickListener {
            //START - PROFILE DATA EDIT
            sUsername = et_username.text.toString()
            sPassword = et_password.text.toString()
            sNama = et_nama.text.toString()
            sEmail = et_email.text.toString()
            sUrl = preferences.getValues("url").toString()

            Log.v("berani", "pUrl 4 = " + preferences.getValues("url").toString())

            if (sUsername == preferences.getValues("username")&&
                sPassword == preferences.getValues("password")&&
                sNama == preferences.getValues("nama")&&
                sEmail == preferences.getValues("email")){

                Toast.makeText(
                    this@EditProfileActivity,
                    "Tidak ada data yang diubah",
                    Toast.LENGTH_LONG
                ).show()

                finishAffinity()
                val intent = Intent(
                    this@EditProfileActivity,
                    HomeActivity::class.java
                )
                startActivity(intent)
            }else if(sUsername.equals("")) {
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
                saveUserEdit(sUsername, sPassword, sNama, sEmail, sUrl)
            }

            //END - PROFILE DATA EDIT

        }
        iv_back.setOnClickListener {
            alertDialog()
        }
    }

    private fun saveUserEdit(sUsername: String, sPassword: String, sNama: String, sEmail: String, sUrl : String) {

        val user = User()

        user.username = sUsername
        user.password = sPassword
        user.nama = sNama
        user.email = sEmail
        user.url = sUrl
        user.saldo = preferences.getValues("saldo").toString()

        checkingUsername(sUsername, user)
    }

    private fun checkingUsername(iUsername: String, data: User) {
        mFirebaseDatabase.child(iUsername).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val user = dataSnapshot.getValue(User::class.java)

                val pUsername = preferences.getValues("username").toString() //PREFERENCES USERNAME

                if (user == null || iUsername == pUsername) {
                    if (iUsername != pUsername) {  //kalau usernamenya tidak sama dengan username sebelumnya, berarti mau dia ubah
                        mFirebaseDatabase.child(iUsername).setValue(data)
                        mFirebaseDatabase.child(pUsername).removeValue()
                        preferences.setValues("username", data.username.toString())
                    }

                    mFirebaseDatabase.child(iUsername).setValue(data)

                    preferences.setValues("username", data.username.toString())
                    preferences.setValues("password", data.password.toString())
                    preferences.setValues("nama", data.nama.toString())
                    preferences.setValues("email", data.email.toString())
                    preferences.setValues("url", data.url.toString())

                    Toast.makeText(
                        this@EditProfileActivity,
                        "Berhasil Edit Profil",
                        Toast.LENGTH_LONG
                    ).show()

                    finishAffinity()
                    val intent = Intent(
                        this@EditProfileActivity,
                        HomeActivity::class.java
                    )
                    startActivity(intent)


                } else {
                    Toast.makeText(
                        this@EditProfileActivity,
                        "User sudah digunakan",
                        Toast.LENGTH_LONG
                    )
                        .show()

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@EditProfileActivity,
                    "Database Error" + error.message,
                    Toast.LENGTH_LONG
                )
                    .show()
            }
        })
    }

    private fun alertDialog() {
        //Inflate the dialog with custom view
        val mDialogView = LayoutInflater.from(this@EditProfileActivity).inflate(R.layout.popup_alert, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(this@EditProfileActivity)
            .setView(mDialogView)
        //show dialog
        val mAlertDialog = mBuilder.show()

        mAlertDialog.tv_alert_title.text = "Batal?"
        mAlertDialog.tv_alert_msg.text = "Edit profilmu tidak akan tersimpan."

        mDialogView.btn_yes.setOnClickListener {
            //dismiss dialog
            finish()
            mAlertDialog.dismiss()
        }
        mDialogView.btn_no.setOnClickListener {
            mAlertDialog.dismiss()
        }
    }

    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
        ImagePicker.with(this)
            .cameraOnly()
            .crop()                    //Crop image(Optional), Check Customization for more option
            .compress(1024)            //Final image size will be less than 1 MB(Optional)
            .maxResultSize(
                1080,
                1080
            )    //Final image resolution will be less than 1080 x 1080(Optional)
            .start()
    }

    override fun onPermissionRationaleShouldBeShown(
        permission: com.karumi.dexter.listener.PermissionRequest?,
        token: PermissionToken?
    ) {
        //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPermissionDenied(response: PermissionDeniedResponse?) {
        //To change body of created functions use File | Settings | File Templates.
        Toast.makeText(this, "Kamu tidak bisa menambahkan photo profile", Toast.LENGTH_LONG).show()
    }

    override fun onBackPressed() {
        alertDialog()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            statusAdd = true
            filePath = data?.getData()!!

            Glide.with(this)
                .load(filePath)
                .apply(RequestOptions.circleCropTransform())
                .into(iv_profile)

            btn_save_edit.visibility = View.VISIBLE
            iv_add.setImageResource(R.drawable.ic_btn_delete)
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }
}