package com.example.sadna.triviaking;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *ScoreActivity, shows the user statistics and the leader board of the whole game
 */
public class ScoreActivity extends AppCompatActivity {

    //region Variables
    int userId;
    String userName;
    TextView txtUserName, txtTotalScore, txtWins, txtLoses, txtRank;
    TableLayout tblScoreTable;
    //endregion

    /**
     * onCreate basic method that run when the activity created and initialize the activity needs
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        //region Bonding the variables with the view objects.
        txtUserName = (TextView) findViewById(R.id.txtUsername);
        txtTotalScore = (TextView) findViewById(R.id.txtTotalScore);
        txtWins = (TextView) findViewById(R.id.txtWins);
        txtLoses = (TextView) findViewById(R.id.txtLoses);
        txtRank = (TextView) findViewById(R.id.txtRank);
        tblScoreTable = (TableLayout) findViewById(R.id.tblScoreBoard);
        //endregion

        // get the user details from previous activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userName = extras.getString("USER_NAME");
            userId = extras.getInt("USER_ID");
        }

        //gets data and show
        GetUserData();
        ShowLeaderBoard();
    }

    /**
     * Gets the user score data from server and show it
     */
    public void GetUserData() {
        txtUserName.setText(userName);
        String URL = Utils.getHttpAddress() + "Trivia/webapi/score/" + userId;

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        txtTotalScore.setText("" + Utils.jsonFieldToInt(response, "score"));
                        txtWins.setText("" + Utils.jsonFieldToInt(response, "wins"));
                        txtLoses.setText("" + Utils.jsonFieldToInt(response, "loses"));
                        txtRank.setText("" + Utils.jsonFieldToInt(response, "rank"));

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                txtTotalScore.setText("n/a");
                txtWins.setText("n/a");
                txtLoses.setText("n/a");
                txtRank.setText("n/a");
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
     * gets the leader board information and show it
     */
    public void ShowLeaderBoard() {

        //region Table and head row init
        TableRow row0 = new TableRow(this);
        row0.setBackgroundColor(Color.LTGRAY);
        TextView col01 = new TextView(this);
        TextView col02 = new TextView(this);
        TextView col03 = new TextView(this);
        TextView col04 = new TextView(this);
        TextView col05 = new TextView(this);
        col01.setText(" Rank ");
        col01.setTextSize(22);
        col01.setGravity(Gravity.CENTER);
        row0.addView(col01);
        col02.setText(" Name ");
        col02.setTextSize(22);
        col02.setGravity(Gravity.CENTER);
        row0.addView(col02);
        col03.setText(" Score ");
        col03.setTextSize(22);
        col03.setGravity(Gravity.CENTER);
        row0.addView(col03);
        col04.setText(" Wins ");
        col04.setTextSize(22);
        col04.setGravity(Gravity.CENTER);
        row0.addView(col04);
        col05.setText(" Loses ");
        col05.setTextSize(22);
        col05.setGravity(Gravity.CENTER);
        row0.addView(col05);
        tblScoreTable.addView(row0);
        //endregion

        String URL = Utils.getHttpAddress() + "Trivia/webapi/score/";

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject obj = response.getJSONObject(i);

                                TableRow row = new TableRow(ScoreActivity.this);
                                //Gets the rank col
                                TextView col1 = new TextView(ScoreActivity.this);
                                col1.setText(String.format("%d", Utils.jsonFieldToInt(obj, "rank")));
                                col1.setGravity(Gravity.CENTER);
                                col1.setTextSize(20);
                                row.addView(col1);
                                //Gets the name col
                                TextView col2 = new TextView(ScoreActivity.this);
                                col2.setText(String.format(Utils.jsonFieldToString(obj, "name")));
                                col2.setGravity(Gravity.CENTER);
                                col2.setTextSize(21);
                                col2.setTextColor(Color.BLUE);
                                row.addView(col2);
                                //Gets the score col
                                TextView col3 = new TextView(ScoreActivity.this);
                                col3.setText(String.format("%d", Utils.jsonFieldToInt(obj, "score")));
                                col3.setGravity(Gravity.CENTER);
                                col3.setTextSize(21);
                                col3.setTextColor(Color.GREEN);
                                row.addView(col3);
                                //Gets the wins col
                                TextView col4 = new TextView(ScoreActivity.this);
                                col4.setText(String.format("%d", Utils.jsonFieldToInt(obj, "wins")));
                                col4.setGravity(Gravity.CENTER);
                                col4.setTextSize(20);
                                row.addView(col4);
                                //Gets the loses col
                                TextView col5 = new TextView(ScoreActivity.this);
                                col5.setText(String.format("%d", Utils.jsonFieldToInt(obj, "loses")));
                                col5.setGravity(Gravity.CENTER);
                                col5.setTextSize(20);
                                row.addView(col5);

                                tblScoreTable.addView(row);
                            }
                        } catch (final JSONException e) {

                        }
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
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonArrayRequest);
    }
}


