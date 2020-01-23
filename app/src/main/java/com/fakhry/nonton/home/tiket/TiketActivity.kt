package com.fakhry.nonton.home.tiket


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.fakhry.nonton.R
import com.fakhry.nonton.checkout.model.Checkout
import com.fakhry.nonton.home.model.Film
import kotlinx.android.synthetic.main.activity_tiket.*
import kotlinx.android.synthetic.main.popup_qr_code.view.*


class TiketActivity : AppCompatActivity() {

    private var dataList = ArrayList<Checkout>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tiket)

        val data = intent.getParcelableExtra<Film>("data")

        tv_title.text = data.judul
        tv_waktu.text = data.genre
        tv_rate.text = data.rating

        Glide.with(this)
            .load(data.poster)
            .into(iv_poster_image)

        rc_checkout.layoutManager = LinearLayoutManager(this)
        dataList.add(Checkout("C1", ""))
        dataList.add(Checkout("C2", ""))

        rc_checkout.adapter =
            TiketAdapter(dataList) {
            }

        iv_back.setOnClickListener {
            finish()
        }

        iv_qr_code_1.setOnClickListener {
            alertDialog()
        }

        cl_qr_code_2.setOnClickListener {
            alertDialog()
        }
    }

    private fun alertDialog() {
        //Inflate the dialog with custom view
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.popup_qr_code, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
        //show dialog
        val mAlertDialog = mBuilder.show()

        mDialogView.btn_yes.setOnClickListener {
            //dismiss dialog
            mAlertDialog.dismiss()
        }
    }
}
