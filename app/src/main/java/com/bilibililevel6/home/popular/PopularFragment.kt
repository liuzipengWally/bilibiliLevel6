package com.bilibililevel6.home.popular

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bilibililevel6.BaseFragment
import com.bilibililevel6.databinding.FragmentPopularBinding
import com.bilibililevel6.extensions.safeObserver
import com.bilibililevel6.home.popular.intent.PopularListIntent
import com.bilibililevel6.home.popular.state.PopularListUiEvent
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

class PopularFragment : BaseFragment() {
    private var viewBinding: FragmentPopularBinding? = null
    private val viewModel: PopularListViewModel by lazy { ViewModelProvider(requireActivity())[PopularListViewModel::class.java] }

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
    }

    private fun initViewModel() {
        viewModel.send(PopularListIntent.FetchPopularList())
        safeObserver {
            viewModel.popularListUiState.map {
                it.isLoading
            }.distinctUntilChanged().collect {
                Log.i("日志", "isLoading：${it}")
            }
        }

        safeObserver {
            viewModel.popularListUiState.map {
                it.popularData
            }.filter { it != null }
                .distinctUntilChanged().collect {
                    Log.i("日志", "更新数据：${it?.list?.get(0)}")
                }
        }

        safeObserver {
            viewModel.popularListUiEvent.collect {
                when (it) {
                    is PopularListUiEvent.ShowToast -> Log.i("日志", "toast:${it.message}")
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