package com.tchnodynamic.micmac

import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tchnodynamic.herbalpoint.Models.Users
import com.tchnodynamic.micmac.Admin.LoginActivityAdmin
import com.tchnodynamic.micmac.Seller.LoginActivitySeller
import com.tchnodynamic.micmac.databinding.ActivityMainBinding
import io.paperdb.Paper
import java.util.*
import kotlin.system.exitProcess

class LoginUser : AppCompatActivity() {
    val mAuthBuyers= FirebaseAuth.getInstance()
    var mGoogleSignInClient: GoogleSignInClient? = null
    var database = FirebaseDatabase.getInstance()
    var progressDialog: ProgressDialog? = null


    private lateinit var binding: ActivityMainBinding
    var usertype: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val networkConnection = NetworkConnection(applicationContext)
        networkConnection.observe(this, Observer { isConnected ->
            if (isConnected) {
                binding.layoutcoonected1.visibility = View.VISIBLE
                binding.layoutnotcoonected1.visibility = View.GONE
            } else {
                binding.layoutcoonected1.visibility = View.GONE
                binding.layoutnotcoonected1.visibility = View.VISIBLE

            }


        })

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = resources.getColor(R.color.colorPrimary, this.theme)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(applicationContext, R.color.colorPrimary)
        }






        usertype = intent.getStringExtra("category").toString()

        progressDialog = ProgressDialog(this)



        binding.btnAdmin.setOnClickListener {
            usertype = "Admin"
            val intent = Intent(applicationContext, LoginActivityAdmin::class.java)
            startActivity(intent)
            finish()
        }



            binding.btnSeller.setOnClickListener {
                usertype = "Seller"
                val intent = Intent(applicationContext, LoginActivitySeller::class.java)
                startActivity(intent)
                finish()


            }

            binding.btnGoogle.setOnClickListener {
                usertype = "Buyer"
                loginwithgooogle()

            }

            Paper.init(this)
        }






    private fun loginwithgooogle() {


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        progressDialog!!.setTitle("Login")
        progressDialog!!.setMessage("Login to your Google account")
        progressDialog!!.setCanceledOnTouchOutside(false)



        progressDialog?.show()

        signIn()

    }

    var RC_SIGN_IN = 65
    private fun signIn() {
        val signInIntent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
//        progressDialog!!.show()


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                Log.d("TAG", "firebaseAuthWithGoogle:" + account!!.id)
                account.idToken?.let { firebaseAuthWithGoogle(it) }
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e)
                // ...
            }
        }
    }


    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuthBuyers.signInWithCredential(credential)
            .addOnCompleteListener(this,
                OnCompleteListener<AuthResult?> { task ->
                    if (task.isSuccessful) {


                        val user: FirebaseUser? = mAuthBuyers.currentUser


                        // Sign in success, update UI with the signed-in user's information
                        Log.d("TAG", "signInWithCredential:success")

//                            hideProgressBar()


                        val shopname =
                            database.reference.child("MicMac").child("Sellers").child(
                                user!!.uid
                            ).child("shopname")


                        val userModel = Users()
                        if (user != null) {
                            userModel.email = user.email.toString()
                        }
                        if (user != null) {
                            userModel.userid = user.uid
                        }
                        if (user != null) {
                            Prevalent.Displayname = user.displayName.toString()
                        }
                        if (user != null) {
                            userModel.displayname = user.displayName.toString()
                        }
                        Prevalent.Photourl = user!!.photoUrl.toString()
                        userModel.photourl = user.photoUrl.toString()
                        //                            database.getReference().child("Patient").child(user.getUid()).child("Userdetails").setValue(userModel);


//                            progressDialog?.dismiss()
                        if (usertype == "Buyer") {
                            Prevalent.UserType = "Buyer"

                            database.reference.child("MicMac").child("Buyers").child(user.uid)
                                .addValueEventListener(
                                    object :
                                        ValueEventListener {
                                        override fun onDataChange(snapshot: DataSnapshot) {

                                            if (snapshot.child("buyercontact").exists()) {

                                                val intent = Intent(
                                                    applicationContext,
                                                    AllProducts::class.java
                                                )
                                                startActivity(intent)
                                                finish()


                                            } else {

                                                database.reference.child("MicMac")
                                                    .child("Buyers")
                                                    .child(
                                                        user.uid
                                                    )
                                                    .setValue(userModel)
                                                val intent = Intent(
                                                    applicationContext,
                                                    BuyerRegistration::class.java
                                                )
                                                startActivity(intent)
                                                finish()


                                            }
                                        }

                                        override fun onCancelled(error: DatabaseError) {
                                        }


                                    })

                            progressDialog?.dismiss()


                        }

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("TAG", "signInWithCredential:failure", task.exception)
                        progressDialog?.dismiss()
//                            hideProgressBar()
                        Toast.makeText(
                            this,
                            "Login Failed" + task.exception!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    progressDialog?.dismiss()

                })

//        hideProgressBar()

    }



    override fun onBackPressed() {
        run {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Exit")
            builder.setMessage("Are You Sure?")
            builder.setPositiveButton("Yes") { dialog, which ->
                FirebaseAuth.getInstance().signOut()
                finishAffinity()
                exitProcess(0)
            }
            builder.setNegativeButton("No") { dialog, which -> dialog.dismiss() }
            val alert = builder.create()
            alert.show()
        }
    }


    fun showProgressBar() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
        binding.progressBar.visibility = View.VISIBLE
    }


    fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }



    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = FirebaseAuth.getInstance().currentUser
//        updateUI(currentUser)

        if (currentUser != null) {

            Prevalent.Displayname= currentUser.displayName.toString()
            Prevalent.Photourl= currentUser.photoUrl.toString()
            Prevalent.UserType="Buyer"
            startActivity(Intent(applicationContext, AllProducts::class.java))
//            Prevalent.UserType="Buyer"
//            startActivity(Intent(applicationContext, ChoiceActivity::class.java))
            Log.wtf("prevalent", Prevalent.Displayname)
            Log.wtf("prevalent", Prevalent.Photourl)
        }
    }









}



