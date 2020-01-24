package com.fakhry.nonton.home.setting

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fakhry.nonton.R
import com.fakhry.nonton.home.model.Credit


//ADAPTER YANG ATUR ISI DARI FILM NYA
class CreditAdapter(private var data: List<Credit>,
                    private val listener: (Credit) -> Unit)
    : RecyclerView.Adapter<CreditAdapter.LeagueViewHolder>() {

    lateinit var ContextAdapter : Context

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

        private val tvTransaksi: TextView = view.findViewById(R.id.tv_transaksi)
//        private val tvWaktu: TextView = view.findViewById(R.id.tv_waktu)
        private val tvSaldo: TextView = view.findViewById(R.id.tv_saldo)

        fun bindItem(data: Credit, listener: (Credit) -> Unit, context : Context, position : Int) {

            tvTransaksi.text = data.id
//            tvWaktu.text = data
            tvSaldo.text = data.price

            itemView.setOnClickListener {
                listener(data)
            }
        }

    }

}

