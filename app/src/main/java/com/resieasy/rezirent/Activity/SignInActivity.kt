package com.resieasy.rezirent.Activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.resieasy.rezirent.Class.UsersClass
import com.resieasy.rezirent.R
import com.resieasy.rezirent.databinding.ActivitySignInBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import java.util.concurrent.TimeUnit

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    lateinit var progressDialog: AlertDialog
    private var personEmail: String? = null
    private var personPhoto: Uri? = null

    private lateinit var signInLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        // 1. Setup Google Sign-In Options
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("825775877561-fhd25aj13btnph23ojcvmf2gipgimtg7.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        // 2. Initialize ActivityResultLauncher
        signInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    account?.idToken?.let { firebaseAuthWithGoogle(it) }
                } catch (e: ApiException) {
                    Log.w("TAG", "Google sign in failed", e)//message+ detailed error
                }
            } else {
                Log.w("TAG", "Google sign in canceled or failed")
            }
        }

        // 3. Trigger Google Sign-In
        binding.googlesigninbutton.setOnClickListener {
            showLoadingDialog("Fetching Accounts ....", cancelable = true)
            signInLauncher.launch(googleSignInClient.signInIntent)
        }
    }

    // 4. Firebase Auth
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                hideLoadingDialog()
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val acct = GoogleSignIn.getLastSignedInAccount(this)
                    acct?.let {
                        personEmail = user?.email
                        personPhoto = it.photoUrl

                        FirebaseMessaging.getInstance().token.addOnCompleteListener { tokenTask ->
                            val token = tokenTask.result
                            saveUserToFirestore(token)
                        }
                    }
                } else {
                    Log.w("TAG", "signInWithCredential:failure", task.exception)
                }
            }
    }
    private fun saveUserToFirestore(token: String?) {
        val userId = auth.uid ?: return

        FirebaseFirestore.getInstance().collection("AllUser").document(userId).get()
            .addOnCompleteListener { task ->
                val document = task.result
                if (document.exists()) {
                    FirebaseFirestore.getInstance().collection("AllUser")
                        .document(userId)
                        .update(mapOf("token" to token))
                        .addOnSuccessListener { navigateToMain() }
                } else {
                    val userClass = UsersClass(
                        "", personEmail, "", "Nanded", token, userId, "", "", 7028
                    )
                    FirebaseFirestore.getInstance().collection("AllUser")
                        .document(userId)
                        .set(userClass)
                        .addOnSuccessListener { navigateToGetNameNumber() }
                }
            }
    }

    private fun navigateToMain() {
        Completable.timer(1, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                startActivity(Intent(this, MainActivity::class.java))
                finishAffinity()
            }
    }

    private fun navigateToGetNameNumber() {
        Completable.timer(1, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                startActivity(Intent(this, GetNameNumberActivity::class.java))
                finishAffinity()
            }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("personEmail", personEmail)
        outState.putParcelable("personPhoto", personPhoto)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        personEmail = savedInstanceState.getString("personEmail")
        personPhoto = savedInstanceState.getParcelable("personPhoto")
    }
    fun showLoadingDialog(message: String = "Please wait...", cancelable: Boolean = false) {
        val view = layoutInflater.inflate(R.layout.alert_dialog_sample, null)
        val messageText = view.findViewById<TextView>(R.id.progressText)
        messageText.text = message

        progressDialog = AlertDialog.Builder(this)
            .setView(view)
            .setCancelable(cancelable)
            .create()

        progressDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        progressDialog.show()
    }

    fun hideLoadingDialog() {
        if (::progressDialog.isInitialized && progressDialog.isShowing) {
            progressDialog.dismiss()
        }
    }

}


