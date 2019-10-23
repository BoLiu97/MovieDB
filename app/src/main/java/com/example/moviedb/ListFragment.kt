package com.example.moviedb


import GenreDecoder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_list.*
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

            if(name == "upComing"){
                var cay = ArrayList<Movie>()
                 val jsonString = resources.openRawResource(R.raw.upcoming).bufferedReader().use { it.readText() }
                TVType.setText("Upcoming")
                val obj = JSONObject(jsonString)
                // fetch JSONArray named users by using getJSONArray
                val userArray = obj.getJSONArray("results")
                // implement for loop for getting users data i.e. name, email and contact
                for (i in 0 until userArray.length()) {
                    // create a JSONObject for fetching single user data
                    val userDetail = userArray.getJSONObject(i)
                    val poster_path = userDetail.getString("poster_path")
                    val backdrop_path = userDetail.getString("backdrop_path")
                    // fetch email and name and store it in arraylist
                    val title = userDetail.getString("title")
                    val vote_average = userDetail.getDouble("vote_average")
                    val overview = userDetail.getString("overview")
                    val release_date = userDetail.getString("release_date")
                    val loved = false
                    var star = 0.0f
                    var comments= ""
                    var id = userDetail.getInt("id")
                    val genres = userDetail.getString("genre_ids")
                    val genres1 = genres.split(",")
                    var genreString = ""
                    for (i in 0 until genres1.size) {
                        val genrenum = genres1[i].
                            replace("[","")
                            .replace("]","")
                            .toInt()
                        genreString += genre.get(genrenum)+" "
                    }
                    val genre_str = genreString
                    cay.add(Movie(poster_path,backdrop_path,genres,title,
                        vote_average,overview,release_date,loved,star,comments,genre_str,id  ))
                }
                viewModel.upcomingMovie.value =cay

            }else{
                 val jsonString = resources.openRawResource(R.raw.now_playing).bufferedReader().use { it.readText() }
                TVType.setText("Now Playing")
                var clay = ArrayList<Movie>()
                val obj = JSONObject(jsonString)
                // fetch JSONArray named users by using getJSONArray
                val userArray = obj.getJSONArray("results")
                // implement for loop for getting users data i.e. name, email and contact
                for (i in 0 until userArray.length()) {
                    // create a JSONObject for fetching single user data
                    val userDetail = userArray.getJSONObject(i)
                    val poster_path = userDetail.getString("poster_path")
                    val backdrop_path = userDetail.getString("backdrop_path")
                    // fetch email and name and store it in arraylist
                    val title = userDetail.getString("title")
                    val vote_average = userDetail.getDouble("vote_average")
                    val overview = userDetail.getString("overview")
                    val release_date = userDetail.getString("release_date")
                    val loved = false
                    var star = 0.0f
                    var comments= ""
                    var id = userDetail.getInt("id")
                    val genres = userDetail.getString("genre_ids")
                    val genres1 = genres.split(",")
                    var genreString = ""
                    for (i in 0 until genres1.size) {
                        val genrenum = genres1[i].
                            replace("[","")
                            .replace("]","")
                            .toInt()
                        genreString += genre.get(genrenum)+" "
                    }
                    val genre_str = genreString
                    clay.add(Movie(poster_path,backdrop_path,genres,title,
                        vote_average,overview,release_date,loved,star,comments,genre_str,id  ))
                }
                viewModel.nowshowingMovie.value =clay


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


