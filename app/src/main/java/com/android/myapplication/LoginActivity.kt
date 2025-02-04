package com.android.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.android.myapplication.DB.AppDatabase
import com.android.myapplication.DB.User
import com.android.myapplication.databinding.ActivityLoginBinding
import com.android.myapplication.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding
    private lateinit var googleSignInClient: GoogleSignInClient

    // RC_SIGN_IN은 구글 로그인 요청을 구분하기 위한 상수
    companion object {
        private const val RC_SIGN_IN = 9001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Firebase 인증 초기화
        auth = FirebaseAuth.getInstance()

        // Google Sign-In 설정
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.WEB_CLIENT_ID)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // 로그인 버튼 클릭 리스너 설정
        binding.btnSignIn.setOnClickListener {
            signInWithGoogle()
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser

        if (currentUser != null) {
            // 사용자가 이미 로그인 되어 있으면 메인 화면으로 이동
            navigateToMain()
        }
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                account?.idToken?.let { firebaseAuthWithGoogle(it) }
            } catch (e: ApiException) {
                // 로그인 실패 처리
                Log.w("GoogleSignIn", "signInResult:failed code=" + e.statusCode)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.let {
                        val email = it.email ?: ""
                        val nickname = it.displayName ?: "사용자"
                        val profileImage = it.photoUrl?.toString() ?: ""

                        // 백그라운드에서 DB 처리 실행
                        lifecycleScope.launch {
                            val db = AppDatabase.getDatabase(applicationContext)
                            val userDao = db.userDao()

                            // 기존 유저 있는지 확인
                            val existingUser = userDao.getUserByEmail(email)

                            if (existingUser == null) {
                                // 유저가 없으면 추가
                                val newUser = User( userId = 0, email = email, nickname = nickname, profileImage = profileImage)
                                userDao.insertUser(newUser)
                            }
                        }
                    }
                    navigateToMain()
                } else {
                    Log.w("GoogleSignIn", "signInWithCredential:failure", task.exception)
                }
            }
    }


    private fun navigateToMain() {
        // 메인 화면으로 이동하는 코드 추가 (예: MainActivity로 이동)
        Log.d("LoginActivity", "navigateToMain() 실행됨")
        val intent = Intent(this, MainActivity::class.java)
        setResult(RESULT_OK)
        finish() // LoginActivity 종료
    }
}