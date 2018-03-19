package com.example.sadna.triviaking;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;

/**
 * MainActivity, is the first screen the user gets when opening the application for the first time
 */
public class MainActivity extends AppCompatActivity {

    //region variables
    EditText edtTxtEmail;
    EditText edtTxtPass;
    Button btnConnect;
    TextView txtSignNewUser;
    TextView txtForgotMyPass;
    int userId = -999;
    TextView txtWarning;
    String userName = null;
    //endregion

    /**
     * onCreate basic method that run when the activity created and initialize the activity needs
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //region Bonding the variables with the view objects.
        edtTxtEmail = (EditText) findViewById(R.id.edtTxtMail);
        edtTxtPass = (EditText) findViewById(R.id.edtTxtPassword);
        btnConnect = (Button) findViewById(R.id.btnConnect);
        txtSignNewUser = (TextView) findViewById(R.id.txtNewUser);
        txtForgotMyPass = (TextView) findViewById(R.id.txtForgotPass);
        txtWarning = (TextView) findViewById(R.id.txtWarn);
        //endregion

        //Loading saved user login if exist from preferences and load main connected activity
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPreferences.contains("USER_ID")) {
            openConnected(sharedPreferences.getInt("USER_ID", -999), sharedPreferences.getString("USER_NAME", ""));
        }
    }

    /**
     * When pressing the connect button, sending the user details to server and connect.
     *
     * @param view
     */
    public void connectUser(View view) {
        txtWarning.setText("");
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Utils.getHttpAddress() + "Trivia/webapi/users/login?email=" + edtTxtEmail.getText().toString() + "&pass="
                + Utils.hash(edtTxtPass.getText().toString());
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        userId = Utils.jsonFieldToInt(response, "id");
                        if (userId == -1)//user is not exist or the password is not right
                        {
                            txtWarning.setText("מייל או סיסמא לא נכונים, נסה שנית");
                        } else {
                            //login is successful, saving user to preferences and loading MainConnected
                            userName = Utils.jsonFieldToString(response, "name");
                            Utils.savePreferences(MainActivity.this.getApplicationContext(), "USER_NAME", userName);
                            Utils.saveIntPreferences(MainActivity.this.getApplicationContext(), "USER_ID", userId);
                            openConnected(userId, userName);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        txtWarning.setText("חלה תקלה בשרת, נסה שנית מאוחד יותר");
                    }
                });
        queue.add(jsObjRequest);
    }

    /**
     * Load new user activity
     *
     * @param view
     */
    public void signNewUser(View view) {
        Intent intent = new Intent(this, CreateNewUserActivity.class);
        startActivity(intent);
    }

    /**
     * Load forget my password activity
     * @param view
     */
    public void forgotMyPassword(View view) {
        Intent intent = new Intent(this, ForgotMyPassActivity.class);
        startActivity(intent);
    }

    /**
     * Load connected main activity with user details
     * @param userId user id as received from the server
     * @param userName  user name as received from the server
     */
    void openConnected(int userId, String userName) {
        Intent intent = new Intent(this, MainConnectedActivity.class);
        intent.putExtra("USER_ID", userId);
        intent.putExtra("USER_NAME", userName);
        startActivity(intent);
    }

    /**
     * Override the back button to not back to the last activity
     */
    public void onBackPressed() {}

}

