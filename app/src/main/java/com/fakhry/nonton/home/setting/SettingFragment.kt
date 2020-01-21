package com.fakhry.nonton.home.setting


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat.finishAffinity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.fakhry.nonton.R
import com.fakhry.nonton.sign.signin.SignInActivity
import com.fakhry.nonton.utils.Preferences
import kotlinx.android.synthetic.main.fragment_setting.*

/**
 * A simple [Fragment] subclass.
 */
class SettingFragment : Fragment() {

    lateinit var preferences: Preferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        preferences = Preferences(context!!.applicationContext)

        iv_nama.text = preferences.getValues("nama")
        tv_email.text = preferences.getValues("email")


        //START - GLIDE, METHOD YANG BERFUNGSI UNTUK MENGECILKAN GAMBAR YANG TERLALU BESAR
        Glide.with(this)
            .load(preferences.getValues("url"))
            .apply(RequestOptions.circleCropTransform())
            .into(iv_profile)
        //END - GLIDE, METHOD YANG BERFUNGSI UNTUK MENGECILKAN GAMBAR YANG TERLALU BESAR

        tv_my_wallet.setOnClickListener{
            val intent = Intent(
                context,
                MyWalletActivity::class.java
            )
            startActivity(intent)
        }

        tv_edit_profile.setOnClickListener{
            val intent = Intent(
                context,
                EditProfileActivity::class.java
            )
            startActivity(intent)
        }

        btn_logout.setOnClickListener{
            activity!!.finish()
            preferences.setValues("status", "0") //SET STATUS PREFERENCES

            preferences.setValues("username", "") //SET STATUS PREFERENCES
            preferences.setValues("password", "") //SET STATUS PREFERENCES
            preferences.setValues("nama", "") //SET STATUS PREFERENCES
            preferences.setValues("email", "") //SET STATUS PREFERENCES
            preferences.setValues("url", "") //SET STATUS PREFERENCES
            preferences.setValues("saldo", "") //SET STATUS PREFERENCES

            var email: String ?=""
            var nama: String ?=""
            var password: String ?=""
            var url: String ?=""
            var username: String ?=""
            var saldo: String ?=""
            val intent = Intent(
                context,
                SignInActivity::class.java
            ).putExtra("EXIT", true)
            startActivity(intent)
        }


    }
}
