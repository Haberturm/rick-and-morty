package com.haberturm.rickandmorty.presentation.common

import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.haberturm.rickandmorty.R
import com.haberturm.rickandmorty.databinding.ErrorLayoutBinding
import com.haberturm.rickandmorty.domain.common.AppException
import com.haberturm.rickandmorty.presentation.entities.CharacterUi
import com.haberturm.rickandmorty.presentation.entities.EpisodeUi
import com.haberturm.rickandmorty.presentation.entities.LocationUi
import com.haberturm.rickandmorty.presentation.screens.charcters.CharacterListAdapter
import com.haberturm.rickandmorty.presentation.screens.charcters.CharactersFilterFragment
import com.haberturm.rickandmorty.presentation.screens.episodes.EpisodesListAdapter
import com.haberturm.rickandmorty.presentation.screens.locations.LocationListAdapter

class ListFragmentMethods {
    fun <T : RecyclerView.ViewHolder?>recyclerViewTooling(
        recyclerView: RecyclerView,
        manager: RecyclerView.LayoutManager,
        decorator: RecyclerView.ItemDecoration,
        recyclerViewAdapter: RecyclerView.Adapter<T>
    ){
        recyclerView.apply {
            layoutManager = manager
            adapter = recyclerViewAdapter
            addItemDecoration(
                decorator
            )
        }
    }

    fun swipeToRefreshListener(
        swipeRefreshLayout: SwipeRefreshLayout,
        onRefreshAction: () -> Unit
    ){
        swipeRefreshLayout.setOnRefreshListener {
            onRefreshAction()
            swipeRefreshLayout.isRefreshing = false
        }
    }


    fun <T, VH : RecyclerView.ViewHolder?>stateObserver(
        lifecycleOwner: LifecycleOwner,
        state: LiveData<UiState<T>>,
        recyclerView: RecyclerView,
        loadingIndicator: CircularProgressIndicator,
        errorView: ErrorLayoutBinding,
        errorRefreshAction: () -> Unit,
        fragmentManager: FragmentManager,
        recyclerViewAdapter: RecyclerView.Adapter<VH>,
    ){
        state.observe(lifecycleOwner, Observer { state ->
            if (state != null) {
                when (state) {
                    UiState.Loading -> {
                        recyclerView.visibility = View.INVISIBLE
                        loadingIndicator.visibility = View.VISIBLE
                        errorView.root.visibility = View.GONE
                    }
                    is UiState.Error -> {
                        loadingIndicator.visibility = View.GONE
                        if (state.exception is AppException.NoInternetConnectionException){
                            val alertDialogFragment = AlertDialogFragment()
                            alertDialogFragment.show(fragmentManager, "NO_INTERNET")
                        }else{
                            errorView.root.visibility = View.VISIBLE
                            errorView.errorRefreshButton.setOnClickListener {
                                errorRefreshAction()
                            }
                        }
                    }
                    is UiState.Data -> {
                        recyclerView.visibility = View.VISIBLE
                        loadingIndicator.visibility = View.GONE
                        errorView.root.visibility = View.GONE
                        when(recyclerViewAdapter){   //todo: generic adapter (а пока оставлю костыль:) )
                            is CharacterListAdapter -> {
                                recyclerViewAdapter.submitUpdate(state.data as List<CharacterUi>)
                            }
                            is EpisodesListAdapter -> {
                                recyclerViewAdapter.submitUpdate(state.data as List<EpisodeUi>)
                            }
                            is LocationListAdapter -> {
                                recyclerViewAdapter.submitUpdate(state.data as List<LocationUi>)
                            }
                        }
                    }
                }
            }
        })
    }

    fun setUpPagePicker(
        currentPageState: LiveData<Int>,
        lifecycleOwner: LifecycleOwner,
        previousPageButton: Button,
        nextPageButton: Button,
        jumpToPageEditText: EditText,
    ){
        currentPageState.observe(lifecycleOwner){
            
        }
    }

    fun openFiltersButtonClickListener(
        button: Button,
        navManager: FragmentManager,
        filterFragment: Fragment,
        fragmentLabel: String
    ){
        button.setOnClickListener {
            val ft = navManager.beginTransaction()
            ft.apply {
                replace(R.id.fragment_container, filterFragment)
                addToBackStack(fragmentLabel)
                commit()
            }
        }
    }
}