package com.example.a2024_madcamp_week2.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.example.a2024_madcamp_week2.R

class BannerAdapter(private val context: Context, private val images: List<Int>) : PagerAdapter() {

    @SuppressLint("ResourceType")
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        // ImageView 생성
        val imageView = ImageView(context)

        // ImageView에 이미지 리소스 설정
        imageView.setImageResource(R.drawable.banner_item_1)

        // Glide를 사용하여 images[position] 이미지를 비동기적으로 로드하고 설정
        Glide.with(context)
            .load(images[position])
            .into(imageView)

        // ImageView를 컨테이너에 추가
        container.addView(imageView)

        // 생성된 ImageView 반환
        return imageView
    }

    override fun getCount(): Int {
        return images.size
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
    }
}