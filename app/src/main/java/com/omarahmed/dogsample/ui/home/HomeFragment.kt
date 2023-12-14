package com.omarahmed.dogsample.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.omarahmed.dogsample.App
import com.omarahmed.dogsample.databinding.FragmentHomeBinding
import com.omarahmed.dogsample.model.Dog
import com.omarahmed.dogsample.util.AdaptiveSpacingItemDecoration
import com.omarahmed.dogsample.util.dpToPx


class HomeFragment : Fragment(), HomeView {

    private var _binds: FragmentHomeBinding? = null
    private val binds: FragmentHomeBinding
        get() = _binds!!

    private lateinit var presenter: HomePresenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binds = FragmentHomeBinding.inflate(inflater, container, false)
        return binds.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val getAllDogsUseCase =
            (activity?.application as App).appComponent.provideGetAllDogsUseCase()

        binds.btnRetry.setOnClickListener { presenter.getDogs() }

        binds.rvDogs.layoutManager = LinearLayoutManager(context)
        binds.rvDogs.addItemDecoration(AdaptiveSpacingItemDecoration(12.dpToPx))

        presenter = HomePresenter(this, getAllDogsUseCase)
        presenter.getDogs()
    }

    override fun onDestroyView() {
        _binds = null
        presenter.cancelScope()
        super.onDestroyView()
    }


    /*** HomeView ***/

    override fun showLoading() {
        binds.progress.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        binds.progress.visibility = View.GONE
    }

    override fun showResult(data: List<Dog>) {
        binds.rvDogs.adapter = DogsAdapter(data) {
            navigateToDetails(it)
            Log.d("HomeFragment", "dog clicked $it")
        }
    }

    override fun showError(message: String) {
        binds.viewError.visibility = View.VISIBLE
        binds.tvErrorMessage.text = message
    }

    override fun hideError() {
        binds.viewError.visibility = View.GONE
    }


    /*** Private functions ***/

    private fun navigateToDetails(dog: Dog) {
        val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(dog)
        findNavController().navigate(action)
    }

}