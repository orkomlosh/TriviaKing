package com.example.sadna.triviaking;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

/**
 * GameActivity the activity that manage the game itself
 */
public class GameActivity extends AppCompatActivity {


    //region Variables
    TextView quesTxt, timerTxt, foeNameTxt, turnTxt, difficultyTxt, scoreTxt, scoreQuesTxt;
    Button[] buttons = new Button[4];
    Button rightAnswer;
    CountDownTimer mCounter;
    ProgressBar pBar;
    int roomId;
    String foeName;
    int userId;
    String userName;
    String question;
    ArrayList<String> answers = new ArrayList<String>();
    int difficulty;
    int generalScore = 0;
    int quesScore = 0;
    boolean myTurn;
    MediaPlayer correctSound;
    MediaPlayer loseSound;
    MediaPlayer winSound;
    MediaPlayer ticSound;
    //endregion

    /**
     * onCreate basic method that run when the activity created and initialize the activity needs
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //region Bonding the variables with the view objects.
        quesTxt = (TextView) findViewById(R.id.txtQuestion);
        buttons = new Button[]{
                (Button) findViewById(R.id.btnAnswer1),
                (Button) findViewById(R.id.btnAnswer2),
                (Button) findViewById(R.id.btnAnswer3),
                (Button) findViewById(R.id.btnAnswer4)};

        timerTxt = (TextView) findViewById(R.id.txtTimer);
        pBar = (ProgressBar) findViewById(R.id.prgsBarTimer);
        foeNameTxt = (TextView) findViewById(R.id.txtFoeName);
        turnTxt = (TextView) findViewById(R.id.txtTurn);
        difficultyTxt = (TextView) findViewById(R.id.txtDifficulty);
        scoreTxt = (TextView) findViewById(R.id.txtScore);
        scoreQuesTxt = (TextView) findViewById(R.id.txtQuesScore);
        correctSound = MediaPlayer.create(this,R.raw.correct);
        loseSound = MediaPlayer.create(this,R.raw.lose);
        winSound = MediaPlayer.create(this, R.raw.win);
        ticSound = MediaPlayer.create(this,R.raw.tic);
        //endregion

        //gets the details from previous activity about the game foe and user
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userName = extras.getString("USER_NAME");
            userId = extras.getInt("USER_ID");
            foeName = extras.getString("FOE_NAME");
            roomId = extras.getInt("ROOM_ID");
        }

        foeNameTxt.setText(foeNameTxt.getText() + foeName);
        //start with the first questions
        nextQuestion();
    }

    /**
     * start the flow of getting the next question
     */
    public void nextQuestion() {
        getReadyForNext();
        GetQuestion();
    }

    /**
     * sending next question request from the server, and store it.
     */
    private void GetQuestion() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Utils.getHttpAddress() + "Trivia/webapi/game/" + roomId + "/question";

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject jsonQuestion = Utils.jsonFieldToJSONObj(response, "question");
                        myTurn = (Utils.jsonFieldToInt(response, "refferdId") == userId);
                        if (jsonQuestion != null) {
                            question = Utils.jsonFieldToString(jsonQuestion, "question");
                            difficulty = Utils.jsonFieldToInt(jsonQuestion, "difficulty");
                            answers.add(Utils.jsonFieldToString(jsonQuestion, "correctAns"));
                            answers.add(Utils.jsonFieldToString(jsonQuestion, "wrongAns1"));
                            answers.add(Utils.jsonFieldToString(jsonQuestion, "wrongAns2"));
                            answers.add(Utils.jsonFieldToString(jsonQuestion, "wrongAns3"));
                            //after getting the question continue with the flow
                            SetQuestion();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        queue.add(jsObjRequest);
    }

