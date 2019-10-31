package com.example.moviedb

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MovieAdapter(

                   var movieData: ArrayList<Movie>,
                   val clickListener: (Movie)->Unit ) : RecyclerView.Adapter<MovieAdapter.RecyclerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        // inflate the Layout with the UI that we have created for the RecyclerView
        val v = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return RecyclerViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        // set the data in items of the RecyclerView
        holder.MVname.text = movieData[position].title
        holder.MVGenre.text = movieData[position].genre_st
        PosterLoader.getInstance().loadURL(movieData[position].poster_path, holder.IV_style)
        holder.bind(movieData[position],clickListener)
    }


    override fun getItemCount(): Int {
        //return the item count
        return movieData.size
    }

    inner class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //connecting od with the text views
        internal var MVname: TextView = itemView.findViewById(R.id.MVName) as TextView
        internal var MVGenre: TextView = itemView.findViewById(R.id.MVGenre) as TextView
        internal var IV_style: ImageView = itemView.findViewById(R.id.IV_style) as ImageView
        fun bind(movie: Movie, clickListener: (Movie) -> Unit) {
            itemView.setOnClickListener {
                clickListener(movie)}
        }
    }
}