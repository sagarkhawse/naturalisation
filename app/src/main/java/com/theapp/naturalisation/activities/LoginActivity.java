package com.theapp.naturalisation.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.PriceChangeConfirmationListener;
import com.android.billingclient.api.PriceChangeFlowParams;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchaseHistoryRecord;
import com.android.billingclient.api.PurchaseHistoryResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.theapp.naturalisation.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingDeque;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int RC_SIGN_IN = 1;
    private static final String TAG = "LoginActivityTest";
    private static final int REQUEST_CODE_LOCATION = 2;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private ProgressBar progress_bar;

    @BindView(R.id.linear_sign_up_google)
    LinearLayoutCompat linear_sign_up_google;

    @BindView(R.id.linear_sign_up_facebook)
    LinearLayoutCompat linear_sign_up_facebook;

    private BillingClient billingClient;


    private FirebaseFirestore firedb;
    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private static final String EMAIL = "email";
    private SkuDetails skuDetails;

    List<SkuDetails> skuDetailsList = new ArrayList<>();
    ;
    // To get key go to Developer Console > Select your app > Development Tools > Services & APIs.
    String base64Key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAp1fWXf7V1ipSKqkqd07Ep0m0wOH5CEgEHRS3Ha35beLctvUSAPTO1hBq6sxZ31P9eIBDdyVF40uzbcCY4bPe9BYgh9sKPoL3O653bSB4+NEs3y5bth6hqG0nxpQqOdg/5MsuKlhiTj0UTQPb14oomU3hELUpjccS82fW6c/BvCRjRf5dxYnKAjDkKpEwcG4TnCPxPdfjP+tsx8a0GHmEnLlhRrTQUlPhRV+wi6k5zyoXnx99eaxCJCpaaEwj6zxoZ2Fbjpo8sfmaP3jFKGNxTwxyBr84s+OmMFaqQ1IujLIVutqlhKkzPW7ck5vbks+kXBX8YydookukC5l2WNca7wIDAQAB";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        initGoogleSignIn();
        initFacebookSignIn();
        checkLocationPermission();
        initAppBill();
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
            // REQUEST_CODE_LOCATION should be defined on your app level
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_LOCATION && grantResults.length > 0
                && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            throw new RuntimeException("Location services are required in order to " +
                    "connect to a reader.");
        }
    }


    /**
     * Verifies that the purchase was signed correctly for this developer's public key.
     * <p>Note: It's strongly recommended to perform such check on your backend since hackers can
     * replace this method with "constant true" if they decompile/rebuild your app.
     * </p>
     */
    private boolean verifyValidSignature(String signedData, String signature) {
        try {
            return Security.verifyPurchase(base64Key, signedData, signature);
        } catch (IOException e) {
            Log.e(TAG, "Got an exception trying to validate a purchase: " + e);
            return false;
        }
    }


    private void initAppBill() {


        billingClient = BillingClient.newBuilder(this)
                .setListener(new PurchasesUpdatedListener() {
                    @Override
                    public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> list) {
                        for (Purchase purchase : list) {
                            // When every a new purchase is made
                            // Here we verify our purchase
                            if (!verifyValidSignature(purchase.getOriginalJson(), purchase.getSignature())) {
                                // Invalid purchase
                                // show error to user
                                Log.i(TAG, "Got a purchase: " + purchase + "; but signature is bad. Skipping...");
                                return;
                            } else {
                                // purchase is valid
                                // Perform actions

                            }
                        }
                        Log.d(TAG, "onPurchasesUpdated: ");
                    }
                })
                .enablePendingPurchases()
                .build();

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    Log.d(TAG, "onBillingSetupFinished: ");
                    appquery();
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                Log.d(TAG, "onBillingServiceDisconnected: ");
            }
        });


        billingClient.queryPurchaseHistoryAsync(BillingClient.SkuType.SUBS, new PurchaseHistoryResponseListener() {
            @Override
            public void onPurchaseHistoryResponse(@NonNull BillingResult billingResult, @Nullable List<PurchaseHistoryRecord> purchasesList) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK
                        && purchasesList != null) {
                    for (PurchaseHistoryRecord purchaseHistoryRecord : purchasesList) {
                        // Process the result.
                        Log.d(TAG, "onPurchaseHistoryResponse: " + purchaseHistoryRecord.getPurchaseTime());
                    }
                }
            }
        });


        ConsumeResponseListener listener = new ConsumeResponseListener() {
            @Override
            public void onConsumeResponse(@NonNull BillingResult billingResult, @NonNull String s) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // Handle the success of the consume operation.
                    // For example, increase the number of coins inside the user's basket.
                    Log.d(TAG, "onConsumeResponse: ");
                }
            }

        };

