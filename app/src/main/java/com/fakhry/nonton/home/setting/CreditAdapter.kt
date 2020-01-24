package com.fakhry.nonton.home.setting

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fakhry.nonton.R
import com.fakhry.nonton.home.model.Credit
import java.text.NumberFormat
import java.util.*

class CreditAdapter(
    private var data: List<Credit>,
    private val listener: (Credit) -> Unit
) : RecyclerView.Adapter<CreditAdapter.LeagueViewHolder>() {

    lateinit var ContextAdapter: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeagueViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        ContextAdapter = parent.context
        val inflatedView: View = layoutInflater.inflate(R.layout.row_item_transaksi, parent, false)

        return LeagueViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: LeagueViewHolder, position: Int) {
        holder.bindItem(data[position], listener, ContextAdapter, position)
    }

    override fun getItemCount(): Int = data.size

    class LeagueViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val tvTitle: TextView = view.findViewById(R.id.tv_title_transaction)
        private val tvTransaksi: TextView = view.findViewById(R.id.tv_id_transaction)
        private val tvSaldo: TextView = view.findViewById(R.id.tv_amount_transaction)

        fun bindItem(data: Credit, listener: (Credit) -> Unit, context: Context, position: Int) {
            tvTitle.setTextColor(Color.parseColor("#FF2667"))
            tvTransaksi.setTextColor(Color.parseColor("#FF2667"))
            tvSaldo.setTextColor(Color.parseColor("#FF2667"))

            tvTitle.text = data.title
            tvTransaksi.text = data.id
            currency(data.price!!.toDouble(), tvSaldo)

            itemView.setOnClickListener {
                listener(data)
            }
        }

        private fun currency(harga: Double, textView: TextView) {
            val localeID = Locale("in", "ID")
            val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
            textView.setText(formatRupiah.format(harga))
        }
    }
}

