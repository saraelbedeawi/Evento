package com.sf.evento;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.storage.FirebaseStorage;

import java.text.CollationElementIterator;
import java.util.concurrent.TimeUnit;

public class PhoneVerificationActivity extends AppCompatActivity {
//    private Button loginBtn, secButton;
//    private boolean mVerficationInProgress=false;
//    private FirebaseAuth mAuth;
//    private boolean mVerificationInProgress = false;
//    private String mVerificationId;
//    private PhoneAuthProvider.ForceResendingToken mResendToken;
//    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
//    private EditText phone;
//    private static final String TAG = "PhoneAuthActivity";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_phone_verification);
//        phone=(EditText)findViewById(R.id.phone);
//        loginBtn = findViewById(R.id.sign_up);
//        loginBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                attemptLogin();
//
//            }
//        });
//
//
//        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//
//            @Override
//            public void onVerificationCompleted(PhoneAuthCredential credential) {
//                // This callback will be invoked in two situations:
//                // 1 - Instant verification. In some cases the phone number can be instantly
//                //     verified without needing to send or enter a verification code.
//                // 2 - Auto-retrieval. On some devices Google Play services can automatically
//                //     detect the incoming verification SMS and perform verification without
//                //     user action.
//                Log.d(TAG, "onVerificationCompleted:" + credential);
//                // [START_EXCLUDE silent]
//                mVerificationInProgress = false;
//                // [END_EXCLUDE]
//
//                // [START_EXCLUDE silent]
//                // Update the UI and attempt sign in with the phone credential
//                //updateUI(STATE_VERIFY_SUCCESS, credential);
//                // [END_EXCLUDE]
//
//
//                String code = phoneAuthCredential.getSmsCode();
//                if (code != null) {
//
//                    editTextCode.setText(code);
//                    //verifying the code
//                    verifyVerificationCode(code);
//                }
//                signInWithPhoneAuthCredential(credential);
//            }
//
//            @Override
//            public void onVerificationFailed(FirebaseException e) {
//                // This callback is invoked in an invalid request for verification is made,
//                // for instance if the the phone number format is not valid.
//                Log.w(TAG, "onVerificationFailed", e);
//                // [START_EXCLUDE silent]
//                mVerificationInProgress = false;
//                // [END_EXCLUDE]
//
//                if (e instanceof FirebaseAuthInvalidCredentialsException) {
//                    // Invalid request
//                    // [START_EXCLUDE]
//                    Toast.makeText(PhoneVerificationActivity.this, "Invalid phone number.", Toast.LENGTH_SHORT).show();
//                    //mPhoneNumberField.setError("Invalid phone number.");
//                    // [END_EXCLUDE]
//                } else if (e instanceof FirebaseTooManyRequestsException) {
//                    // The SMS quota for the project has been exceeded
//                    // [START_EXCLUDE]
//                    Toast.makeText(PhoneVerificationActivity.this, "Quota exceeded.", Toast.LENGTH_SHORT).show();
////                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
////                            Snackbar.LENGTH_SHORT).show();
//                    // [END_EXCLUDE]
//                }
//
//                // Show a message and update the UI
//                // [START_EXCLUDE]
////                updateUI(STATE_VERIFY_FAILED);
//                Toast.makeText(PhoneVerificationActivity.this, "Verify Failed", Toast.LENGTH_SHORT).show();
//                // [END_EXCLUDE]
//            }
//
//            @Override
//            public void onCodeSent(@NonNull String verificationId,
//                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
//                // The SMS verification code has been sent to the provided phone number, we
//                // now need to ask the user to enter the code and then construct a credential
//                // by combining the code with a verification ID.
//                Log.d(TAG, "onCodeSent:" + verificationId);
//
//                // Save verification ID and resending token so we can use them later
//                mVerificationId = verificationId;
//                mResendToken = token;
//
//                // [START_EXCLUDE]
//                // Update UI
////                updateUI(STATE_CODE_SENT);
//                Toast.makeText(PhoneVerificationActivity.this, "Code Sent", Toast.LENGTH_SHORT).show();
//                // [END_EXCLUDE]
//            }
//        };
//
//    }
//    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "signInWithCredential:success");
//
//                            FirebaseUser user = task.getResult().getUser();
//                            // [START_EXCLUDE]
////                            updateUI(STATE_SIGNIN_SUCCESS, user);
//                            Toast.makeText(PhoneVerificationActivity.this, "SignIn Success", Toast.LENGTH_SHORT).show();
//                            // [END_EXCLUDE]
//                        } else {
//                            // Sign in failed, display a message and update the UI
//                            Log.w(TAG, "signInWithCredential:failure", task.getException());
//                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
//                                // The verification code entered was invalid
//                                // [START_EXCLUDE silent]
////                                mVerificationField.setError("Invalid code.");
//                                Toast.makeText(PhoneVerificationActivity.this, "Invalid Code.", Toast.LENGTH_SHORT).show();
//                                // [END_EXCLUDE]
//                            }
//                            // [START_EXCLUDE silent]
//                            // Update UI
////                            updateUI(STATE_SIGNIN_FAILED);
//                            Toast.makeText(PhoneVerificationActivity.this, "Sign In Failed", Toast.LENGTH_SHORT).show();
//                            // [END_EXCLUDE]
//                        }
//                    }
//                });
//    }
//    private  void attemptLogin()
//    {
//        phone.setError(null);
//
//        startPhoneNumberVerfication(phone.getText().toString());
//    }
//    private  void startPhoneNumberVerfication(String phone)
//    {
//                PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                        "+2"+phone,        // Phone number to verify
//                        60,                 // Timeout duration
//                        TimeUnit.SECONDS,   // Unit of timeout
//                        PhoneVerificationActivity.this,               // Activity (for callback binding)
//                        mCallbacks);
//                mVerficationInProgress=true;
//    }
private EditText editTextMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verification);

        editTextMobile = findViewById(R.id.phone);

        findViewById(R.id.Continue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mobile = editTextMobile.getText().toString().trim();

                if(mobile.isEmpty() || mobile.length() < 11){
                    editTextMobile.setError("Enter a valid mobile");
                    editTextMobile.requestFocus();
                    return;
                }

                Intent intent = new Intent(PhoneVerificationActivity.this, CodeVerficationActivity.class);
                intent.putExtra("mobile", mobile);
                startActivity(intent);
            }
        });
    }

}

