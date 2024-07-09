package com.example.a2024_madcamp_week2

import android.Manifest.permission.CAMERA
import android.app.Activity
import android.content.ClipData
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.a2024_madcamp_week2.api.ApiClient
import com.example.a2024_madcamp_week2.api.ReviewService
import com.example.a2024_madcamp_week2.databinding.ActivityReviewCreateBinding
import com.example.a2024_madcamp_week2.utility.getUserIdFromSharedPreferences
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import com.example.a2024_madcamp_week2.ui.notifications.NotificationsFragment



class ReviewCreateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReviewCreateBinding
    lateinit var filePath: String
    private lateinit var resultLauncher : ActivityResultLauncher<Intent>
    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 123 // 원하는 요청 코드
    }
    private var imageUrl: Uri? = null
    private lateinit var photoURI: Uri

    // 이전 액티비티에서 카메라를 선택할 때
    private val REQUEST_CAMERA = 1

    // 이전 액티비티에서 갤러리를 선택할 때
    private val REQUEST_GALLERY = 2


    override fun onStart() {
        super.onStart()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviewCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                Log.d("INFO", "RESULT_OK")
                val option = BitmapFactory.Options().apply {
                    inSampleSize = 10
                }
                val bitmap = BitmapFactory.decodeFile(filePath, option)
                // 이미지뷰 등에 비트맵 설정
            } else {
                Log.d("INFO", "불러오기 실패")
            }
        }

        binding.buttonCamera.setOnClickListener {
            getImageFromCamera()
        }

        binding.buttonGallery.setOnClickListener {
            getImageFromGallery()
        }

        //요청하기 버튼
        binding.buttonSubmitReview.setOnClickListener {
            postReportFun()
            Toast.makeText(applicationContext, "요청이 접수되었습니다", Toast.LENGTH_SHORT).show()
            finish()
        }

        // 뒤로가기 버튼
        binding.buttonBack.setOnClickListener {
            // NotificationsFragment로 전환
            //replaceFragment(NotificationsFragment())
            finish()
        }
    }

    private fun getImageFromCamera(){

        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val file = File.createTempFile(
            "JPEG_${timeStamp}_", ".jpg", storageDir
        )
        filePath = file.absolutePath
        photoURI = createImageFile()

        if (ContextCompat.checkSelfPermission(this, CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // 권한이 없을 경우 권한 요청
            ActivityCompat.requestPermissions(this, arrayOf(CAMERA), REQUEST_CAMERA_PERMISSION)
        } else {
            //  권한이 이미 부여되었을 경우 카메라 앱 호출 등 필요한 작업 수행
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            // resultLauncher.launch(cameraIntent)
            startActivityForResult(cameraIntent, REQUEST_CAMERA)
        }
    }

    // 권한 요청 결과 처리
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 권한이 부여된 경우 필요한 작업 수행
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                resultLauncher.launch(intent)
            } else {
                // 권한이 거부된 경우 사용자에게 알림 표시 또는 다른 대체 작업 수행
            }
        }
    }

    private fun createImageFile(): Uri {
        val now = SimpleDateFormat("yyMMdd_HHmmss").format(Date())
        val content = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "img_$now.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
        }
        return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, content)!!
    }

    private fun getImageFromGallery(){
        //갤러리에서 이미지 가져오기
        val galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent.type = "image/*"
        galleryIntent.putExtra(Intent.EXTRA_AUTO_LAUNCH_SINGLE_CHOICE, true);
        startActivityForResult(galleryIntent, REQUEST_GALLERY)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_CAMERA -> run {
                imageUrl = photoURI
            }
            REQUEST_GALLERY -> {
                if (resultCode == RESULT_OK) {
                    if(data == null){   // 어떤 이미지도 선택하지 않은 경우
                        Toast.makeText(this, "이미지를 선택하지 않았습니다.", Toast.LENGTH_LONG).show();
                    }
                    else{
                        if(data.clipData == null){     //이미지 1개 선택
                            Log.e("single choice: ", data.data.toString());
                            imageUrl = data.data
                            binding.imageViewReview.setImageURI(imageUrl)

                        }
                        else{      // 이미지를 여러장 선택한 경우
                            var clipData : ClipData = data.clipData!!;

                            if(clipData.itemCount > 10){
                                Toast.makeText(this, "사진은 10장까지 선택 가능합니다.", Toast.LENGTH_LONG).show();
                            }
                            else{   // 선택한 이미지가 1장 이상 10장 이하인 경우
                                Log.e(ContentValues.TAG, "multiple choice");

                                for (i in 0 until clipData.itemCount) {
                                    imageUrl = clipData.getItemAt(i).uri
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun postReportFun() {
        val apiService = ApiClient.create(ReviewService::class.java)

        // 이미지 파일 생성
        val filePath = getRealPathFromUri(this, imageUrl)
        Log.d("파일경로", filePath.toString())
        val file = File(filePath)
        val requestFile = file.asRequestBody("image/*".toMediaType())
        val imagePart = MultipartBody.Part.createFormData("image", file.name, requestFile)

        // 네트워크 요청 및 응답 처리
        val call = apiService.postReview(
            image = imagePart,
            title = binding.editTextReviewTitle.text.toString(),
            content = binding.editTextReviewContent.text.toString(),
            rating = 5,
            userId = getUserIdFromSharedPreferences(applicationContext)
        )
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    val responseData = response.body()
                    Log.d("리뷰 등록 성공: ", responseData.toString())
                    val intent = Intent()
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("리뷰 등록 실패: ", errorBody.toString())
                    setResult(Activity.RESULT_CANCELED)
                    finish()
                }
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("TAG", "실패원인: $t")
                setResult(Activity.RESULT_CANCELED)
                finish()
            }
        })
    }

    fun getRealPathFromUri(context: Context, uri: Uri?): String? {
        var filePath: String? = null
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(uri!!, projection, null, null, null)
        cursor?.use {
            val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            it.moveToFirst()
            filePath = it.getString(columnIndex)
        }
        return filePath
    }

    // 터치 이벤트를 통해 키보드 숨기기
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val view = currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }

    // 프래그먼트 교체 함수
    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}