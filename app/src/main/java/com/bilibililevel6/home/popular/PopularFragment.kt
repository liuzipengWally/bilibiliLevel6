package com.bilibililevel6.home.popular

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bilibililevel6.BaseFragment
import com.bilibililevel6.databinding.FragmentPopularBinding
import com.bilibililevel6.extensions.safeObserver
import com.bilibililevel6.extensions.showToast
import com.bilibililevel6.home.popular.intent.PopularListIntent
import com.bilibililevel6.home.popular.state.PopularListUiEvent
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

class PopularFragment : BaseFragment() {
    private var viewBinding: FragmentPopularBinding? = null
    private val viewModel: PopularListViewModel by lazy { ViewModelProvider(requireActivity())[PopularListViewModel::class.java] }
    private val listAdapter by lazy {
        PopularListAdapter(itemClick = {

        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentPopularBinding.inflate(inflater, container, false)
        return viewBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initView()
    }

    private fun initView() {
        viewBinding?.popularList?.apply {
            layoutManager = GridLayoutManager(requireActivity(), 4)
            adapter = listAdapter
        }
    }

    private fun initViewModel() {
        viewModel.send(PopularListIntent.FetchPopularList())
        safeObserver(Lifecycle.State.STARTED) {
            viewModel.popularListUiState.map {
                it.isLoading
            }.distinctUntilChanged().collect {
                Log.i("日志", "isLoading：${it}")
            }
        }

        safeObserver(Lifecycle.State.STARTED) {
            viewModel.popularListUiState.map {
                it.popularData
            }.distinctUntilChanged().filter { it != null }.collect {
                Log.i("日志", "popularData")
                listAdapter.addItems(it?.list!!)
            }
        }

        safeObserver(Lifecycle.State.STARTED) {
            viewModel.popularListUiState.map {
                it.loadMorePopularData
            }.distinctUntilChanged().filter { it != null }.collect {
                Log.i("日志", "loadMorePopularData")
                listAdapter.updateList(it?.list!!)
            }
        }

        safeObserver {
            viewModel.popularListUiEvent.collect {
                when (it) {
                    is PopularListUiEvent.ShowToast -> showToast(it.message)
                    is PopularListUiEvent.ShowErrorPage -> Log.i("日志", "error:${it.message}")
                    else -> {}
                }
            }
        }
    }

    companion object {
        fun newInstance() = PopularFragment()
    }
}