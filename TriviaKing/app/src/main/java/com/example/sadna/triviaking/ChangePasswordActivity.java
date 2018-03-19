package com.example.sadna.triviaking;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import java.util.HashMap;
import java.util.Map;

/**
 * ChangePasswordActivity is used to change the user password
 */
public class ChangePasswordActivity extends AppCompatActivity {


    //region Variables
    EditText edtTxtOldPass, edtTxtNewPass, edtTxtRptNewPass;
    Button btnSend;
    TextView txtWarning;
    String userName;
    int userId;
    //endregion

    /**
     * onCreate basic method that run when the activity created and initialize the activity needs
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);


        //region Bonding the variables with the view objects
        edtTxtOldPass = (EditText) findViewById(R.id.edtOldTxtPassword);
        edtTxtNewPass = (EditText) findViewById(R.id.edtNewTxtPassword);
        edtTxtRptNewPass = (EditText) findViewById(R.id.edtTxtRepeatPassword);
        btnSend = (Button) findViewById(R.id.btnSendUserPassword);
        txtWarning = (TextView) findViewById(R.id.txtWarn);
        //endregion

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userName = extras.getString("USER_NAME");
            userId = extras.getInt("USER_ID");
        }
    }

    /**
     * When pressing the send button, validate data and send to server
     *
     * @param view
     */
    public void sendNewPassword(View view) {
        txtWarning.setText("");
        validateDetailsAndSend();
    }

    /**
     * validating the new password and sending the date to server
     */
    public void validateDetailsAndSend() {


        //Password validation
        if (edtTxtNewPass.getText().length() > 10 || edtTxtRptNewPass.getText().length() < 4) {
            txtWarning.setText("הסיסמא החדשה לא עומדת בתנאים!");
            return;
        }

        //password match validation
        if (!edtTxtNewPass.getText().toString().equals(edtTxtRptNewPass.getText().toString())) {
            txtWarning.setText("הסיסמאות שהוקשו לא תואמות!");
            edtTxtNewPass.setText("");
            edtTxtRptNewPass.setText("");
            return;
        }

        //sending the data
        txtWarning.setText("שולח נתונים לשרת");
        sendDataToServer();
    }

    /**
     * Sending the new password to the server
     */
    public void sendDataToServer() {
        String URL = Utils.getHttpAddress() + "Trivia/webapi/users?id=" + userId + "&newPass=" +
                Utils.hash(edtTxtNewPass.getText().toString()) + "&oldPass=" + Utils.hash(edtTxtOldPass.getText().toString());

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(new JsonObjectRequest(Request.Method.PUT, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (Utils.jsonFieldToBoolean(response, "success")) {
                            txtWarning.setText("הסיסמא שונתה בהצלחה");
                            timer();
                        }
                        else
                        {
                            txtWarning.setText("הסיסמא הישנה לא תואמת למה שקיים במערכת");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                txtWarning.setText("חלה תקלה בשרת, אנא נסה שנית מאוחר יותר");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        });
    }

    public void timer()
    {
        CountDownTimer mCounter = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                goToMainMenu();
            }
        }.start();
    }

    /**
     * Open main menu with the user info
     */
    public void goToMainMenu() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("USER_ID", userId);
        intent.putExtra("USER_NAME", userName);
        startActivity(intent);
    }
}
