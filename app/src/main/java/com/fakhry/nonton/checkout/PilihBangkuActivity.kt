package com.fakhry.nonton.checkout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.fakhry.nonton.R
import com.fakhry.nonton.checkout.model.Checkout
import com.fakhry.nonton.home.model.Film
import kotlinx.android.synthetic.main.activity_pilih_bangku.*


class PilihBangkuActivity : AppCompatActivity() {

    var statusA3: Boolean = false
    var statusA4: Boolean = false
    var statusB1: Boolean = false
    var statusB2: Boolean = false
    var total: Int = 0

    private var dataList = ArrayList<Checkout>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pilih_bangku)


        val data = intent.getParcelableExtra<Film>("data")

        //HASIL DATA YANG DIBAWA DARI DETAIL ACTIVITY
        tv_judul.text = data.judul

        //START - KURSI YANG TERSEDIA
        a3.setOnClickListener {
            if (statusA3) {
                a3.setImageResource(R.drawable.ic_rectangle_empty)
                statusA3 = false
                total -= 1
                belitiket(total)

            } else {
                a3.setImageResource(R.drawable.ic_rectangle_selected)
                statusA3 = true
                total += 1
                belitiket(total)

                val data = Checkout("A3", "70000")
                dataList.add(data)
            }
        }

        a4.setOnClickListener {
            if (statusA4) {
                a4.setImageResource(R.drawable.ic_rectangle_empty)
                statusA4 = false
                total -= 1
                belitiket(total)
            } else {
                a4.setImageResource(R.drawable.ic_rectangle_selected)
                statusA4 = true
                total += 1
                belitiket(total)

                val data = Checkout("A4", "80000")
                dataList.add(data)
            }
        }

        b1.setOnClickListener {
            if (statusB1) {
                b1.setImageResource(R.drawable.ic_rectangle_empty)
                statusB1 = false
                total -= 1
                belitiket(total)
            } else {
                b1.setImageResource(R.drawable.ic_rectangle_selected)
                statusB1 = true
                total += 1
                belitiket(total)

                val data = Checkout("B14", "30000")
                dataList.add(data)
            }
        }
        b2.setOnClickListener {
            if (statusB2) {
                b2.setImageResource(R.drawable.ic_rectangle_empty)
                statusB2 = false
                total -= 1
                belitiket(total)
            } else {
                b2.setImageResource(R.drawable.ic_rectangle_selected)
                statusB2 = true
                total += 1
                belitiket(total)

                val data = Checkout("B2", "50000")
                dataList.add(data)
            }
        }
        //END - KURSI YANG TERSEDIA

        //DATA KURSI YANG DIPILIH, KEMUDIAN DIKIRIM KE CHECKOUT ACTIVITY MELALUI INTENT
        btn_home.setOnClickListener {
            val intent = Intent(
                this@PilihBangkuActivity,
                CheckoutActivity::class.java
            )
            intent.putExtra("data1", dataList)
            intent.putExtra("data2", data)
            startActivity(intent)
        }

        iv_back.setOnClickListener {
            finish()
        }

    }


    private fun belitiket(total: Int) {
        if (total == 0) {
            btn_home.setText("Beli Tiket")
            btn_home.visibility = View.INVISIBLE
        } else {
            btn_home.setText("Beli Tiket (" + total + ")")
            btn_home.visibility = View.VISIBLE
        }

    }
}
