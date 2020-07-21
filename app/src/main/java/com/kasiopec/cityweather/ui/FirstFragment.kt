package com.kasiopec.cityweather.ui

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kasiopec.cityweather.R
import com.kasiopec.cityweather.database.DatabaseEntities
import com.kasiopec.cityweather.model.MainFragmentViewModel
import kotlinx.android.synthetic.main.fragment_first.*
import java.util.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment(), OnItemClickListener {
    lateinit var recyclerView: RecyclerView
    lateinit var recyclerAdapter: WeatherListAdapter

    lateinit var viewModel: MainFragmentViewModel
    lateinit var cityToDelete: DatabaseEntities.CityWeather
    lateinit var cityWeatherList: List<DatabaseEntities.CityWeather>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_first, container, false)
        viewModel = ViewModelProvider(this).get(MainFragmentViewModel::class.java)
        recyclerView = view.findViewById(R.id.weatherRecycler)
        recyclerView.layoutManager = LinearLayoutManager(activity)

        recyclerAdapter = WeatherListAdapter(requireActivity(), this)
        recyclerView.adapter = recyclerAdapter

        // Inflate the layout for this fragment
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Objects.requireNonNull(activity as MainActivity).hideFab(false)
        viewModel.cityWeatherList.observe(
            viewLifecycleOwner,
            Observer<List<DatabaseEntities.CityWeather>> {
                cityWeatherList = it
                recyclerAdapter.items = it
                recyclerAdapter.notifyDataSetChanged()
            }
        )
        swipeContainer.setOnRefreshListener {
            viewModel.updateCitiesWeather(cityWeatherList)
            viewModel.isNetworkError.observe(viewLifecycleOwner, Observer<Boolean>{isNetworkError ->
                if(isNetworkError) notifyOnError()
            })
            swipeContainer.isRefreshing = false
        }
    }


    override fun onItemClicked(item: DatabaseEntities.CityWeather) {
        val secondFragment = SecondFragment()
        val bundle = Bundle()
        bundle.putSerializable("item", item)
        secondFragment.arguments = bundle
        val manager = parentFragmentManager
        val fragTransaction = manager.beginTransaction()
        //fragTransaction.replace(R.id.nav_host_fragment, secondFragment)
        fragTransaction.setCustomAnimations(
            R.anim.enter_from_right, R.anim.exit_to_right,
            R.anim.enter_from_right, R.anim.exit_to_right
        )
        fragTransaction.addToBackStack(null)
        fragTransaction.replace(R.id.nav_host_fragment, secondFragment).commit()
        println(bundle)
//findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
    }

    override fun onDeleteClicked(item: DatabaseEntities.CityWeather) {
        val deleteDialog = DeleteDialog()
        deleteDialog.setTargetFragment(this, 1)
        cityToDelete = item
        deleteDialog.show(parentFragmentManager, "deleteDialog")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1) {
            if (resultCode == 1) {
                viewModel.deleteWeatherFromRepository(cityToDelete)
                Toast.makeText(
                    activity,
                    cityToDelete.cityName + " was deleted.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun notifyOnError(){
        if(!viewModel.isNetworkErrorShown.value!!){
            Toast.makeText(requireContext(), viewModel.networkErrorMessage.value, Toast.LENGTH_SHORT).show()
            viewModel.networkErrorShown()
        }
    }


}

