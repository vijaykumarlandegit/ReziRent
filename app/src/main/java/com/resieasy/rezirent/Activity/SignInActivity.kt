package com.resieasy.rezirent.Activity

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.resieasy.rezirent.Class.UsersClass
import com.resieasy.rezirent.databinding.ActivitySignInBinding

class SignInActivity : AppCompatActivity() {
    var binding: ActivitySignInBinding? = null
    var auth: FirebaseAuth? = null
    var mAuth: FirebaseAuth? = null
    var RC_SIGN_IN: Int = 100
    var personName: String? = null
    var personEmail: String? = null
    var personalNumber: String? = null
    var personPhoto: Uri? = null
    var image1: String? = null

    var progressDialog: ProgressDialog? = null
    var progressDialog2: ProgressDialog? = null
    var progressDialog3: ProgressDialog? = null

    var googleSignInClient: GoogleSignInClient? = null
    var sreference: StorageReference? = null
    var storage: FirebaseStorage? = null
    var database: FirebaseDatabase? = null
    var dreference: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        auth = FirebaseAuth.getInstance()
        mAuth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        database = FirebaseDatabase.getInstance()

        progressDialog = ProgressDialog(this)
        progressDialog!!.setTitle("Creating Account")
        progressDialog!!.setMessage("Please wait, we are creating your account for ResiEasy .....")
        progressDialog!!.setCancelable(false)
        progressDialog2 = ProgressDialog(this)
        progressDialog2!!.setTitle("Fetching Your Account .....")
        progressDialog2!!.setCancelable(false)
        progressDialog3 = ProgressDialog(this)
        progressDialog3!!.setTitle("Please wait .....")
        progressDialog3!!.setCancelable(false)

        image1 =
            "https://upload.wikimedia.org/wikipedia/commons/7/7c/Profile_avatar_placeholder_large.png"


        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("825775877561-fhd25aj13btnph23ojcvmf2gipgimtg7.apps.googleusercontent.com")
            .requestEmail()
            .build()

        // Initialize sign in client
        googleSignInClient = GoogleSignIn.getClient(this@SignInActivity, googleSignInOptions)


        binding!!.googlesigninbutton.setOnClickListener {
            progressDialog2!!.show()
            val signInIntent = googleSignInClient!!.signInIntent
            startActivityForResult(signInIntent, 123)
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 123) {
            progressDialog2!!.dismiss()

            progressDialog!!.show()

            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                Log.d("TAG", "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken)
            } catch (e: ApiException) {
                progressDialog2!!.dismiss()
                Log.w("TAG", "Google sign in failed", e)
            }
        }
    }


    private fun firebaseAuthWithGoogle(idToken: String?) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth!!.signInWithCredential(credential)
            .addOnCompleteListener(
                this
            ) { task ->
                if (task.isSuccessful) {
                    Log.d("TAG", "signInWithCredential:success")
                    val user = auth!!.currentUser
                    val acct =
                        GoogleSignIn.getLastSignedInAccount(this@SignInActivity)
                    if (acct != null) {
                        personName = user!!.displayName
                        personEmail = user.email
                        personalNumber = user.phoneNumber
                        personPhoto = acct.photoUrl


                        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                            val tokenn = task.result
                            FirebaseFirestore.getInstance().collection("AllUser").document(
                                FirebaseAuth.getInstance().uid!!
                            )
                                .get().addOnCompleteListener { task ->
                                    val document = task.result
                                    if (document.exists()) {
                                        val hashMap =
                                            HashMap<String, Any?>()
                                        hashMap["token"] = tokenn
                                        FirebaseFirestore.getInstance().collection("AllUser")
                                            .document(
                                                FirebaseAuth.getInstance().uid!!
                                            )
                                            .update(hashMap).addOnSuccessListener {
                                                val intent =
                                                    Intent(
                                                        this@SignInActivity,
                                                        MainActivity::class.java
                                                    )
                                                progressDialog!!.dismiss()
                                                Toast.makeText(
                                                    this@SignInActivity,
                                                    "Sign-In Successfully, Welcome To ResiEasy",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                                startActivity(intent)
                                                finishAffinity()
                                            }
                                    } else {
                                        val userClass = UsersClass(
                                            "",
                                            personEmail,
                                            "",
                                            "Nanded",
                                            tokenn,
                                            FirebaseAuth.getInstance().uid,
                                            "",
                                            "",
                                            7028
                                        )

                                        FirebaseFirestore.getInstance().collection("AllUser")
                                            .document(
                                                FirebaseAuth.getInstance().uid!!
                                            )
                                            .set(userClass).addOnSuccessListener {
                                                progressDialog!!.dismiss()
                                                Toast.makeText(
                                                    this@SignInActivity,
                                                    "Your Account Is Created",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                                Toast.makeText(
                                                    this@SignInActivity,
                                                    "Welcome To ResiEasy",
                                                    Toast.LENGTH_LONG
                                                ).show()

                                                val intent =
                                                    Intent(
                                                        this@SignInActivity,
                                                        GetNameNumberActivity::class.java
                                                    )
                                                startActivity(intent)
                                                finishAffinity()
                                            }
                                    }
                                }
                        }
                    }
                } else {
                    progressDialog!!.dismiss()

                    Log.w("TAG", "signInWithCredential:failure", task.exception)
                }
            }
    }
}


