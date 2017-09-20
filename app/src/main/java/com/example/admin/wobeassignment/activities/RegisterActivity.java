package com.example.admin.wobeassignment.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.admin.wobeassignment.R;
import com.example.admin.wobeassignment.fragments.GoogleSignInFragment;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Admin on 20-09-2017.
 */

public class RegisterActivity extends FragmentActivity implements View.OnClickListener {

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private Button fb, btnRegister;
    private TextInputEditText etFirstName, etLastName, etEmail, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        super.onCreate(savedInstanceState);
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_register);

        fb = (Button) findViewById(R.id.btnFacebookSignUp);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        fb.setOnClickListener(this);

        loginButton.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        Log.d("success", "Success");

                        String accessToken = loginResult.getAccessToken()
                                .getToken();
                        Log.i("accessToken", accessToken);

                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object,
                                                            GraphResponse response) {

                                        Log.i("LoginActivity",
                                                response.toString());
                                        try {
                                            String id = object.getString("id");
                                            try {
                                                URL profile_pic = new URL(
                                                        "http://graph.facebook.com/" + id + "/picture?type=large");
                                                Log.i("profile_pic",
                                                        profile_pic + "");

                                            } catch (MalformedURLException e) {
                                                e.printStackTrace();
                                            }
                                            String name = object.getString("name");
                                            String email = object.getString("email");
                                            String gender = object.getString("gender");
                                            String birthday = object.getString("birthday");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields",
                                "id,name,email,gender, birthday");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        System.out.println("onCancel");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        System.out.println("onError");
                        Log.v("LoginActivity", exception.getCause().toString());
                    }
                });

        initialiseViews();
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode,
                                    Intent data) {
        super.onActivityResult(requestCode, responseCode, data);
        callbackManager.onActivityResult(requestCode, responseCode, data);
    }


    private void initialiseViews() {
        Button btnGoogleSignUp = (Button) findViewById(R.id.btnGoogleSignUp);
        btnGoogleSignUp.setOnClickListener(this);
        etFirstName = (TextInputEditText) findViewById(R.id.etFirstname);
        etLastName = (TextInputEditText) findViewById(R.id.etLastname);
        etEmail = (TextInputEditText) findViewById(R.id.etEmail);
        etPassword = (TextInputEditText) findViewById(R.id.etPassword);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);
    }

    private void validation() {
        if (!(etFirstName.getText().toString().trim().length() > 0)) {
            Toast.makeText(this, getResources().getText(R.string.enter_first_name), Toast.LENGTH_SHORT).show();
        } else if (!(etLastName.getText().toString().trim().length() > 0)) {
            Toast.makeText(this, getResources().getText(R.string.enter_last_name), Toast.LENGTH_SHORT).show();
        } else if (!(etEmail.getText().toString().trim().length() > 0)) {
            Toast.makeText(this, getResources().getText(R.string.enter_email), Toast.LENGTH_SHORT).show();
        } else if (!(etPassword.getText().toString().trim().length() > 0)) {
            Toast.makeText(this, getResources().getText(R.string.enter_password), Toast.LENGTH_SHORT).show();
        } else {
            //register api call
        }
    }


    private void addFragment() {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        GoogleSignInFragment googleSignInFragment = new GoogleSignInFragment();
        fragmentTransaction.add(R.id.fragment_holder, googleSignInFragment, "GoogleSignIn");
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btnGoogleSignUp:
                FrameLayout frameLayout = (FrameLayout) findViewById(R.id.fragment_layout);
                frameLayout.setVisibility(View.VISIBLE);
                addFragment();
                break;
            case R.id.btnFacebookSignUp:
                loginButton.performClick();
                break;
            case R.id.btnRegister:
                validation();
                break;
        }
    }
}