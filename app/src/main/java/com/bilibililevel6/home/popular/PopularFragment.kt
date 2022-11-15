package com.bilibililevel6.home.popular

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bilibililevel6.BaseFragment
import com.bilibililevel6.R
import com.bilibililevel6.databinding.FragmentPopularBinding

class PopularFragment : BaseFragment() {
    private val viewBinding: FragmentPopularBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_popular, container, false)
    }
}