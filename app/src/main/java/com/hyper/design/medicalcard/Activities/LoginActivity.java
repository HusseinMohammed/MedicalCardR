package com.hyper.design.medicalcard.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hyper.design.medicalcard.AlertDialogManager.AlertDialogManager;
import com.hyper.design.medicalcard.Helper.SessionManager;
import com.hyper.design.medicalcard.Helper.SQLiteHandler;
import com.hyper.design.medicalcard.MainActivity;
import com.hyper.design.medicalcard.R;
import com.hyper.design.medicalcard.User.User;
import com.hyper.design.medicalcard.User.UserLocalStore;
import com.hyper.design.medicalcard.network_hus.MySingletonhus;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.Manifest.permission.READ_CONTACTS;
import static com.hyper.design.medicalcard.AppConstant.SERVER_URL;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String SERVER_MEDICAL_URL = SERVER_URL;
    public static final String SERVER_LOGIN_URL= "/login";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_CODE = "code";

    UserLocalStore userLocalStore;

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    // Alert Dialog Manager
    AlertDialogManager alert = new AlertDialogManager();

    // Session Manager Class
    //SessionManager session;

    //private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnLogin;
    private Button btnLinkToRegister;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    private User user;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView/*, mCodeView*/;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    SharedPreferences settings;

    public String localLanguage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);*/

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.email_sign_in_button);
        btnLinkToRegister = (Button) findViewById(R.id.email_sign_up_button);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        /*if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }*/

        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        intent.getIntExtra("id",1);
        intent.getStringExtra("name");





        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        /*mCodeView = (AutoCompleteTextView) findViewById(R.id.code);*/
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        userLocalStore = new UserLocalStore(this);
        user = new User();

        settings = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        localLanguage = settings.getString("locale","");
        //localLanguage = "en";

        editor.clear();
        editor.commit();

        //Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();

        /*mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    //attemptLogin();
                    return true;
                }
                return false;
            }
        })*/;

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        Button mEmailSignUpButton = (Button) findViewById(R.id.email_sign_up_button);


        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get username, password from EditText
                String email = mEmailView.getText().toString();
                String password = mPasswordView.getText().toString();
                User user;
                user = new User(email, password);

                //userLocalStore.storedUserData(user);
                //userLocalStore.setUserLoggedIn(true);

                switch (view.getId()){
                    case R.id.email_sign_in_button:
                        /*String email = mEmailView.getText().toString();
                        String password = mPasswordView.getText().toString();*/
                        if (email.equals("")){
                            mEmailView.setError(getString(R.string.error_field_required));
                        } else if(!isEmailValid(email)){
                            mEmailView.setError(getString(R.string.error_invalid_email));
                        } else if (password.equals("")){
                            mPasswordView.setError(getString(R.string.error_field_required));
                        } else if(!isPasswordValid(password)){
                            mPasswordView.setError(getString(R.string.error_invalid_password));
                        } else {
                            email = mEmailView.getText().toString();;
                            password = mPasswordView.getText().toString();
                            user = new User(email, password);
                            //userLocalStore.storedUserData(user);
                            //userLocalStore.setUserLoggedIn(true);
                            showProgress(true);
                            sendDataOfLogin();
                        }
                        break;
                    case R.id.email_sign_up_button:
                        Intent intent = new Intent(LoginActivity.this, Register.class);
                        startActivity(intent);
                        finish();
                        break;
                }


            }
        });

        mEmailSignUpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, Register.class);
                startActivity(intent);
                finish();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public void sendDataOfLogin(){

        final String email = mEmailView.getText().toString();
        /*final String code = mCodeView.getText().toString().trim();*/
        final String password = mPasswordView.getText().toString();

        pDialog.setMessage("Logging in ...");
        //showDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, SERVER_MEDICAL_URL + SERVER_LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("CitySpinner Response",response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            try{
                                // Loop through the array elements

                                String strMessage = jsonObject.getString("msg");
                                int intSuccess = jsonObject.getInt("done");

                                switch (strMessage){
                                    case "login success":
                                        JSONObject jsonObjectUser = jsonObject.getJSONObject("user");
                                        String userName = jsonObjectUser.getString("name");
                                        int clientId = jsonObjectUser.getInt("client_id");
                                        String userEmail = jsonObjectUser.getString("email");
                                        int userId = jsonObjectUser.getInt("id");
                                        String uId = String.valueOf(userId);
                                        String created_at = jsonObjectUser
                                                .getString("created_at");
                                        // user successfully logged in
                                        // Create login session
                                        session.setLogin(true);

                                        // Now store the user in SQLite
                                        //String uid = jObj.getString("uid");

                                        // Inserting row in users table
                                        db.addUser(userName, userEmail, uId, created_at);

                                        user = new User(userId, userName, userEmail);
                                        userLocalStore.storeUserData(user);

                                        userLocalStore.setUserLoggedIn(true);

                                        Boolean userLoggedIn = userLocalStore.getUserLoggedIn();
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        intent.putExtra("email", userEmail);
                                        intent.putExtra("id", userId);
                                        intent.putExtra("name", userName);
                                        intent.putExtra("userLoggedIn", userLoggedIn);
                                        startActivity(intent);
                                        finish();
                                    case "check your email":
                                        showProgress(false);
                                        //hideDialog();
                                        Toast.makeText(getBaseContext(), strMessage, Toast.LENGTH_LONG).show();
                                    case "check your password":
                                        showProgress(false);
                                        //hideDialog();
                                        Toast.makeText(getBaseContext(), strMessage, Toast.LENGTH_LONG).show();
                                }
                                /*if(intSuccess == 1 && strMessage == "login success"){
                                    // Staring MainActivity
                                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(i);
                                    finish();
                                    onDestroy();
                                } else if(strMessage == "check your email"){
                                    Toast.makeText(getBaseContext(), strMessage, Toast.LENGTH_LONG).show();
                                } else if(strMessage == "check your password"){
                                    Toast.makeText(getBaseContext(), strMessage, Toast.LENGTH_LONG).show();
                                }*/

                            }catch (JSONException e){
                                e.printStackTrace();
                            }




                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error CitySpinner", error+"");
                //hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(KEY_EMAIL, email);
                params.put(KEY_PASSWORD, password);
                /*params.put(KEY_CODE, code);*/
                return params;
            }
        };
