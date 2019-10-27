package com.example.moviedb

import GenreDecoder
import android.app.Application
import android.widget.RatingBar
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.GeneratedAdapter
import java.nio.file.Files.find
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query


class MovieViewModel(application: Application) : AndroidViewModel(application){
    private val apiURL = "https://api.themoviedb.org/"
    private val apiKey = "e4895b42ccbc3c00f84418206bcd00b5"
    private val language = "en-US"

    var genre = MutableLiveData<ArrayList<genre>>()
    var upcomingMovie = MutableLiveData<ArrayList<Movie>>()
    var nowshowingMovie = MutableLiveData<ArrayList<Movie>>()
    var savedMovies = MutableLiveData<ArrayList<Movie>>()
    var singleMovie = MutableLiveData<Movie>()
    val databaseNowplay = MovieDB.getDBObject(getApplication<Application>().applicationContext)
    val databaseUpcome = MovieDB.getDBObject(getApplication<Application>().applicationContext)


    init {
        savedMovies.value=ArrayList()
        nowshowingMovie.value=ArrayList()
        upcomingMovie.value=ArrayList()
        genre.value=ArrayList()

    }
    fun updateListNowplay() {
        nowshowingMovie.value?.addAll(
            databaseNowplay?.movieDAO()?.getMovieList() as ArrayList<Movie>)
    }
    fun updateListUpcome() {
        upcomingMovie.value?.addAll(
            databaseUpcome?.movieDAO()?.getMovieList() as ArrayList<Movie>)
    }
    interface MovieService {

        @GET("3/movie/now_playing?")
        fun getListNowPlay(
            @Query("api_key") api_key: String,
            @Query("language") language: String
        ): Call<ResponseBody>

        @GET("3/movie/upcoming?")
        fun getListUpcoming(
            @Query("api_key") api_key: String,
            @Query("language") language: String
        ): Call<ResponseBody>
    }

    fun fetchNowPlay() {

        val retrofit = Retrofit.Builder()
            .baseUrl(apiURL)
            .build()

        val service = retrofit.create(MovieService::class.java)
        val call = service.getListNowPlay( apiKey, language)
        call.enqueue(MovieCallback(1))
    }
    fun fetchUpcome() {

        val retrofit = Retrofit.Builder()
            .baseUrl(apiURL)
            .build()

        val service = retrofit.create(MovieService::class.java)
        val call = service.getListUpcoming( apiKey, language)
        call.enqueue(MovieCallback(2))
    }
    inner class MovieCallback(int: Int) : Callback<ResponseBody> {
        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
        }
        val ti  = int
        override fun onResponse(
            call: Call<ResponseBody>,
            response: Response<ResponseBody>
        ) {
            if (response.isSuccessful) {
                response.body()?.let {
                    // println(it.string())// you can only get the string once
                    if(ti ==1 ){
                        addMovieListNP(it.string())
                    }else{
                        addMovieListUC(it.string())
                    }
                    //addMovieListNP(it.string())


                }
            }
        }
    }
    fun addMovieListNP(json: String) {
        val movie = Movie()
        val data = JSONObject(json)
        var arrayOfObject = data.getJSONArray("results")
        //var savedMovies = MutableLiveData<ArrayList<Movie>>()
        var movies = ArrayList<Movie>()
        for (i in 0 until arrayOfObject.length()) {
            // create a JSONObject for fetching single user data
            val userDetail = arrayOfObject.getJSONObject(i)
                movie.poster_path = userDetail.getString("poster_path")
                movie.backdrop_path = userDetail.getString("backdrop_path")
                // fetch email and name and store it in arraylist
                movie.title = userDetail.getString("title")
                movie.vote_average = userDetail.getDouble("vote_average")
                movie.overview = userDetail.getString("overview")
                movie.release_date = userDetail.getString("release_date")
                movie.loved = false
                movie.star = 0.0f
                movie.comments = ""
                movie.id = userDetail.getInt("id")
                movie.genre_ids = userDetail.getString("genre_ids")
                //genre_ids may be a problem in future
                //need to do this part in future
                //movies.add(movie)
                println(movie.title)
                databaseNowplay?.movieDAO()?.insert(movie)
                updateListNowplay()

        }
//        databaseNowplay?.movieDAO()?.insert(movies)
//        updateListNowplay()

    }
    fun addMovieListUC(json: String) {
        val movie = Movie()
        val data = JSONObject(json)
        var arrayOfObject = data.getJSONArray("results")
        var movies = ArrayList<Movie>()

        for (i in 0 until arrayOfObject.length()) {

            // create a JSONObject for fetching single user data
            val userDetail = arrayOfObject.getJSONObject(i)
            movie.poster_path = userDetail.getString("poster_path")
            movie.backdrop_path = userDetail.getString("backdrop_path")
            // fetch email and name and store it in arraylist
            movie.title = userDetail.getString("title")
            movie.vote_average = userDetail.getDouble("vote_average")
            movie.overview = userDetail.getString("overview")
            movie.release_date = userDetail.getString("release_date")
            movie.loved = false
            movie.star = 0.0f
            movie.comments= ""
            movie.id = userDetail.getInt("id")
            movie.genre_ids = userDetail.getString("genre_ids")
            //genre_ids may be a problem in future
            //need to do this part in future

            databaseUpcome?.movieDAO()?.insert(movie)
            updateListUpcome()
            //movies.add(movie)
            println("current movie name is: "+ movie.title)

        }
        println("movie cap is " + movies.size)
//        databaseUpcome?.movieDAO()?.insert(movies)
//        updateListUpcome()

    }
    fun checkSaved():Boolean?{
        return  savedMovies.value?.any {
            it.id==singleMovie.value?.id
        }

    }
    fun checkListN(int: Int):Boolean?{
        val aa = int
        return  nowshowingMovie.value?.any {
            it.id == aa
        }
    }
    fun checkListU(int: Int):Boolean?{
        val aa = int
        return  upcomingMovie.value?.any {
            it.id == aa
        }
    }
    fun updateProfile(rating:Double,review:String){
        savedMovies.value!!
    }
    fun addMovie(movie: Movie){
        savedMovies.value?.add(movie)
    }
    fun removeMovie(index:Int){
        savedMovies.value?.removeAt(index)
    }
}