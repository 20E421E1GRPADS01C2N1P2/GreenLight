package br.com.greenlight.ui.trip.list

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.greenlight.R

class ListTripFragment : Fragment() {

    companion object {
        fun newInstance() = ListTripFragment()
    }

    private lateinit var viewModel: ListTripViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.list_trip_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ListTripViewModel::class.java)
        // TODO: Use the ViewModel
    }

}