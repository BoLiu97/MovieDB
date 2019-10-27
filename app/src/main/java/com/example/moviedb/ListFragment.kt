package com.example.moviedb


import GenreDecoder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.fragment_review.*
import org.json.JSONException
import org.json.JSONObject


class ListFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_list, container, false)
    }



    override fun onViewCreated(view: View,savedInstanceState: Bundle?) {
        super.onViewCreated(view,savedInstanceState)
        (activity as AppCompatActivity).toolbar.title = ""

        // get the reference of RecyclerView that was created in the MainActivity
        val recyclerView = view.findViewById(R.id.list_recycler) as RecyclerView
        // set a LinearLayoutManager with default vertical orientation
        val jsonString = resources.openRawResource(R.raw.genre).bufferedReader().use { it.readText() }
        val genre=GenreDecoder(jsonString).codes

        val linearLayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = linearLayoutManager
        var name = arguments?.getString("MovieList")
            try {

                if (name == "upComing") {

                    if (viewModel.upcomingMovie.value?.size!! < 1) {
                        viewModel.fetchUpcome()
                    }
                    TVType.setText("Upcoming")

                    println(viewModel.upcomingMovie.value?.joinToString(",") { it.title })
                    println(" life is soooooo hard")


                    viewModel.upcomingMovie.observe(this, Observer {
                        for (i in 0 until viewModel.upcomingMovie.value!!.size) {
                            viewModel.upcomingMovie.observe(this, Observer {
                                val genres = it.get(i).genre_ids
                                val genres1 = genres.split(",")
                                var genreString = ""
                                for (i in 0 until genres1.size) {
                                    val genrenum = genres1[i].replace("[", "")
                                        .replace("]", "")
                                        .toInt()
                                    genreString += genre.get(genrenum) + " "
                                }
                                it.get(i).genre_st = genreString
                            }
                            )
                        }
                    })

                } else {
                    TVType.setText("Now Playing")

                    // viewModel.fetchNowPlay()
                    if (viewModel.nowshowingMovie.value?.size!! < 1) {
                        viewModel.fetchNowPlay()
                    }
                    println(viewModel.nowshowingMovie.value?.joinToString(",") { it.title })
                    println(" life is soooooo hard")


                    viewModel.nowshowingMovie.observe(this, Observer {
                        for (i in 0 until viewModel.nowshowingMovie.value!!.size) {
                            viewModel.nowshowingMovie.observe(this, Observer {
                                val genres = it.get(i).genre_ids
                                val genres1 = genres.split(",")
                                var genreString = ""
                                for (i in 0 until genres1.size) {
                                    val genrenum = genres1[i].replace("[", "")
                                        .replace("]", "")
                                        .toInt()
                                    genreString += genre.get(genrenum) + " "
                                }
                                it.get(i).genre_st = genreString
                            }
                            )
                        }
                    })
                }

            } catch (e: JSONException) {
                //exception
                e.printStackTrace()
            }

        if(name == "upComing") {
            //  call the constructor of MyAdapter to send the reference and data to Adapter
            val customAdapter = MovieAdapter(viewModel.upcomingMovie.value!! ){
                movie:Movie ->recyclerViewItemSelected(movie)

            }
            recyclerView.adapter = customAdapter // set the Adapter to RecyclerView
        }else{
            val customAdapter = MovieAdapter(viewModel.nowshowingMovie.value!! ){
                    movie:Movie ->recyclerViewItemSelected(movie)
            }
            recyclerView.adapter = customAdapter // set the Adapter to RecyclerView
        }

    }
    fun recyclerViewItemSelected(movie: Movie) {
        viewModel.singleMovie.value = movie
        findNavController().navigate(R.id.action_listFragment_to_detailFragment)
    }


}


