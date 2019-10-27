package com.example.moviedb


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movieDB")
class Movie {
    @PrimaryKey
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
    var id = 0

}


//@PrimaryKey(autoGenerate = true)
//var popularity: Double? = null,
//var vote_count: Int? = null,
//var poster_path: String = "",
//var imdb_id: String = "",
//var backdrop_path: String = "",
//var original_language: String = "",
//var original_title: String = "",
//var genre_ids: String? = null,
//var title: String? = null,
//var vote_average: Double? = null,
//var overview: String? = null,
//var release_date: String? = null,
//var loved: Boolean = false,
//var start: Float = 0.0f,
//var comments: String? = null


//class Movie(
//    var poster_path:String,
//    var backdrop_path:String,
//    var genre_ids:String,
//    var title:String,
//    var vote_average:Double,
//    var overview:String,
//    var release_date:String,
//    var loved:Boolean,
//    var star:Float,
//    var comments:String,
//    var genre_st:String,
//    var id:Int
//
//)


