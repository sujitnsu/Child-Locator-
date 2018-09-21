package com.example.ridowanahmed.childlocator.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ridowanahmed.childlocator.Dashboard.ParentDashboard;
import com.example.ridowanahmed.childlocator.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class ParentLoginActivity extends AppCompatActivity {

    EditText editText_parent_number;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mFirebaseAuthStateListener;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId, phoneNumber;

    SharedPreferences mSharedPreferences;

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mFirebaseAuthStateListener);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_login);

        editText_parent_number = (EditText) findViewById(R.id.editText_parent_number);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    mSharedPreferences = ParentLoginActivity.this.getSharedPreferences(getString(R.string.PREF_FILE), MODE_PRIVATE);
                    SharedPreferences.Editor mEditor = mSharedPreferences.edit();
                    mEditor.putString(getString(R.string.PARENT_GIVE_NUMBER), phoneNumber);
                    mEditor.commit();
                    Toast.makeText(ParentLoginActivity.this, "Now you are logged into " + firebaseAuth.getCurrentUser().getProviderId(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ParentLoginActivity.this, ParentDashboard.class);
                    Log.e("ParentLoginActivity", "Starting ParentDashboard Activity");
                    startActivity(intent);
                    finish();
                }
            }
        };

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                //Called if it is not needed to enter verification code
                signInWithPhone(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                //incorrect phone number, verification code, emulator, etc.
                Toast.makeText(ParentLoginActivity.this, "onVerificationFailed " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                //now the code has been sent, save the verificationId we may need it
                super.onCodeSent(verificationId, forceResendingToken);
                mVerificationId = verificationId;
                Log.e("ParentLoginActivity", mVerificationId);
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(String verificationId) {
                //called after timeout if onVerificationCompleted has not been called
                super.onCodeAutoRetrievalTimeOut(verificationId);
                Toast.makeText(ParentLoginActivity.this, "onCodeAutoRetrievalTimeOut :" + verificationId, Toast.LENGTH_SHORT).show();
            }
        };

    }

    public void parentLogin(View view) {
        phoneNumber = editText_parent_number.getText().toString();
        if (phoneNumber.length() != 11) {
            editText_parent_number.setError(getString(R.string.number_error));
            return;
        }
        Toast.makeText(getApplicationContext(), "Request send. Wait a second", Toast.LENGTH_SHORT).show();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,                                                 // Phone number to verify
                60,                                                          // Timeout duration
                TimeUnit.SECONDS,                                            // Unit of timeout
                ParentLoginActivity.this,                                    // Activity (for callback binding)
                mCallbacks
        );
        return;
    }

    private void signInWithPhone(PhoneAuthCredential phoneAuthCredential) {
        mFirebaseAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ParentLoginActivity.this, "Signed in successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ParentLoginActivity.this, "Failed to log in" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
