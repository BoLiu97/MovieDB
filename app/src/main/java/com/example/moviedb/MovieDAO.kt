package com.example.moviedb

import androidx.room.*
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MovieDAO {
    @Query("SELECT * FROM movieDB")
    fun getMovieList(): List<Movie>

    @Query("SELECT * FROM movieDB WHERE id = :id")
    fun getMovie(id: String): Movie

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movie: Movie)

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insert(movies: List<Movie>)

    @Query("DELETE FROM movieDB WHERE id = :id")
    fun deleteMovie(id: String)

    @Query("DELETE FROM movieDB")
    fun deleteAll()

    @Query("SELECT * FROM movieDB LIMIT :pageSize OFFSET :pageIndex")
    fun getMoviePage(pageSize: Int, pageIndex: Int): List<Movie>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun update(movie: Movie)

}