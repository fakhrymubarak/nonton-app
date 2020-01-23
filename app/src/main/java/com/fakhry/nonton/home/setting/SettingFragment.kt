package com.fakhry.nonton.home.setting


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat.finishAffinity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.fakhry.nonton.R
import com.fakhry.nonton.sign.signin.SignInActivity
import com.fakhry.nonton.utils.Preferences
import kotlinx.android.synthetic.main.fragment_setting.*
import kotlinx.android.synthetic.main.popup_alert.*
import kotlinx.android.synthetic.main.popup_alert.view.*

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

        val profilePict = preferences.getValues("url")

        iv_nama.text = preferences.getValues("nama")
        tv_email.text = preferences.getValues("email")


        //START - CONDITION FOR SETTING PROFILE PICTURE
        if(profilePict == ""){
            iv_profile.setImageResource(R.drawable.user_pic)
        }else{
            Glide.with(this)
                .load(profilePict)
                .apply(RequestOptions.circleCropTransform())
                .into(iv_profile)
        }
        //END - CONDITION FOR SETTING PROFILE PICTURE

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
            //Inflate the dialog with custom view
            val mDialogView = LayoutInflater.from(context).inflate(R.layout.popup_alert, null)
            //AlertDialogBuilder
            val mBuilder = AlertDialog.Builder(context!!)
                .setView(mDialogView)
            //show dialog
            val mAlertDialog = mBuilder.show()

            mAlertDialog.tv_alert_title.text = "Logout?"
            mAlertDialog.tv_alert_msg.text = "Apakan kamu yakin ingin logout akun?"

            mDialogView.btn_yes.setOnClickListener {
                activity!!.finish()
                preferences.setValues("status", "0") //SET STATUS PREFERENCES

                preferences.setValues("username", "") //SET STATUS PREFERENCES
                preferences.setValues("password", "") //SET STATUS PREFERENCES
                preferences.setValues("nama", "") //SET STATUS PREFERENCES
                preferences.setValues("email", "") //SET STATUS PREFERENCES
                preferences.setValues("url", "") //SET STATUS PREFERENCES
                preferences.setValues("saldo", "") //SET STATUS PREFERENCES
                val intent = Intent(
                    context,
                    SignInActivity::class.java
                )
                startActivity(intent)
                mAlertDialog.dismiss()
            }
            mDialogView.btn_no.setOnClickListener {
                mAlertDialog.dismiss()
            }
        }
    }


}
