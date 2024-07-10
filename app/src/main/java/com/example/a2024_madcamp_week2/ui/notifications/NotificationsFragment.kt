package com.example.a2024_madcamp_week2.ui.notifications

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a2024_madcamp_week2.ReviewCreateActivity
import com.example.a2024_madcamp_week2.adapter.ReviewAdapter
import com.example.a2024_madcamp_week2.api.ApiClient
import com.example.a2024_madcamp_week2.api.ReviewResponse
import com.example.a2024_madcamp_week2.api.ReviewService
import com.example.a2024_madcamp_week2.databinding.FragmentNotificationsBinding
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var reviewList: ArrayList<ReviewResponse>
    private lateinit var reviewAdapter: ReviewAdapter
    private lateinit var createReviewLauncher: ActivityResultLauncher<Intent>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        reviewList = ArrayList() // reviewList 초기화

//        notificationsViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }

        createReviewLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            Log.d("INFO", "ActivityResult callback 호출, resultCode: ${result.resultCode}")
            // resultCode와 상관없이 새로고침
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                getAllReviews()
                reviewAdapter.notifyDataSetChanged()
            }
        }

        binding.btnCreateReview.setOnClickListener {
            val intent = Intent(requireContext(), ReviewCreateActivity::class.java)
            createReviewLauncher.launch(intent)
        }

        binding.btnSearch.setOnClickListener {
            val searchText = binding.etSearch.text.toString().trim()
            if (searchText.isNotEmpty()) {
                viewLifecycleOwner.lifecycleScope.launch {
                    searchReviews(searchText)
                }
            } else {
                Toast.makeText(requireContext(), "검색어를 입력하세요", Toast.LENGTH_SHORT).show()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            getAllReviews()
            setupRecyclerView() // getAllReviews가 완료된 후에 RecyclerView 설정
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private suspend fun getAllReviews() {
        withContext(Dispatchers.IO) {
            // var email=binding.etSignupEmailleft.text.toString()+binding.etSignupEmailright.text.toString()
            val response = ReviewService.retrofitGetAllReviews().execute()
            if (response.isSuccessful) {
                val reviews = response.body()
                if (reviews != null) {
                    reviewList.clear()  // Clear the list to avoid duplicates
                    for (review in reviews) {
                        val imgPart = ApiClient.BASE_URL + review.image.replace("\\", "/")
                        val title = review.title
                        val content = review.content
                        val rating = review.rating
                        val createdAt = review.createdAt
                        val name = review.name
                        reviewList.add(ReviewResponse(image=imgPart, title=title, content=content, rating=rating, createdAt=createdAt, name=name))
                    }
                } else {
                    val body = response.errorBody()?.string()
                    Log.e(ContentValues.TAG, "body : $body")
                }
            }
        }
    }

    private fun setupRecyclerView() {
        reviewAdapter = ReviewAdapter(reviewList)
        Log.d("reviewList", reviewList.toString())
        // reviewAdapter.setOnItemClickListener(object : ReportImageRVAdapter.OnItemClickListener {
        //    override fun onItemClick(pos: Int) {
        //    }
        // })

        val reviewImageLinearLayoutManager = LinearLayoutManager(requireContext())
        reviewImageLinearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvReview.layoutManager = reviewImageLinearLayoutManager
        binding.rvReview.adapter = reviewAdapter
    }

    private suspend fun searchReviews(keyword: String) {
        withContext(Dispatchers.IO) {
            try {
                val response = ReviewService.retrofitGetReviewsByKeyword(keyword).execute()
                if (response.isSuccessful) {
                    val reviews = response.body()
                    if (reviews != null) {
                        reviewList.clear()
                        for (review in reviews) {
                            val imgPart = ApiClient.BASE_URL + review.image.replace("\\", "/")
                            val title = review.title
                            val content = review.content
                            val rating = review.rating
                            val createdAt = review.createdAt
                            val name = review.name
                            reviewList.add(ReviewResponse(image = imgPart, title = title, content = content, rating = rating, createdAt = createdAt, name = name))
                        }
                    }
                    withContext(Dispatchers.Main) {
                        reviewAdapter.notifyDataSetChanged()
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("NotificationsFragment", "Error: $errorBody")
                }
            } catch (e: Exception) {
                Log.e("NotificationsFragment", "Exception: ${e.message}")
            }
        }
    }

}