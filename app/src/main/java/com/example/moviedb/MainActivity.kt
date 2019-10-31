package com.example.moviedb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: MovieViewModel
    lateinit var viewAdapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        val navController=findNavController(R.id.nav_host_fragment)
        toolbar.setupWithNavController(navController)
        toolbar.title = ""
        viewModel = ViewModelProviders.of(this).get(MovieViewModel::class.java)
        viewModel.fetchUpcome()
        viewModel.fetchNowPlay()
        viewAdapter = MovieAdapter(viewModel.nowshowingMovie.value!!,{})
        viewAdapter.notifyDataSetChanged()
        viewModel.savedMovies.value = viewModel.databaseSaved?.movieDAO()?.getMovieList() as ArrayList<Movie>

    }
    var movieMenu: Menu? = null
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        movieMenu = menu
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        var navController = findNavController(R.id.nav_host_fragment)
        val bundle = Bundle()
        when (item?.itemId) {
            R.id.action_playing -> {
                bundle.putString("MovieList","nowPlaying")
                navController.navigate(R.id.action_global_listFragment,bundle)
            }

            R.id.action_coming -> {
                bundle.putString("MovieList","upComing")
                navController.navigate(R.id.action_global_listFragment,bundle)
            }
            R.id.action_saved -> {

                navController.navigate(R.id.action_global_favorFragment)
            }
        }
        return true
    }
}
