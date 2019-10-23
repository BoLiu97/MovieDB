package com.example.moviedb

import GenreDecoder
import android.widget.RatingBar
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.os.Bundle
import androidx.lifecycle.GeneratedAdapter
import java.nio.file.Files.find


class MovieViewModel :ViewModel(){
    var genre = MutableLiveData<ArrayList<genre>>()
    var upcomingMovie = MutableLiveData<ArrayList<Movie>>()
    var nowshowingMovie = MutableLiveData<ArrayList<Movie>>()
    var savedMovies = MutableLiveData<ArrayList<Movie>>()
    var singleMovie = MutableLiveData<Movie>()

    init {
        savedMovies.value=ArrayList()
        nowshowingMovie.value=ArrayList()
        upcomingMovie.value=ArrayList()
        genre.value=ArrayList()
    }
    fun checkSaved():Boolean?{
         return  savedMovies.value?.any {
                it.id==singleMovie.value?.id
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