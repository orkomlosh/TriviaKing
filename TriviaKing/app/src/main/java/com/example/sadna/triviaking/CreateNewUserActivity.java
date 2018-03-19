package com.example.sadna.triviaking;

import android.content.Intent;
import android.os.Bundle;
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
 * CreateNewUserActivity is the activity to create new user for the game
 */
public class CreateNewUserActivity extends AppCompatActivity {


    //region Variables
    EditText edtTxtUsername, edtTxtPass, edtTxtRptPass, edtTxtEmail;
    Button btnSend;
    TextView txtWarning;
    boolean bool;
    //endregion

    /**
     * onCreate basic method that run when the activity created and initialize the activity needs
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_user);


        //region Bonding the variables with the view objects
        edtTxtEmail = (EditText) findViewById(R.id.edtTxtMail);
        edtTxtUsername = (EditText) findViewById(R.id.edtTxtUserName);
        edtTxtPass = (EditText) findViewById(R.id.edtTxtPassword);
        edtTxtRptPass = (EditText) findViewById(R.id.edtTxtRepeatPassword);
        btnSend = (Button) findViewById(R.id.btnSendUserData);
        txtWarning = (TextView) findViewById(R.id.txtWarn);
        //endregion
    }

    /**
     * When pressing the send button, validate data and send to server
     *
     * @param view
     */
    public void sendUserData(View view) {
        txtWarning.setText("");
        validateDataAndSend();
    }

    /**
     * Validating the mail and continue the flow
     */
    public void validateDataAndSend() {
        if (!Utils.isValidEmail(edtTxtEmail.getText())) {
            txtWarning.setText("המייל שהוקש אינו תקין, נסה שנית");
            return;
        }
        isMailExist();
    }

    /**
     * checking if mail exist already, if it's not continue validating the details.
     */
    public void isMailExist() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Utils.getHttpAddress() + "Trivia/webapi/users?email=" + edtTxtEmail.getText().toString();

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        bool = Utils.jsonFieldToBoolean(response, "exist");
                        if (!bool) {
                            //not exist on the server
                            txtWarning.setText("");
                            //continue to validate the details
                            validateDetailsAndSend();
                        } else {
                            //exist on the server
                            txtWarning.setText("המייל קיים במערכת אין אפשרות לצרפו בשנית");
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
     * validating the other details and sending the date to server
     */
    public void validateDetailsAndSend() {

        //User name validation
        if (edtTxtUsername.getText().length() == 0 || edtTxtUsername.getText().length() > 10) {
            txtWarning.setText("הכינוי לא יכול להיות ריק או לעלות על 10 תווים");
            return;
        }

        //Password validation
        if (edtTxtPass.getText().length() > 10 || edtTxtPass.getText().length() < 4) {
            txtWarning.setText("הסיסמא לא עומדת בתנאים!");
            return;
        }

        //password match validation
        if (!edtTxtPass.getText().toString().equals(edtTxtRptPass.getText().toString())) {
            txtWarning.setText("הסיסמאות שהוקשו לא תואמות!");
            edtTxtPass.setText("");
            edtTxtRptPass.setText("");
            return;
        }

        //sending the data
        txtWarning.setText("שולח נתונים לשרת");
        sendDataToServer();
    }

    /**
     * Sending the new user details to the server
     */
    public void sendDataToServer() {
        String URL = Utils.getHttpAddress() + "Trivia/webapi/users?email=" + edtTxtEmail.getText().toString() + "&name=" +
                edtTxtUsername.getText().toString() + "&pass=" + Utils.hash(edtTxtPass.getText().toString());

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(new JsonObjectRequest(Request.Method.POST, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // user successfully created
                        txtWarning.setText("המשתמש נוצר בהצלחה");
                        goToMainMenu(response);
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

    /**
     * Parsing the user details, saving it as preferences and start the main connected activity
     * @param user
     */
    public void goToMainMenu(JSONObject user) {
        Utils.savePreferences(CreateNewUserActivity.this.getApplicationContext(), "USER_NAME", Utils.jsonFieldToString(user, "name"));
        Utils.saveIntPreferences(CreateNewUserActivity.this.getApplicationContext(), "USER_ID", Utils.jsonFieldToInt(user, "id"));
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