//       RequestQueue requestQueue = Volley.newRequestQueue(this.getActivity());
//       requestQueue.add(stringRequest);
        MySingletonhus.getInstance(LoginActivity.this).addToRequestQueue(stringRequest);

    }

    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == android.R.id.home)
        {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        /*Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);*/
        return super.onOptionsItemSelected(item);//true
    }


    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        //getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /*@Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }*/

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.email_sign_in_button:
                String email = mEmailView.getText().toString();
                String password = mPasswordView.getText().toString();

                switch (view.getId()){
                    case R.id.email:
                        if (email.equals("")){
                            mEmailView.setError(getString(R.string.error_field_required));
                        } else if(!isEmailValid(email)){
                            mEmailView.setError(getString(R.string.error_invalid_email));
                        }
                    case R.id.password:
                        if (password.equals("")){
                            mPasswordView.setError(getString(R.string.error_field_required));
                        } else if(!isPasswordValid(password)){
                            mPasswordView.setError(getString(R.string.error_invalid_password));
                        } else {
                            email = mEmailView.getText().toString();;
                            password = mPasswordView.getText().toString();
                            User user = new User(email, password);
                            //userLocalStore.storedUserData(user);
                            userLocalStore.setUserLoggedIn(true);
                            showProgress(true);
                            sendDataOfLogin();
                        }
                }
                break;
            case R.id.email_sign_up_button:
                Intent intent = new Intent(LoginActivity.this, Register.class);
                startActivity(intent);
                break;
        }
    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }


    public void checkUserLogin(){

        // Get username, password from EditText
        String username = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        // Check if username, password is filled
        if(username.trim().length() > 0 && password.trim().length() > 0){
            // For testing puspose username, password is checked with sample data
            // username = test
            // password = test
            if(username.equals("test") && password.equals("test")){

                // Creating user login session
                // For testing i am stroing name, email as follow
                // Use user real data
                //session.createLoginSession("Android Hive", "anroidhive@gmail.com");

                // Staring MainActivity
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();

            } else{
                // username / password doesn't match
                alert.showAlertDialog(LoginActivity.this, "Login failed..", "Username/Password is incorrect", false);
            }
        } else{
            // user didn't entered username or password
            // Show alert asking him to enter the details
            alert.showAlertDialog(LoginActivity.this, "Login failed..", "Please enter username and password", false);
        }

    }

}

