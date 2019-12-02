package com.p3.aaugroup301.lockeese;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements TextWatcher, CompoundButton.OnCheckedChangeListener {

   // UserLoginTask authTask;
    // UI references.
    private EditText usernameView;
    private EditText passwordView;
    private Button loginBtn;
    private CheckBox rememberMeCb;
    private static final String PREFERENCE_NAME = "preference";
    private static final String KEY_REMEMBER = "remember";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    int duration = Toast.LENGTH_LONG;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login2 );

        sharedPreferences = getSharedPreferences( PREFERENCE_NAME, MODE_PRIVATE );
        editor = sharedPreferences.edit();

        usernameView = findViewById( R.id.username );
        passwordView = findViewById( R.id.password );
        loginBtn = findViewById( R.id.login );

        rememberMeCb = findViewById( R.id.checkBox );

        if (sharedPreferences.getBoolean( KEY_REMEMBER, false )) {
            rememberMeCb.setChecked( true );
        } else {
            rememberMeCb.setChecked( false );
        }
        usernameView.setText( sharedPreferences.getString( KEY_USERNAME, "" ) );
        passwordView.setText( sharedPreferences.getString( KEY_PASSWORD, "" ) );

        usernameView.addTextChangedListener( this );
        passwordView.addTextChangedListener( this );
        rememberMeCb.setOnCheckedChangeListener( this );


        Log.e( " UDP client ", "starting app" );

        usernameView = findViewById( R.id.username );
        passwordView = findViewById( R.id.password );
        // Create a button handler and call the dialog box display method in it
        Button aboutUsButton = findViewById( R.id.aboutUsButton );
        aboutUsButton.
                setOnClickListener( new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        PopUpWindow popUpClass = new PopUpWindow();
                        popUpClass.showPopupWindow( v );
                    }
                } );


        Button mEmailSignInButton = findViewById( R.id.login );
        mEmailSignInButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        } );
    }

    private void attemptLogin() {

        // Store values at the time of the login attempt.
        String username = usernameView.getText().toString();
        String password = passwordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)){
            passwordView.setError(getString(R.string.error_invalid_password));
            focusView = passwordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(username)) {
            usernameView.setError(getString(R.string.error_field_required));
            focusView = usernameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();

        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

            UserLoginTask authTask = new UserLoginTask(username, password, this);
            if(checkPassword(password)){
                authTask.execute((Void) null);
            }
        }

        }


    public  boolean checkPassword(String password){
        boolean isFine = false;
        if(password.length() >= 8 && password.matches(".*\\d+.*")) {
            Log.e("checkP", "lengthOK");
            Log.e("checkP", "number here");
            isFine = true;
        }

        else {
            Toast.makeText(getApplicationContext(), "Password must be at least 8 " +
                    "characters long and have a digit", duration).show();
            isFine = false;
        }
        Log.e("checkP", "before return " + isFine);
        return isFine;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(rememberMeCb.isChecked()){
            editor.putString(KEY_USERNAME, usernameView.getText().toString().trim());
            editor.putString(KEY_PASSWORD,passwordView.getText().toString().trim());
            editor.putBoolean(KEY_REMEMBER,true);
            editor.apply();
        }else {
            editor.putBoolean(KEY_REMEMBER, false);
            editor.remove(KEY_PASSWORD);
            editor.remove(KEY_USERNAME);
            editor.apply();
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
*/
    public class UserLoginTask extends AsyncTask<Void, Void, String> {

        private  String username;
        private  String password;
        private Context context;
        private ProgressDialog progressDialog;
        private String result;
        DataBaseHandler dataBaseHandler = new DataBaseHandler();

      public UserLoginTask(String username, String password, Context context) {
            this.context = context;
            this.username = username;
            this.password = password;
        }

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("Loading");
            progressDialog.setMessage("Please wait");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {

            synchronized (this) {
                try {
                    Log.e(" trying to login ", "Before DataBaseHandler");
                    //Calling the login method on the DataBaseHandler
                    result = dataBaseHandler.login(username,password);
                    //calling publishProgress method
                    //publishProgress();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if (progressDialog.isShowing()) {
                progressDialog.setMessage(result);
                progressDialog.dismiss();
                if(!result.equals("")){
                    //Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show();
                    //Controller.goToMainActivity(context);
                      context.startActivity(new Intent(context, KeysListActivity.class));
                }
                else { AlertDialog.Builder login = new AlertDialog.Builder(context);

                    login.setMessage(result);
                    login.setCancelable(false);
                    login.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog reg = login.create();
                    reg.show();
                }
            }
        }
    }

}
