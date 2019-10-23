package com.example.moviedb


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_favor.*
import kotlinx.android.synthetic.main.fragment_list.TVType

/**
 * A simple [Fragment] subclass.
 */
class FavorFragment : Fragment() {
    lateinit var viewModel: MovieViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ):  View? {
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(MovieViewModel::class.java)
        } ?: throw Exception("bad activity")
        //viewModel.getProfile()

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favor, container, false)
    }

    lateinit var listener: OnFragmentInteractionListener

    interface OnFragmentInteractionListener {
        fun getCart(): ArrayList<Movie>
        fun updateCart()


    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener)
            listener = context
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).toolbar.title = ""

//        val recyclerView = view.findViewById(R.id.fave_recycler) as RecyclerView
//        val linearLayoutManager = LinearLayoutManager(context)
//        TVType.setText("My Favourite")
//        recyclerView.layoutManager = linearLayoutManager
//        val customAdapter = MovieAdapter(viewModel.savedMovies.value!! ){
//                movie:Movie ->recyclerViewItemSelected(movie)
//
//        }
//        ItemTouchHelper(SwipeHandler()).attachToRecyclerView(
//            `@+id/fave_recycler`
//        )
//
//        recyclerView.adapter = customAdapter // set the Adapter to RecyclerView
//

        TVType.setText("My Favourite Movies")
        viewManager = LinearLayoutManager(context)
        viewAdapter = RecyclerViewAdapter(viewModel.savedMovies.value!!){
                movie:Movie ->recyclerViewItemSelected(movie)
        }
        println("size of savedMovie" + viewModel.savedMovies.value!!.size)
        fave_recycler.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }
        viewModel.savedMovies.observe(this, Observer {
            viewAdapter.list = it
            viewAdapter.notifyDataSetChanged()
        })

        ItemTouchHelper(SwipeHandler()).attachToRecyclerView(
            fave_recycler
        )
    }
    fun recyclerViewItemSelected(movie: Movie) {
        viewModel.singleMovie.value = movie
        findNavController().navigate(R.id.action_favorFragment_to_detailFragment)
    }
    inner class SwipeHandler(): ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT){
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            viewModel.removeMovie(viewHolder.adapterPosition)
            viewAdapter.notifyItemRemoved(viewHolder.adapterPosition)
        }

    }
    lateinit var viewAdapter: RecyclerViewAdapter
    lateinit var viewManager: RecyclerView.LayoutManager

    class RecyclerViewAdapter(var list: ArrayList<Movie>,
                              val clickListener: (Movie)->Unit) :
        RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
            var viewItem =
                LayoutInflater.from(parent.context).inflate(R.layout.fav_item, parent, false)
            return RecyclerViewHolder(viewItem)
        }
        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
            return holder.bind(list[position],clickListener)
        }

        class RecyclerViewHolder(val viewItem: View) : RecyclerView.ViewHolder(viewItem) {
            fun bind(movie: Movie, clickListener: (Movie) -> Unit) {
                viewItem.run {
                    findViewById<TextView>(R.id.MVName).text = movie.title
                    findViewById<RatingBar>(R.id.RB_fav).setRating(movie.star)
                    findViewById<RatingBar>(R.id.RB_fav).setIsIndicator(true)
                    PosterLoader.getInstance().loadURL(movie.poster_path,  findViewById<ImageView>(R.id.IV_style))

                }
                itemView.setOnClickListener {
                    clickListener(movie)}
            }
        }

    }

}



