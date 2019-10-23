package com.example.moviedb


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.fragment_review.*
import java.lang.Exception
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class ReviewFragment : Fragment() {
    lateinit var movieViewModel: MovieViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        movieViewModel = activity?.run {
            ViewModelProviders.of(this).get(MovieViewModel::class.java)
        } ?: throw Exception("Activity Invalid")
        return inflater.inflate(R.layout.fragment_review, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).toolbar.title = "Rate Movie"

        var orginId = 0
        var backPath = ""
        var poster_path = ""
        var genre_ids = ""
        var title = ""
        var vote_average = 0.0
        var overview = ""
        var release_date = ""
        var loved = true
        var star = 0.0f
        ///////array list update???

        var comments = ""
        var genre_st = ""
        var idmm = 0
        var bReview = ""
        var bStar = 0.0
        var loc = 0
        movieViewModel.singleMovie.observe(this, Observer {
            orginId = it.id
            backPath = it.backdrop_path
            poster_path = it.poster_path
            genre_ids = it.genre_ids
            title = it.title
            vote_average = it.vote_average
            overview = it.overview
            release_date = it.release_date
            loved = it.loved
            star = it.star
            PosterLoader.getInstance().loadURL(it.backdrop_path, IV_post)
        })

//        movieViewModel.singleMovie.observe(this, Observer {
//            for (i in 0 until movieViewModel.savedMovies.value!!.size) {
//                movieViewModel.savedMovies.observe(this, Observer {
//                    idmm = it.get(i).id
//                    if (orginId == idmm) {
//                        ET_review.setText(it.get(i).comments)
//                        ratingBar.setRating(it.get(i).star.toFloat())
//                    }
//                })
//            }
//        })
        if(movieViewModel.checkSaved()!!){
            for (i in 0 until movieViewModel.savedMovies.value!!.size) {
                movieViewModel.savedMovies.observe(this, Observer {
                    idmm = it.get(i).id

                    if (orginId == idmm) {
                        ET_review.setText(it.get(i).comments)
                        ratingBar.setRating(movieViewModel.savedMovies.value!!.get(i)?.star)
                        loc = i
                    }
                })

            }

            //println(movieViewModel.savedMovies.value?.joinToString(",") {it.title  })

        }

        bt_save.setOnClickListener {
            movieViewModel.singleMovie.value!!.star = ratingBar.rating
            movieViewModel.singleMovie.value!!.comments = ET_review.text.toString()
            movieViewModel.singleMovie.value!!.loved = true


//            for (i in 0 until movieViewModel.savedMovies.value!!.size) {
//                movieViewModel.savedMovies.observe(this, Observer {
//                    idmm = it.get(i).id
//
//                })
//                if (orginId == idmm) {
//                    movieViewModel.savedMovies.value!!.get(i).comments = ET_review.text.toString()
//                    movieViewModel.savedMovies.value!!.get(i).star = ratingBar.rating
//                    findNavController().navigate(R.id.action_global_detailFragment)
//                    return@setOnClickListener
//                }
//            }
            if(movieViewModel.checkSaved()!!){
                movieViewModel.savedMovies.value!!.get(loc)?.comments =  ET_review.text.toString()
                movieViewModel.savedMovies.value!!.get(loc)?.star = ratingBar.rating
                findNavController().navigate(R.id.action_global_detailFragment)
                println(movieViewModel.savedMovies.value?.joinToString(",") {it.title  })
            }else {
                movieViewModel.addMovie(movieViewModel.singleMovie.value!!)
                findNavController().navigate(R.id.action_global_detailFragment)
            }
        }
        bt_cancel.setOnClickListener {
            //            var movie11 = Movie(poster_path,backPath,genre_ids,
//                title,vote_average,overview,release_date,loved,bStar,bReview,genre_st,id)
//            movieViewModel.savedMovies.value!!.set(orginId,movie11)
//            var nohad = false
//            for (i in 0 until movieViewModel.savedMovies.value!!.size) {
//                movieViewModel.savedMovies.observe(this, Observer {
//                    idmm = it.get(i).id
//
//                })
//                if (orginId == idmm) {
//                    nohad = true
//                }
//            }
//            if (nohad) {
//                movieViewModel.addMovie(movieViewModel.singleMovie.value!!)
//            }
            if(!movieViewModel.checkSaved()!!){
                //here is the problem
                movieViewModel.addMovie(movieViewModel.singleMovie.value!!)
            }else{
                ET_review.setText(movieViewModel.savedMovies.value!!.get(loc)?.comments)
                ratingBar.setRating(movieViewModel.savedMovies.value!!.get(loc)?.star)
                //save the orgin values if click cancel
            }
            findNavController().navigate(R.id.action_global_detailFragment)
        }

    }
    override fun onPause() {
        super.onPause()
        movieViewModel.singleMovie.value!!.star = ratingBar.rating
        movieViewModel.singleMovie.value!!.comments = ET_review.text.toString()
    }
}
