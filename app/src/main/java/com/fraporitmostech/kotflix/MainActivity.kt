package com.fraporitmostech.kotflix

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.fraporitmostech.kotflix.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response

class MainActivity : AppCompatActivity(), OnClickListener {
    private lateinit var adapterPremier: AdapterMovie
    private lateinit var adapterTerror: AdapterMovie
    private var listPremier = mutableListOf<Movie>()
    private var listTerror = mutableListOf<Movie>()
    private lateinit var mBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        GlobalScope.launch(Dispatchers.IO) {
            val service: Endpoints = Connection.ResponseEngine().create(Endpoints::class.java)
            val response: Response<ResponseMovieApi> = service.getDataMovies()

            runOnUiThread {
                if (response.isSuccessful) {

                    for (movie in response.body()?.estreno!!) {
                        listPremier.add(movie)
                        adapterPremier = AdapterMovie(listPremier, this@MainActivity)
                        mBinding.rvPremiere.apply {
                            adapter = adapterPremier
                            layoutManager = LinearLayoutManager(
                                this@MainActivity,
                                LinearLayoutManager.HORIZONTAL,
                                false
                            )
                        }

                    }
                    for (movie in response.body()?.terror!!) {
                        listTerror.add(movie)
                        adapterTerror = AdapterMovie(listTerror, this@MainActivity)
                        mBinding.rvHorror.apply {
                            adapter = adapterTerror
                            layoutManager = LinearLayoutManager(
                                this@MainActivity,
                                LinearLayoutManager.HORIZONTAL,
                                false
                            )
                        }

                    }
                }
            }
        }
    }

    override fun onClick(movie: Movie) {
        val intent = Intent(baseContext,PlayMovieActivity::class.java)
        intent.putExtra("url", movie.url)
        startActivity(intent)
        Toast.makeText(baseContext, movie.title, Toast.LENGTH_SHORT).show()
    }
}

