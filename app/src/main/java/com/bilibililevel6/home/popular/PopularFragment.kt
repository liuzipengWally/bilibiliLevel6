package com.bilibililevel6.home.popular

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bilibililevel6.BaseFragment
import com.bilibililevel6.databinding.FragmentPopularBinding
import com.bilibililevel6.extensions.onLoadMore
import com.bilibililevel6.extensions.launchObserve
import com.bilibililevel6.extensions.observeState
import com.bilibililevel6.extensions.showToast
import com.bilibililevel6.home.popular.intent.PopularListIntent
import com.bilibililevel6.home.popular.state.PopularListUiEvent
import com.bilibililevel6.play.PlayVideoActivity
import com.bilibililevel6.widget.EmptyViewType
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class PopularFragment : BaseFragment() {
    private var viewBinding: FragmentPopularBinding? = null
    private val viewModel: PopularListViewModel by lazy { ViewModelProvider(requireActivity())[PopularListViewModel::class.java] }
    private val listAdapter by lazy {
        PopularListAdapter(itemClick = {
            PlayVideoActivity.start(requireActivity())
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
        initEvent()
    }

    private fun initEvent() {
        viewBinding?.emptyView?.setOnClickListener {
            viewModel.send(PopularListIntent.FetchPopularList())
        }
    }

    private fun initView() {
        viewBinding?.popularList?.apply {
            setNumColumns(4)
            adapter = listAdapter
            onLoadMore(4) {
                viewModel.send(PopularListIntent.FetchPopularList(isLoadMore = true))
            }
        }
    }

    private fun initViewModel() = viewModel.apply {
        send(PopularListIntent.FetchPopularList())
        launchObserve {
            popularListUiState.observeState {
                it.isLoading
            }.collect {
                if (it && listAdapter.itemCount == 0) {
                    viewBinding?.loadingView?.starLoading()
                } else {
                    viewBinding?.loadingView?.endLoading()
                }
            }
        }

        launchObserve {
            popularListUiState.observeState {
                it.insertPopularList
            }.collect {
                listAdapter.addItems(it)
            }
        }

        launchObserve {
            popularListUiState.observeState {
                it.popularList
            }.collect {
                if (it.isNotEmpty()) {
                    listAdapter.updateList(it)
                }
            }
        }

        launchObserve {
            popularListUiState.observeState {
                it.isEmpty
            }.collect {
                if (it) {
                    viewBinding?.popularList?.visibility = View.GONE
                    viewBinding?.emptyView?.showEmptyView(
                        EmptyViewType.NO_DATA
                    )
                } else {
                    viewBinding?.popularList?.visibility = View.VISIBLE
                    viewBinding?.emptyView?.hideEmptyView()
                }
            }
        }

        launchObserve {
            popularListUiEvent.collect {
                when (it) {
                    is PopularListUiEvent.ShowToast -> showToast(it.message)
                    is PopularListUiEvent.ShowErrorPage -> viewBinding?.emptyView?.showEmptyView(
                        EmptyViewType.LOAD_ERROR
                    )
                }
            }
        }
    }

    companion object {
        fun newInstance() = PopularFragment()
    }
}