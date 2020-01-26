package com.fakhry.nonton.home.setting

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fakhry.nonton.R
import com.fakhry.nonton.home.dashboard.DashboardFragment
import com.fakhry.nonton.home.model.Debit
import java.text.NumberFormat
import java.util.*

class DebitAdapter(
    private var data: List<Debit>,
    private val listener: (Debit) -> Unit
) : RecyclerView.Adapter<DebitAdapter.LeagueViewHolder>() {

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
        private val tvDate: TextView = view.findViewById(R.id.tv_date_transaction)

        fun bindItem(data: Debit, listener: (Debit) -> Unit, context: Context, position: Int) {
            tvSaldo.setTextColor(Color.parseColor("#52E300"))

            tvTitle.text = data.detail
            tvTransaksi.text = data.id
            tvDate.text = data.date
            DashboardFragment().currency(data.amount!!.toDouble(), tvSaldo)

            itemView.setOnClickListener {
                listener(data)
            }
        }
    }
}

