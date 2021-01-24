package com.eselman.android.asteroidradar.list

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eselman.android.asteroidradar.R
import com.eselman.android.asteroidradar.common.viewmodel.AsteroidViewModel
import com.eselman.android.asteroidradar.databinding.FragmentAsteroidListBinding
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_asteroid_list.*
import kotlinx.android.synthetic.main.fragment_asteroid_list.view.*

class AsteroidListFragment : Fragment() {
    private val viewModel: AsteroidViewModel by activityViewModels()

    private var viewModelAdapter: AsteroidListAdapter? = null

    private lateinit var pictureOfDayImageView: ImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        val binding = FragmentAsteroidListBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        setHasOptionsMenu(true)

        viewModelAdapter = AsteroidListAdapter(AsteroidListAdapter.AsteroidClick {
            val action = AsteroidListFragmentDirections.actionShowDetail(it)
            findNavController().navigate(action)
        })

        binding.root.findViewById<RecyclerView>(R.id.asteroid_recycler).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewModelAdapter
        }

        pictureOfDayImageView = binding.root.findViewById(R.id.activity_main_image_of_the_day)

        binding.root.swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = true
            viewModel.getTodayAsteroids()
        }

        viewModel.getTodayAsteroids()
        observeViewModel()

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.show_today_menu -> viewModel.getTodayAsteroids()
            R.id.show_week_menu -> viewModel.getWeekAsteroids()
            R.id.show_all_menu -> viewModel.getAllAsteroids()
        }
        return true
    }

    private fun observeViewModel() {
        viewModel.asteroidsList.observe(viewLifecycleOwner, { asteroidsList ->
            asteroidsList?.apply {
                viewModelAdapter?.asteroidsList = this
                if(swipeRefreshLayout.isRefreshing) {
                    swipeRefreshLayout.isRefreshing = false
                }
            }
        })

        viewModel.pictureOfDay.observe(viewLifecycleOwner, {
            if (it.mediaType == "image") {
                Picasso.with(context)
                    .load(it.url)
                    .placeholder(R.drawable.placeholder_picture_of_day)
                    .error(R.drawable.placeholder_picture_of_day)
                    .into(pictureOfDayImageView)

                pictureOfDayImageView.contentDescription = it.title
            }
        })
    }
}