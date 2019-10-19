package com.example.moviedb


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.fragment_detail.FV_movie
import kotlinx.android.synthetic.main.fragment_review.*

import java.lang.Exception



/**
 * A simple [Fragment] subclass.
 */
class DetailFragment : Fragment() {
    lateinit var movieViewModel: MovieViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        movieViewModel = activity?.run {
            ViewModelProviders.of(this).get(MovieViewModel::class.java)
        }?: throw Exception ("Activity Invalid")
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).toolbar.title = "Movie Detail"
//        toolbar.setNavigationOnClickListener{
//
//        }
//        toolbar.setNavigationOnClickListener { view -> onBackPressed() }

//        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
//            // Handle the back button event
//        }
        bt_edit.setOnClickListener{
            findNavController().navigate(R.id.action_detailFragment_to_reviewFragment)
        }
        bt_like.setOnClickListener{
            findNavController().navigate(R.id.action_detailFragment_to_reviewFragment)
        }
        rating.setIsIndicator(true)
        var idMovie = 0
        var svComm = ""
        var svRate = 0.0
        TV_personal.isVisible =false
        rating.isVisible=false
        bt_edit.isVisible = false
        movieViewModel.singleMovie.observe(this, Observer {
            TV_name.setText(it.title)
            TV_data.setText("Lacun Date: " + it.release_date)
            TV_rate.setText("Public Rating: " + it.vote_average.toString())
            TV_type.setText("Genres: " + it.genre_st)
            TV_des.setText(it.overview)
            idMovie = it.id
            PosterLoader.getInstance().loadURL(it.backdrop_path, FV_movie)
        })
        idMovie =movieViewModel.singleMovie.value!!.id
        var idmm =0
        for (i in 0 until movieViewModel.savedMovies.value!!.size) {
            movieViewModel.savedMovies.observe(this, Observer {
                idmm = it.get(i).id
                if(idMovie == idmm){
                    TV_personal.setText(it.get(i).comments)
                    rating.setRating(it.get(i).star.toFloat())
                    TV_personal.isVisible =true
                    rating.isVisible=true
                    bt_edit.isVisible = true
                }

            })
//            if(idMovie == idmm){
//                TV_personal.setText(svComm)
//                rating.setRating(svRate.toFloat())
//            }
        }
    }


}
