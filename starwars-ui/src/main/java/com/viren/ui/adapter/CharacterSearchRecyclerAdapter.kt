package com.viren.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.viren.starwars_api_bridge.model.CharacterDetail
import com.viren.starwars_ui.R
// using synthesis to refer view properties
import kotlinx.android.synthetic.main.character_search_item.view.*

/**
 * Displays search result for keyword searched by user
 */
class CharacterSearchRecyclerAdapter(
    nameSearchDiffUtils: CharacterSearchDiffUtilsCallback,
    private val clickListener: (CharacterDetail) -> Unit,
    private val loadMore: () -> Unit
) : ListAdapter<CharacterDetail, CharacterSearchRecyclerAdapter.SearchNameViewHolder>(
    nameSearchDiffUtils
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchNameViewHolder {
        return SearchNameViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.character_search_item, parent, false)
        )
    }


    override fun onBindViewHolder(holder: SearchNameViewHolder, position: Int) {
        holder.bind(getItem(position))
        if (position > itemCount - 2) loadMore.invoke()
    }

    inner class SearchNameViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        // setup recycler view item
        fun bind(characterDetail: CharacterDetail) {
            view.txtName.text = characterDetail.name
            view.setOnClickListener { clickListener(characterDetail) }
        }
    }
}
