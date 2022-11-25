package com.bilibililevel6.home.popular

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bilibililevel6.R
import com.bilibililevel6.databinding.ItemPopularListBinding
import com.bilibililevel6.home.popular.entity.Popular
import com.bilibililevel6.utils.*

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
            itemView.setOnClickListener {
                itemClick(popular)
            }
            ImageLoader.loadRoundedCornersImg(
                itemView.context, pic, viewBinding.coverImg, R.drawable.img_video_default, 15, 0
            )
            ImageLoader.loadCircleImg(
                itemView.context,
                owner.face,
                viewBinding.avatarImg,
                R.drawable.ic_user_default_avatar
            )
            viewBinding.titleText.text = title
            viewBinding.upNameText.text =
                "${owner.name} · ${ctime * 1000 dateFormatTo DateFormatMode.YEAR_MONTH_DAY}"
            viewBinding.danmakuText.text = stat.danmaku.toThousandString()
            viewBinding.playCountText.text = stat.view.toThousandString()
            viewBinding.durationText.text = duration.secondToDurationString()
        }
    }
}