    /**
     * Init the activity variables and view for the next question
     */
    public void getReadyForNext() {
        timerTxt.setVisibility(View.INVISIBLE);
        timerTxt.setTextColor(Color.BLUE);
        pBar.setVisibility(View.INVISIBLE);
        quesTxt.setVisibility(View.INVISIBLE);
        difficultyTxt.setVisibility(View.INVISIBLE);
        scoreQuesTxt.setText("");
        scoreTxt.setText("ניקוד: " + generalScore);

        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setVisibility(View.INVISIBLE);
            buttons[i].setText("");
            buttons[i].setBackgroundColor(Color.LTGRAY);
            buttons[i].setClickable(true);
            buttons[i].setAlpha(1f);
        }
        answers.clear();
    }

    /**
     * sets the question information and continue flow
     */
    public void SetQuestion() {
        quesTxt.setText(question);
        difficultyTxt.setText("רמת קושי: " + difficulty);
        SetAnswersRandomly();
        ShowQuestion();
    }

    /**
     * sets the answers randonly on the buttons
     */
    private void SetAnswersRandomly() {
        int nextAnswerBtn;
        Random rand = new Random();
        int btnUsed = 0;
        for (int i = 0; i < 4; i++) {
            while (btnUsed == i) {
                nextAnswerBtn = rand.nextInt(4);
                if (buttons[nextAnswerBtn].getText() == "") {
                    buttons[nextAnswerBtn].setText(answers.get(i));
                    if (i == 0) {
                        rightAnswer = buttons[nextAnswerBtn];
                    }
                    btnUsed++;
                }
            }
        }
    }

    /**
     * show the question first and continue with flow
     */
    public void ShowQuestion() {
        quesTxt.setVisibility(View.VISIBLE);
        timerTxt.setVisibility(View.VISIBLE);
        difficultyTxt.setVisibility(View.VISIBLE);
        SetTurn();
        mCounter = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerTxt.setText(String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                ShowAnswersAndStart();
            }
        }.start();
    }

    /**
     * checks who need to play and make the game ready for the the turn
     */
    public void SetTurn() {
        if (myTurn) {
            turnTxt.setText("השאלה הבאה מופנית אליך!");
        } else {
            turnTxt.setText("השאלה הבאה מופנית ל: " + foeName);
            for (int i = 0; i < buttons.length; i++) {
                buttons[i].setClickable(false);
                buttons[i].setAlpha(0.5f);
            }
        }
        turnTxt.setVisibility(View.VISIBLE);
    }

    /**
     * show the answers and start the game timer
     */
    public void ShowAnswersAndStart() {
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setVisibility(View.VISIBLE);
        }
        StartTimer();
    }

    /**
     * start the timer and if not the user turn send wait to the server
     */
    public void StartTimer() {
        pBar.setVisibility(View.VISIBLE);
        mCounter = new CountDownTimer(15000, 1000) {

            public void onFinish() {
                if (myTurn) {
                    loseSound.start();
                    pBar.setVisibility(View.INVISIBLE);
                    timerTxt.setText("הזמן נגמר!");
                    turnTxt.setText("הפסדת, נסה שוב!");
                    timesUp();
                }
            }

            public void onTick(long MillisUntilFinished) {
                timerTxt.setText(String.valueOf(MillisUntilFinished / 1000));
                quesScore = difficulty * 25 * (int) (MillisUntilFinished / 1000);
                ticSound.start();
                scoreQuesTxt.setText("+" + quesScore);
                if (MillisUntilFinished <= 6000) {
                    timerTxt.setTextColor(Color.RED);
                }
            }
        }.start();
        if (!myTurn) {
            sendWaitForFoeRequest();
        }
    }

    /**
     * when time is up send indication ans score to the server
     */
    public void timesUp() {
        sendAnswerToServer(0);
        sendScoreToServer("lose");
        startDelayBeforeQuit();
    }

    /**
     * when the user press one of the answers checking the answer
     *
     * @param view
     */
    public void checkAnswer(View view) {
        Button pressed = (Button) view;
        //send the answer to the server
        sendAnswerToServer(answers.indexOf(pressed.getText()) + 1);
        mCounter.cancel();
        rightAnswer.setBackgroundColor(Color.GREEN);
        pBar.setVisibility(View.INVISIBLE);
        timerTxt.setVisibility(View.INVISIBLE);

        if (view.getId() == rightAnswer.getId()) {
            //right answer
            correctSound.start();
            turnTxt.setText("כל הכבוד תשובה נכונה!");
            for (int i = 0; i < buttons.length; i++) {
                buttons[i].setClickable(false);
                if ((buttons[i] != rightAnswer)) {
                    buttons[i].setAlpha(0.5f);
                }
            }
            //adds the question score to general score
            generalScore = generalScore + quesScore;
            startDelayBeforeNextQues();
        } else {
            //wrong answer
            loseSound.start();
            turnTxt.setText("תשובה לא נכונה!");
            view.setBackgroundColor(Color.RED);
            for (int i = 0; i < buttons.length; i++) {
                buttons[i].setClickable(false);
                if ((buttons[i] != rightAnswer) && (buttons[i] != view)) {
                    buttons[i].setAlpha(0.5f);
                }
            }
            sendScoreToServer("lose");
            startDelayBeforeQuit();
        }
    }

    /**
     * start timer delay before next question for user to see the information and start next question
     * when done
     */
    public void startDelayBeforeNextQues() {
        mCounter = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                nextQuestion();
            }
        }.start();
    }

    /**
     * start delay before quitting when user lost the game
     */
    public void startDelayBeforeQuit() {
        mCounter = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                backToMainMenu();
            }
        }.start();

    }

    /**
     * start the main activity
     */
    public void backToMainMenu() {
        Intent intent = new Intent(this, MainConnectedActivity.class);
        intent.putExtra("USER_ID", userId);
        intent.putExtra("USER_NAME", userName);
        startActivity(intent);
    }

    /**
     * send indication to the server that the user is waiting for the foe turn to end
     */
    public void sendWaitForFoeRequest() {
        String URL = Utils.getHttpAddress() + "Trivia/webapi/game/" + roomId + "/answer";

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mCounter.cancel();
                        int answer = Utils.jsonFieldToInt(response, "answerNumber");
                        if (answer == 1) {//the foe answered right
                            pBar.setVisibility(View.INVISIBLE);
                            timerTxt.setVisibility(View.INVISIBLE);
                            rightAnswer.setBackgroundColor(Color.GREEN);
                            turnTxt.setText("היריב ענה תשובה נכונה! מיד נמשיך..");
                            startDelayBeforeNextQues();
                        } else {//the foe answered wrong
                            gameOver(answer);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                gameOver(-1);
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
     * when the foe answered wrong or there was error on the server
     *
     * @param answer the indication to what happened
     */
    public void gameOver(int answer) {
        winSound.start();
        switch (answer) {
            case -2://the for left the game
                turnTxt.setText("היריב יצא מהמשחק, ניצחת! כל הכבוד!");
                break;
            case -1://error on the game
                turnTxt.setText("הייתה תקלה במשחק מיד תצא למסך הראשי..");
                break;
            case 0://times up for the foe
                turnTxt.setText("ליריב תם הזמן, ניצחת! כל הכבוד!");
                break;
            //the foe answered wrong
            case 2:
            case 3:
            case 4:
                turnTxt.setText("היריב טעה בתשובה, ניצחת! כל הכבוד");
                showFoeWrongAnswer(answer);
                break;
        }
        //the user gets bonus for winning
        generalScore = generalScore + difficulty * 100;
        sendScoreToServer("win");
        startDelayBeforeQuit();
    }

    /**
     * show the foe wrong answer
     * @param answer
     */
    public void showFoeWrongAnswer(int answer) {
        rightAnswer.setBackgroundColor(Color.GREEN);
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setClickable(false);
            if (answers.indexOf(buttons[i].getText()) == answer - 1)
                buttons[i].setBackgroundColor(Color.RED);
            else if (buttons[i] != rightAnswer) {
                buttons[i].setAlpha(0.5f);
            }
        }
    }

    /**
     * send the score to the server when the game ends.
     * @param gameStatus indication to the server if "win" or "lose"
     */
    public void sendScoreToServer(String gameStatus) {
        scoreQuesTxt.setVisibility(View.INVISIBLE);
        scoreTxt.setTextColor(Color.GREEN);

        String URL = Utils.getHttpAddress() + "Trivia/webapi/score/" + userId + "/" + generalScore + "/" + gameStatus;

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        return;
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                return;
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
     * send the answer number to the server
     * @param answer answer number
     */
    public void sendAnswerToServer(int answer) {
        String URL = Utils.getHttpAddress() + "Trivia/webapi/game/" + roomId + "/" + answer;

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        return;
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                return;
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
     * Override when back button is pressed send indication and score to the server and start
     * main activity
     */
    @Override
    public void onBackPressed() {
        sendAnswerToServer(-2);
        sendScoreToServer("lose");
        backToMainMenu();
    }

}
