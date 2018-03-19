package com.example.sadna.triviaking;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;

/**
 * ForgotMyPassActivity, screen to reset user password
 */
public class ForgotMyPassActivity extends AppCompatActivity {

    //region variables
    EditText edtTxtEmail;
    Button btnSend;
    TextView txtWarning;
    //endregion

    /**
     * onCreate basic method that run when the activity created and initialize the activity needs
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_my_pass);

        //region Bonding the variables with the view objects.
        edtTxtEmail = (EditText) findViewById(R.id.edtTxtMail);
        btnSend = (Button) findViewById(R.id.btnSend);
        txtWarning = (TextView) findViewById(R.id.txtWarn);
        //endregion
    }


    /**
     * When pressing the send button, sending email to reset the password.
     *
     * @param view
     */
    public void resetPass(View view) {
        txtWarning.setText("");
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Utils.getHttpAddress() + "Trivia/webapi/users/forgot?email=" + edtTxtEmail.getText().toString();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (Utils.jsonFieldToBoolean(response,"exist"))
                        {
                            txtWarning.setVisibility(View.VISIBLE);
                            txtWarning.setText("סיסמה חדשה נשלחה למייל שלך!");
                        }
                        else
                        {
                            txtWarning.setText("המייל שהוקש לא קיים במערכת");
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        txtWarning.setText("חלה תקלה בשרת, נסה שנית מאוחד יותר");
                    }
                });
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsObjRequest);
    }
}

