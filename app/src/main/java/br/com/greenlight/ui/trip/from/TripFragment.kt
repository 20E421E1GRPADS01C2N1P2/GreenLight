package br.com.greenlight.ui.trip.from

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import br.com.greenlight.R
import br.com.greenlight.database.dao.TripDaoFirestore
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.trip_fragment.*

class TripFragment() : Fragment() {

    private lateinit var viewModel: TripViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        var view = inflater.inflate(R.layout.trip_fragment, container, false)
        val application = requireActivity().application
        val tripViewModelFactory = TripViewModelFactory(TripDaoFirestore(),
            application)


        viewModel = ViewModelProvider(this, tripViewModelFactory)
            .get(TripViewModel::class.java)

        viewModel.carros.observe(viewLifecycleOwner, {
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                it
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerVehicle.adapter = adapter
            spinnerVehicle.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {}

                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        val placa = it[position]
                        viewModel.vehicleSelecionadoo(placa)
                    }
                }
        })

        viewModel.status.observe(viewLifecycleOwner, Observer { status ->
            if (status)
                findNavController().popBackStack()
        })

        viewModel.msg.observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrBlank())
                Toast
                    .makeText(
                        requireContext(),
                        it,
                        Toast.LENGTH_LONG
                    ).show()
        })
        viewModel.distancia.observe(viewLifecycleOwner,{
            Log.i("distance",it.toString())
            if(!it.isNullOrEmpty()){
               editTextDistancia.setText(it)
            }
        })

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(TripViewModel::class.java)

        editTextDestino.setOnFocusChangeListener { v, hasFocus ->
            val destination =
                bundleOf("destino" to editTextDestino.text.toString())
            Log.i("destino", destination.toString())
        }

        editTextPartida.setOnFocusChangeListener { v, hasFocus ->

            viewModel.origem = listOf(editTextPartida.text.toString())
            Log.i("distance", viewModel.origem.toString())
        }
        editTextDestino.setOnFocusChangeListener { v, hasFocus ->
            viewModel.destino = listOf(editTextDestino.text.toString())
            Log.i("distance",  viewModel.destino.toString())
            viewModel.obterDistancia()

        }

        btnBuscar.setOnClickListener {
            val nomeViagem = edtTextNomeViagem.text.toString()
            val destino = editTextDestino.text.toString()
            val partida = editTextPartida.text.toString()
            val distancia = editTextDistancia.text.toString()

            when {
                nomeViagem.isNullOrBlank() -> Snackbar.make(
                    requireContext(),
                    this.requireView(),
                    "Preencha o campo Viagem corretamente",
                    Snackbar.LENGTH_LONG
                ).show()
                destino.isNullOrBlank() -> Snackbar.make(
                    requireContext(),
                    this.requireView(),
                    "Preencha o campo Destino corretamente",
                    Snackbar.LENGTH_LONG
                ).show()
                partida.isNullOrBlank() -> Snackbar.make(
                    requireContext(),
                    this.requireView(),
                    "Preencha o campo Partida corretamente",
                    Snackbar.LENGTH_LONG
                ).show()
                distancia.isNullOrBlank() -> Snackbar.make(
                    requireContext(),
                    this.requireView(),
                    "Preencha o campo Distância corretamente",
                    Snackbar.LENGTH_LONG
                ).show()
                else -> viewModel.insertTrip(
                    nomeViagem,
                    partida,
                    destino,
                    distancia
                )
            }
        }
    }


}