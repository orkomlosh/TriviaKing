package com.example.sadna.triviaking;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * MainConnectedActivity, main activity for logged users
 */
public class MainConnectedActivity extends AppCompatActivity {


    //region Variables
    Button btnSearch, btnScoreBoards;
    TextView txtLogOut;
    TextView txtConnectedAs;
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
        setContentView(R.layout.activity_main_connected);

        //region Bonding the variables with the view objects.
        btnSearch = (Button) findViewById(R.id.btnSearchForGame);
        btnScoreBoards = (Button) findViewById(R.id.btnScoreBoards);
        txtLogOut = (TextView) findViewById(R.id.txtLogOut);
        txtConnectedAs = (TextView) findViewById(R.id.txtConnectedAs);
        //endregion

        //getting the saved user details
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userName = extras.getString("USER_NAME");
            userId = extras.getInt("USER_ID");
        }

        txtConnectedAs.setText(txtConnectedAs.getText().toString() + userName);
    }

    /**
     * open the search for game activity
     *
     * @param view
     */
    public void searchForGame(View view) {
        Intent intent = new Intent(this, NewGameActivity.class);
        intent.putExtra("USER_ID", userId);
        intent.putExtra("USER_NAME", userName);
        startActivity(intent);
    }

    /**
     * open score board activity
     *
     * @param view
     */
    public void openScoreBoards(View view) {
        Intent intent = new Intent(this, ScoreActivity.class);
        intent.putExtra("USER_NAME", userName);
        intent.putExtra("USER_ID", userId);
        startActivity(intent);
    }

    /**
     * Delete the user details from saved preferences and load main activity
     *
     * @param view
     */
    public void logOutUser(View view) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * Open the Change user password screen
     * @param view
     */
    public void ChangePassword(View view)
    {
        Intent intent = new Intent(this, ChangePasswordActivity.class);
        intent.putExtra("USER_NAME", userName);
        intent.putExtra("USER_ID", userId);
        startActivity(intent);
    }

    /**
     * override the back button to not back to the last activity
     */
    public void onBackPressed() {

    }
}


