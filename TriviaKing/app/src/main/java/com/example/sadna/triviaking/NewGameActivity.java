package com.example.sadna.triviaking;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
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
 * NewGameActivity serach for new game and connect
 */
public class NewGameActivity extends AppCompatActivity {


    //region variables
    String userName;
    String foeName;
    int userId;
    int roomId;
    TextView headline;
    TextView downLine;
    ProgressBar progressBar;
    //endregion

    /**
     * onCreate basic method that run when the activity created and initialize the activity needs
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);

        //region Bonding the variables with the view objects.
        headline = (TextView) findViewById(R.id.txtSearchForGame);
        downLine = (TextView) findViewById(R.id.txtSoon);
        progressBar = (ProgressBar) findViewById(R.id.prgsBarTimer);
        //endregion

        //Load the user details from previous activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userName = extras.getString("USER_NAME");
            userId = extras.getInt("USER_ID");
        }

        sendPlayRequest();
    }

    /**
     * sends play request for the user and start the game if finds opponent
     */
    public void sendPlayRequest() {
        String URL = Utils.getHttpAddress() + "Trivia/webapi/game/" + userId;

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        roomId = Utils.jsonFieldToInt(response, "room");
                        if (roomId > 0) {//if got valid room id
                            foeName = Utils.jsonFieldToString(response, "foeName");
                            headline.setText("נמצא משחק, נתחיל מיד");
                            downLine.setVisibility(View.INVISIBLE);
                            CountDownTimer mCounter = new CountDownTimer(3000, 1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {
                                }

                                @Override
                                public void onFinish() {
                                    startGame();
                                }
                            }.start();
                        } else {//if a game wasn't found
                            if (roomId == -2) {
                            }
                            headline.setText("לא נמצא משחק אנא נסה שנית..");
                            downLine.setVisibility(View.INVISIBLE);
                            progressBar.setVisibility(View.INVISIBLE);
                            getBackMainMenu();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                headline.setText("תקלה בשרת נסה מאוחר יותר..");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonObjectRequest);
    }

    /**
     * Start Game activity with all the needed details
     */
    public void startGame() {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("USER_ID", userId);
        intent.putExtra("USER_NAME", userName);
        intent.putExtra("ROOM_ID", roomId);
        intent.putExtra("FOE_NAME", foeName);
        startActivity(intent);
    }

    /**
     * Get back to main menu after the failed tt get a game or canceled by the user
     */
    public void getBackMainMenu() {
        Intent intent = new Intent(this, MainConnectedActivity.class);
        intent.putExtra("USER_ID", userId);
        intent.putExtra("USER_NAME", userName);
        startActivity(intent);
    }

    /**
     * Override the method when pressing the back button, sending cancel request to server
     */
    public void onBackPressed() {
        String URL = Utils.getHttpAddress() + "Trivia/webapi/game/" + userId;

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        headline.setText("בקשת הביטול נשלחה לשרת");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonObjectRequest);
        getBackMainMenu();
    }
}
