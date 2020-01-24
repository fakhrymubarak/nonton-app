package com.fakhry.nonton.sign.signup

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast

import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.fakhry.nonton.home.HomeActivity
import com.fakhry.nonton.R
import com.fakhry.nonton.utils.Preferences
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_sign_up_photo_screen.*
import java.util.*

class SignUpPhotoScreenActivity : AppCompatActivity(), PermissionListener {


    var statusAdd: Boolean = false
    lateinit var filePath: Uri


    private lateinit var storage: FirebaseStorage
    private lateinit var storageReference: StorageReference

    private lateinit var mFirebaseDatabase: DatabaseReference

    lateinit var preferences: Preferences

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_photo_screen)

        preferences = Preferences(this)
        storage = FirebaseStorage.getInstance()
        storageReference = storage.getReference()

        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference("User")


        tv_hello.text = "Selamat Datang,\n" + intent.getStringExtra("nama")

        //START - KONDISI KETIKA BUTTON ADD FOTO DI TEKAN
        iv_add.setOnClickListener {
            if (statusAdd) {
                statusAdd = false
                btn_save.visibility = View.INVISIBLE
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
        }
        //END - KONDISI KETIKA BUTTON ADD FOTO DI TEKAN

        //START - KONDISI KETIKA BUTTON UPLOAD FOTO DI TEKAN
        btn_home.setOnClickListener {

            finishAffinity()

            val intent = Intent(
                this@SignUpPhotoScreenActivity,
                HomeActivity::class.java
            )
            startActivity(intent)
        }
        //END - KONDISI KETIKA BUTTON UPLOAD FOTO DI TEKAN

        btn_save.setOnClickListener {
            if (filePath != null) {
                val progressDialog = ProgressDialog(this)
                progressDialog.setTitle("Uploading...")
                progressDialog.show()

                Log.v("tamvan", "file uri upload 2 " + filePath)

                val ref = storageReference.child("images/" + UUID.randomUUID().toString())
                ref.putFile(filePath)
                    .addOnSuccessListener {
                        progressDialog.dismiss()
                        Toast.makeText(
                            this@SignUpPhotoScreenActivity,
                            "Uploaded",
                            Toast.LENGTH_SHORT
                        ).show()

                        ref.downloadUrl.addOnSuccessListener {
                            preferences.setValues("url", it.toString())

                            //START - TRANSFER URL TO DATABASE
                            mFirebaseDatabase.child(intent.getStringExtra("username")).child("url").setValue(it.toString())

                            finishAffinity()
                            val intent = Intent(
                                this@SignUpPhotoScreenActivity,
                                HomeActivity::class.java
                            )
                            startActivity(intent)
                        }
//                        Log.v("tamvan", "url(3) " + it.toString())


                    }
                    .addOnFailureListener { e ->
                        progressDialog.dismiss()
                        Toast.makeText(
                            this@SignUpPhotoScreenActivity,
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
        }
    }

    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
//        //To change body of created functions use File | Settings | File Templates.
//        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
//            takePictureIntent.resolveActivity(packageManager)?.also {
//                startActivityForResult(takePictureIntent, CAMERA_REQUEST)
//            }
//        }
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
        Toast.makeText(this, "Tergesah? Klik tombol Upload Nanti aja", Toast.LENGTH_LONG).show()
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

            btn_save.visibility = View.VISIBLE
            iv_add.setImageResource(R.drawable.ic_btn_delete)
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }
}