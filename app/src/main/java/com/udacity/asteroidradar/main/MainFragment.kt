package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    private lateinit var  viewModel: MainViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
         binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this


        val viewModelFactory = MainViewModelFactory(requireActivity().application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        binding.viewModel = viewModel

        val adapter = MainFragmentAdapter(AsteroidClickListener { asteroid ->
            findNavController().navigate(MainFragmentDirections.actionShowDetail(asteroid))
        })
        binding.asteroidRecycler.adapter = adapter
        setHasOptionsMenu(true)
        binding.statusLoadingWheel.visibility = View.VISIBLE
        binding.asteroidRecycler.visibility = View.GONE

        viewModel.asteroidListFromData.observe(viewLifecycleOwner, Observer { asteroids ->
            asteroids?.let {
                adapter.submitList(asteroids)
                binding.asteroidRecycler.visibility = View.VISIBLE
                binding.statusLoadingWheel.visibility = View.GONE
            }
        })

        viewModel.pictureOfTheDayFromData.observe(viewLifecycleOwner, Observer { picture ->
            picture?.let {
                if (picture.mediaType == "image") {
                    setPictureOfDay(picture)
                }
            }
        })


        return binding.root


    }

    private fun setPictureOfDay(pictureOfDay: PictureOfDay) {
        Picasso.with(binding.activityMainImageOfTheDay.context)
            .load(pictureOfDay.url)
            .placeholder(R.drawable.loading_animation)
            .error(R.drawable.broken_image)
            .into(binding.activityMainImageOfTheDay)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }
}
