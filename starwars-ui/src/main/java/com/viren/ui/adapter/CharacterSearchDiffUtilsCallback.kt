package com.viren.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.viren.starwars_api_bridge.model.CharacterDetail

/**
 * Determines if there is a difference between 2 items or not in the recyclerView
 */
class CharacterSearchDiffUtilsCallback : DiffUtil.ItemCallback<CharacterDetail>() {

    override fun areItemsTheSame(oldItem: CharacterDetail, newItem: CharacterDetail) =
        oldItem.name == newItem.name

    override fun areContentsTheSame(oldItem: CharacterDetail, newItem: CharacterDetail) =
        oldItem.name == newItem.name
}
