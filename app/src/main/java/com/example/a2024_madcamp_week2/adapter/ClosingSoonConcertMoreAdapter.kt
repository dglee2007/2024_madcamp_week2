package com.example.a2024_madcamp_week2.adapter

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.a2024_madcamp_week2.api.ClosingSoonConcertResponse
import com.example.a2024_madcamp_week2.databinding.ItemClosingSoonConcertMoreBinding

class ClosingSoonConcertMoreAdapter(
    private val closingSoonConcertList: List<ClosingSoonConcertResponse>,
    private val onAddEventClickListener: OnAddEventClickListener
) : RecyclerView.Adapter<ClosingSoonConcertMoreAdapter.ClosingSoonConcertMoreViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClosingSoonConcertMoreViewHolder {
        val binding = ItemClosingSoonConcertMoreBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ClosingSoonConcertMoreViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClosingSoonConcertMoreViewHolder, position: Int) {
        val closingSoonConcert = closingSoonConcertList[position]
        holder.bind(closingSoonConcert)
    }

    override fun getItemCount(): Int {
        return closingSoonConcertList.size
    }

    inner class ClosingSoonConcertMoreViewHolder(private val binding: ItemClosingSoonConcertMoreBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(closingSoonConcert: ClosingSoonConcertResponse) {
            binding.concertTitleTextView.text = closingSoonConcert.title
            binding.concertPlaceTextView.text = closingSoonConcert.place
            binding.concertDateTextView.text = closingSoonConcert.date

            Glide.with(binding.concertImageView.context)
                .load(closingSoonConcert.imageUrl)
                .into(binding.concertImageView)

            binding.registerButton.setOnClickListener {
                AlertDialog.Builder(binding.root.context)
                    .setMessage("일정 등록하시겠습니까?")
                    .setPositiveButton("확인") { dialog, _ ->
                        val dateString = closingSoonConcert.date // "2024-07-13" 형태의 문자열
                        onAddEventClickListener.onAddEventClicked(closingSoonConcert.concertId, dateString)
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