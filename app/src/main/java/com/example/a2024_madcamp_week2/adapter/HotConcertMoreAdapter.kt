package com.example.a2024_madcamp_week2.adapter

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.a2024_madcamp_week2.api.HotConcertResponse
import com.example.a2024_madcamp_week2.databinding.ItemHotConcertMoreBinding

class HotConcertMoreAdapter(
    private val hotConcertList: List<HotConcertResponse>,
    private val onAddEventClickListener: OnAddEventClickListener
) : RecyclerView.Adapter<HotConcertMoreAdapter.HotConcertMoreViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotConcertMoreViewHolder {
        val binding = ItemHotConcertMoreBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HotConcertMoreViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HotConcertMoreViewHolder, position: Int) {
        val hotConcert = hotConcertList[position]
        holder.bind(hotConcert)
    }

    override fun getItemCount(): Int {
        return hotConcertList.size
    }

    inner class HotConcertMoreViewHolder(private val binding: ItemHotConcertMoreBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(hotConcert: HotConcertResponse) {
            binding.concertTitleTextView.text = hotConcert.title
            binding.concertPlaceTextView.text = hotConcert.place
            binding.concertDateTextView.text = hotConcert.date

            Glide.with(binding.concertImageView.context)
                .load(hotConcert.imageUrl)
                .into(binding.concertImageView)

            binding.registerButton.setOnClickListener {
                AlertDialog.Builder(binding.root.context)
                    .setMessage("일정 등록하시겠습니까?")
                    .setPositiveButton("확인") { dialog, _ ->
                        val dateString = hotConcert.date // "2024-07-13" 형태의 문자열
                        onAddEventClickListener.onAddEventClicked(hotConcert.concertId, dateString)
                        dialog.dismiss()
                    }
                    .setNegativeButton("취소") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
        }
    }

    interface OnAddEventClickListener {
        fun onAddEventClicked(concertId: Int, dateString: String)
    }
}