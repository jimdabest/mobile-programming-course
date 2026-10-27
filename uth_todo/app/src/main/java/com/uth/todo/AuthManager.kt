package com.uth.todo

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class AuthManager {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun getCurrentUser(): FirebaseUser? = auth.currentUser

    fun loginUser(email: String, pass: String, onResult: (Boolean, String) -> Unit) {
        if (email.isBlank() || pass.isBlank()) {
            onResult(false, "Vui lòng nhập email và mật khẩu")
            return
        }

        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(true, "Đăng nhập thành công")
                } else {
                    onResult(false, task.exception?.message ?: "Đăng nhập thất bại")
                }
            }
    }

    // Cấu hình yêu cầu lấy Token từ Google
    fun getGoogleSignInClient(context: Context): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(context, gso)
    }

    // Đưa Token của Google cho Firebase để xác thực
    fun firebaseAuthWithGoogle(idToken: String, onResult: (Boolean, String) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) onResult(true, "Đăng nhập Google thành công")
            else onResult(false, task.exception?.message ?: "Lỗi xác thực Firebase")
        }
    }
}

