package com.eselman.android.asteroidradar.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.eselman.android.asteroidradar.R
import com.eselman.android.asteroidradar.common.model.Asteroid
import com.eselman.android.asteroidradar.databinding.AsteroidItemBinding

class AsteroidListAdapter(private val asteroidClick: AsteroidClick): RecyclerView.Adapter<AsteroidListAdapter.AsteroidViewHolder>() {
    var asteroidsList: List<Asteroid> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class AsteroidViewHolder(val viewDataBinding: AsteroidItemBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.asteroid_item
        }
    }

    class AsteroidClick(val block: (Asteroid) -> Unit) {
        fun onClick(asteroid: Asteroid) = block(asteroid)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):AsteroidViewHolder {
        val withDataBinding: AsteroidItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            AsteroidViewHolder.LAYOUT,
            parent,
            false)
        return AsteroidViewHolder(withDataBinding)
    }

    override fun getItemCount() = asteroidsList.size

     override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.asteroid = asteroidsList[position]
            it.asteroidClick = asteroidClick
        }
    }
}