//        billingClient.consumeAsync(,listener);
//    SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
//    params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS);
//    params.build();
//
//    billingClient.querySkuDetailsAsync(params,
//            new SkuDetailsResponseListener() {
//                @Override
//                public void onSkuDetailsResponse(BillingResult billingResult,
//                                                 List<SkuDetails> skdlist) {
//
//                    // Retrieve a value for "skuDetails" by calling querySkuDetailsAsync().
////                    BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
////                            .setSkuDetails(skdlist.get(0))
////                            .build();
//
////                    int responseCode = billingClient.launchBillingFlow(LoginActivity.this, billingFlowParams).getResponseCode();
//                }
//            });


// Handle the result.


    }


    private void appquery() {
        List<String> skuList = new ArrayList<>();
        skuList.add("simplyfrench.pro.subscription.monthly");
//        skuList.add("test");

        SkuDetailsParams params = SkuDetailsParams.newBuilder()
                .setSkusList(skuList)
                .setType(BillingClient.SkuType.SUBS)
                .build();
//        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
//        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
        billingClient.querySkuDetailsAsync(params, new SkuDetailsResponseListener() {
            @Override
            public void onSkuDetailsResponse(@NonNull BillingResult billingResult, @Nullable List<SkuDetails> list) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && list != null) {
                    Log.d(TAG, "onSkuDetailsResponse: " + list.toString());
if (list.size()==0){
    Toast.makeText(LoginActivity.this, "item not found ", Toast.LENGTH_SHORT).show();
}else {
    Toast.makeText(LoginActivity.this, "item found", Toast.LENGTH_SHORT).show();
                                    SkuDetails skuDetails = skuDetailsList.get(0);
                                BillingFlowParams builder = BillingFlowParams.newBuilder()
                                        .setSkuDetails(skuDetails).build();
                                int responseCode = billingClient.launchBillingFlow(LoginActivity.this, builder).getResponseCode();
                                Log.d(TAG, "onSkuDetailsResponse: "+responseCode);
}



                } else {
                    Log.d(TAG, "onSkuDetailsResponse: failed");
                }
            }
        });

    }

    private void initViews() {
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        // Access a Cloud Firestore instance from your Activity
        firedb = FirebaseFirestore.getInstance();
        progress_bar = findViewById(R.id.progress_google);

        linear_sign_up_google.setOnClickListener(this);
        linear_sign_up_facebook.setOnClickListener(this);

        loginButton = findViewById(R.id.login_button);
    }


    private void initGoogleSignIn() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }

    private void initFacebookSignIn() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions(Collections.singletonList(EMAIL));
        // If you are using in a fragment, call loginButton.setFragment(this);


        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Log.d(TAG, "onSuccess: " + loginResult.toString());
                handleFacebookToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                // App code
                Log.d(TAG, "onCancel: ");

            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.d(TAG, "onError: " + exception.getMessage());

            }
        });

    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    //=====================Handle Facebook login=================//
    private void handleFacebookToken(final AccessToken accessToken) {
        final AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //login success
                    Log.d(TAG, "onComplete: " + task.getResult());
                    try {
                        if (task.getResult().getAdditionalUserInfo().isNewUser()) {
                            storeUserData();
                        } else {
                            progress_bar.setVisibility(View.GONE);
                            linear_sign_up_google.setVisibility(View.VISIBLE);
                            openHome();
                        }
                    } catch (NullPointerException e) {
                        Log.d(TAG, "onComplete: " + e.getMessage());
                    }

                } else {
                    Toast.makeText(LoginActivity.this, "Failed : -" + task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
            }
        });

    }
    //=====================Handle Facebook login=================//

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            progress_bar.setVisibility(View.VISIBLE);
            linear_sign_up_google.setVisibility(View.GONE);
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "onActivityResult: success sign in");

                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.d(TAG, "onActivityResult: " + e.getLocalizedMessage());
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: " + task.getResult());
                            mUser = mAuth.getCurrentUser();
                            // Sign in success, update UI with the signed-in user's information
                            try {
                                if (task.getResult().getAdditionalUserInfo().isNewUser()) {
                                    storeUserData();
                                } else {
                                    progress_bar.setVisibility(View.GONE);
                                    linear_sign_up_google.setVisibility(View.VISIBLE);
                                    openHome();
                                }
                            } catch (NullPointerException e) {
                                Log.d(TAG, "onComplete: " + e.getMessage());
                            }


                        } else {
                            Log.d(TAG, "onComplete: " + task.getException());
                            // If sign in fails, display a message to the user.
                        }

                        // ...
                    }
                });
    }

    private void storeUserData() {
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("name", mUser.getDisplayName());
        user.put("email", mUser.getEmail());
        user.put("id", mUser.getUid());
        user.put("subscription", "inactive");


        // Add a new document with a generated ID
        firedb.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        progress_bar.setVisibility(View.GONE);
                        linear_sign_up_google.setVisibility(View.VISIBLE);
                        openHome();
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    private void openHome() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        Toast.makeText(this, "Login success", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linear_sign_up_google:
                signIn();
                break;
            case R.id.linear_sign_up_facebook:
                loginButton.performClick();
                break;
        }
    }


}