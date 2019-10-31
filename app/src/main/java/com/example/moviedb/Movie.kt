package com.example.moviedb


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movieDB")
class Movie {
    @PrimaryKey
    var id = 0
    var popularity = 0.0
    var poster_path = ""
    var backdrop_path = ""
    var original_language = ""
    var genre_ids = ""
    var title = ""
    var vote_average = 0.0
    var overview = ""
    var release_date = ""
    var loved = false
    var star = 0.0f
    var comments = ""
    var genre_st = ""

}



