package com.bilibililevel6.home.popular

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bilibililevel6.R
import com.bilibililevel6.databinding.ItemPopularListBinding
import com.bilibililevel6.home.popular.entity.Popular
import com.bilibililevel6.utils.DateFormatMode
import com.bilibililevel6.utils.ImageLoader
import com.bilibililevel6.utils.dateFormatTo

/**
 * author：liuzipeng
 * time: 2022/11/20 22:47
 */
class PopularListAdapter(val itemClick: (popular: Popular) -> Unit) :
    RecyclerView.Adapter<PopularListAdapter.ViewHolder>() {
    private val popularList = mutableListOf<Popular>()

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(list: List<Popular>) {
        if (popularList.isNotEmpty()) popularList.clear()
        popularList.addAll(list)
        notifyDataSetChanged()
    }

    fun addItems(list: List<Popular>) {
        val latestIndex = itemCount
        popularList.addAll(list)
        notifyItemInserted(latestIndex)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemPopularListBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(popularList[position])
    }

    override fun getItemCount(): Int = popularList.size

    inner class ViewHolder(private val viewBinding: ItemPopularListBinding) :
        RecyclerView.ViewHolder(
            viewBinding.root
        ) {
        @SuppressLint("SetTextI18n")
        fun bind(popular: Popular) = with(popular) {
            ImageLoader.loadRoundedCornersImg(
                itemView.context, pic, viewBinding.coverImg, R.drawable.bili_default_image_tv, 15, 0
            )
            ImageLoader.loadCircleImg(
                itemView.context,
                owner.face,
                viewBinding.avatarImg,
                R.drawable.bili_default_image_tv
            )
            viewBinding.titleText.text = title
            viewBinding.upNameText.text =
                "${owner.name} · ${ctime dateFormatTo DateFormatMode.YEAR_MONTH_DAY}"
            viewBinding.bulletCountText.text = stat.danmaku.toString()
            viewBinding.playCountText.text = stat.view.toString()
            viewBinding.durationText.text = duration dateFormatTo DateFormatMode.DURATION
        }
    }
}