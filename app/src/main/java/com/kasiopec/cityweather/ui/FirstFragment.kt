package com.kasiopec.cityweather.ui

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.kasiopec.cityweather.Contract
import com.kasiopec.cityweather.R
import com.kasiopec.cityweather.database.DatabaseEntities
import com.kasiopec.cityweather.model.MainFragmentViewModel
import com.kasiopec.cityweather.presenter.MainFragmentPresenter

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment(),
    Contract.MainFragmentView, OnItemClickListener {
    lateinit var recyclerView: RecyclerView
    lateinit var recyclerAdapter: WeatherListAdapter

    lateinit var viewModel: MainFragmentViewModel
    lateinit var cityToDelete : DatabaseEntities.CityWeather


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
        viewModel.cityWeatherList.observe(
            viewLifecycleOwner,
            Observer<List<DatabaseEntities.CityWeather>> {
                recyclerAdapter.items = it
                recyclerAdapter.notifyDataSetChanged()
            })


    }

    override fun renderCityList() {
        recyclerAdapter.notifyDataSetChanged()
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
       if(requestCode == 1){
           if(resultCode == 1){
               viewModel.deleteWeatherFromRepository(cityToDelete)
               Toast.makeText(activity,cityToDelete.cityName + " was deleted.",Toast.LENGTH_SHORT).show()
           }
       }
    }





}

