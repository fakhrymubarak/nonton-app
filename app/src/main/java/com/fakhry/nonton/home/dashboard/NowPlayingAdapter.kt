package com.fakhry.nonton.home.dashboard

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fakhry.nonton.R
import com.fakhry.nonton.home.model.Film

class NowPlayingAdapter(private var data: List<Film>,
                        private val listener: (Film) -> Unit)
    : RecyclerView.Adapter<NowPlayingAdapter.LeagueViewHolder>() {

    lateinit var ContextAdapter : Context

    //START - STANDARD FUNCTION ADAPTER
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeagueViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        ContextAdapter = parent.context
        val inflatedView: View = layoutInflater.inflate(R.layout.row_item_now_playing, parent, false)

        return LeagueViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: LeagueViewHolder, position: Int) {
        holder.bindItem(data[position], listener, ContextAdapter, position)
    }

    override fun getItemCount(): Int = data.size
    //END - STANDARD FUNCTION ADAPTER


    class LeagueViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val tvTitle: TextView = view.findViewById(R.id.tv_transaksi)
        private val tvGenre: TextView = view.findViewById(R.id.tv_waktu)
        private val tvRate: TextView = view.findViewById(R.id.tv_rating)
        private val tvImage: ImageView = view.findViewById(R.id.iv_poster_image)

        fun bindItem(data: Film, listener: (Film) -> Unit, context : Context, position : Int) {

            tvTitle.text = data.judul
            tvGenre.text = data.genre
            tvRate.text = data.rating

            Glide.with(context)
                .load(data.poster)
                .into(tvImage)

            itemView.setOnClickListener {
                listener(data)
            }
        }

    }

}

