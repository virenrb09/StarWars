package com.viren.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.viren.starwars_api_bridge.model.MoviesDetailsResponse
import com.viren.starwars_ui.R
import kotlinx.android.synthetic.main.fragment_character_search.view.*
import kotlinx.android.synthetic.main.fragment_character_search.view.txtTitle
import kotlinx.android.synthetic.main.movies_item.view.*

/**
 * Displays all movies of a character in details view
 */
class MoviesRecyclerAdapter(private val list: List<MoviesDetailsResponse>) :
    RecyclerView.Adapter<MoviesRecyclerAdapter.MoviesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movies_item, parent, false)
        return MoviesViewHolder(view)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        holder.bind(list[position])
    }

    /**
     * ViewHolder of the RecyclerView
     */
    class MoviesViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        /**
         * Sets TextViews
         */
        fun bind(speciesDetailsResponse: MoviesDetailsResponse) {
            view.txtTitle.text = speciesDetailsResponse.title
            view.txtReleaseDate.text = view.context.getString(
                R.string.movie_release_date_is,
                speciesDetailsResponse.release_date
            )
            view.txtOpening.text = view.context.getString(
                R.string.movie_opening_is,
                speciesDetailsResponse.opening_crawl
            )
        }
    }
}
