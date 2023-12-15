package com.omarahmed.dogsample.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.omarahmed.dogsample.App
import com.omarahmed.dogsample.R
import com.omarahmed.dogsample.databinding.FragmentHomeBinding
import com.omarahmed.dogsample.model.Dog
import com.omarahmed.dogsample.model.ListType
import com.omarahmed.dogsample.util.AdaptiveSpacingItemDecoration
import com.omarahmed.dogsample.util.dpToPx


class HomeFragment : Fragment(), HomeView {

    private var listType: ListType = ListType.LIST

    private var _binds: FragmentHomeBinding? = null
    private val binds: FragmentHomeBinding
        get() = _binds!!

    private lateinit var presenter: HomePresenter
    private val dogsAdapter = DogsAdapter(listType) {
        navigateToDetails(it)
        Log.d("HomeFragment", "dog clicked $it")
    }

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
        binds.btnChangeListType.setOnClickListener { onChangeListTypeClicked() }

        binds.rvDogs.layoutManager = GridLayoutManager(context, 1)
        binds.rvDogs.addItemDecoration(AdaptiveSpacingItemDecoration(12.dpToPx))
        binds.rvDogs.adapter = dogsAdapter
        updateListView()

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
        dogsAdapter.updateData(data)
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


    private fun onChangeListTypeClicked() {
        listType = when (listType) {
            ListType.LIST -> ListType.GIRD
            ListType.GIRD -> ListType.LIST
        }
        updateListView()
    }

    private fun updateListView() {
        when (listType) {
            ListType.LIST -> {
                binds.btnChangeListType.setImageResource(R.drawable.ic_grid_view)
                (binds.rvDogs.layoutManager as? GridLayoutManager)?.spanCount = 1
            }

            ListType.GIRD -> {
                binds.btnChangeListType.setImageResource(R.drawable.ic_list_view)
                (binds.rvDogs.layoutManager as? GridLayoutManager)?.spanCount = 2
            }
        }
        dogsAdapter.updateListType(listType)
    }

}