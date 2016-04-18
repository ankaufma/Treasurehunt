package com.contexagon.treasurehunt;

import android.app.DialogFragment;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.RemoteException;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.*;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.*;
import com.contexagon.treasurehunt.asyncTasks.NativeImageAsync;
import com.contexagon.treasurehunt.controller.Controller;
import com.contexagon.treasurehunt.database.CouchbaseLite;
import com.contexagon.treasurehunt.dialogs.PlayerStatsDialog;
import com.contexagon.treasurehunt.model.siteaggregat.areas.BeaconArea;
import com.contexagon.treasurehunt.model.siteaggregat.games.Answer;
import com.contexagon.treasurehunt.model.siteaggregat.games.Game;
import com.contexagon.treasurehunt.model.siteaggregat.games.Hint;
import com.contexagon.treasurehunt.model.siteaggregat.games.Question;
import com.contexagon.treasurehunt.model.playGame.PlayGame;
import com.contexagon.treasurehunt.model.playGame.PlayGameWaypoint;
import com.contexagon.treasurehunt.model.siteaggregat.Site;
import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import org.altbeacon.beacon.*;
import org.altbeacon.beacon.Beacon;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by ankaufma on 16.02.2016.
 */
public class PlayGameActivity extends AppCompatActivity implements BeaconConsumer {

    // UI Elements
    AlphaAnimation falseAnimation = new AlphaAnimation(1, 0);
    AlphaAnimation trueAnimation = new AlphaAnimation(1, 0);
    GridLayout gl = null;
    ImageView[] ivArray = null;
    Button aw1 = null;
    Button aw2 = null;
    Button aw3 = null;
    Button aw4 = null;
    TextView clock = null;

    // Play Game Logic
    String player = null;
    PlayGame game = null;
    List<PlayGameWaypoint> pgm = null;
    PlayGameWaypoint currentWaypoint = null;
    Site site = null;
    BeaconManager bm = null;
    Region region = null;
    volatile boolean init = true;
    volatile boolean questionReady = true;
    volatile int indexImage = 0;

    // Point System
    volatile long timePoints = 0;
    int waypointsPoints = 0;
    int falsePointsSum = 0;
    int truePointsSum = 0;
    int points = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);

        aw1 = (Button) findViewById(R.id.aw1);
        aw2 = (Button) findViewById(R.id.aw2);
        aw3 = (Button) findViewById(R.id.aw3);
        aw4 = (Button) findViewById(R.id.aw4);
        clock = (TextView) findViewById(R.id.clock);

        Controller.contextHolder = this;

        initAnimations(falseAnimation); initAnimations(trueAnimation); //Initialize Button Animations
        Intent intent = getIntent();
        int index = intent.getIntExtra(StartGameActivity.GAME_INDEX, 0);
        player = intent.getStringExtra(StartGameActivity.PLAYER_NAME);
        site = Controller.getInstance(this).getCurrentSite();
        game = createGame(player, site.getGames().get(index));
        pgm = game.getPlayGameWaypoints();
        waypointsPoints = (Integer.valueOf(game.getEstimatedTime())*60) / (pgm.size() * 4);
        bm = BeaconManager.getInstanceForApplication(this);
        region = new Region("all", null, null, null);
        bm.bind(this);
        bm.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        initCollectedItemsGridView();
        initHintView(getCurrentWaypoint().getHints().get(0));
        new CountDownTimer(Integer.valueOf(game.getEstimatedTime())*60*1000, 1000){

            public void onTick(long millisUntilFinished) {
                timePoints = millisUntilFinished / 1000;
                long minutes = (millisUntilFinished / 1000) / 60;
                String secondsWithLeadingZero = String.format("%02d", (millisUntilFinished / 1000) % 60);
                clock.setText("Time remaining: " + minutes + ":" + secondsWithLeadingZero + " min");
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                clock.setText("done!");
                endGame();
            }

        }.start();
    }

    public void initCollectedItemsGridView() {
        gl = (GridLayout) findViewById(R.id.gridView);
        gl.removeAllViews();
        gl.setColumnCount(pgm.size());
        gl.setRowCount(1);
        ivArray = new ImageView[pgm.size()];
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        for(int i=0; i<ivArray.length; i++) {
            ivArray[i] = new ImageView(this);
            ivArray[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.noimage));
            GridLayout.LayoutParams param = new GridLayout.LayoutParams();
            param.height = GridLayout.LayoutParams.WRAP_CONTENT;
            param.width = width / ivArray.length;
            param.rightMargin = 5;
            param.topMargin = 5;
            param.setGravity(Gravity.CENTER);
            param.columnSpec = GridLayout.spec(i);
            param.rowSpec = GridLayout.spec(0);
            ivArray[i].setLayoutParams(param);
            gl.addView(ivArray[i]);
        }
        Log.d("Image Layout", gl.getChildCount() + "");
    }

    public void nextQuestion() {
        Log.d("REACHED", "Next Question");
        initQuestionView(getCurrentWaypoint().getQuestions().get(0));
    }

    public void checkIfTrue(View v) {
        Button b = (Button) v;
        Boolean endGame = true;
        String awToCheck = b.getText().toString();
        List<Answer> answers = getCurrentWaypoint().getQuestions().get(0).getAnswers();
        boolean isTrue = false;
        for(Answer aw: answers) {
            if(awToCheck.equals(aw.getAnswer()) && aw.getIsTrue().equals("true")) {
                isTrue = true;
                if(indexImage < ivArray.length) {
                    new NativeImageAsync(ivArray[indexImage]).execute(getCurrentWaypoint().getImages().get(0));
                    indexImage++;
                }
                currentWaypoint.setReached(true);
                for(PlayGameWaypoint wp: pgm) {
                    if(!wp.isReached()) endGame = false;
                }
                break;
            }
        }
        if(isTrue) {
            b.startAnimation(trueAnimation);
            b.getBackground().setColorFilter(0xFF00FF00, PorterDuff.Mode.MULTIPLY);
            initHintView(getCurrentWaypoint().getHints().get(0));
            truePointsSum += waypointsPoints;
            Toast.makeText(this, "+ " + waypointsPoints + " Points !", Toast.LENGTH_LONG).show();
            if(endGame) {
                endGame();
            }
        } else {
            falsePointsSum += waypointsPoints;
            Toast.makeText(this, "- " + waypointsPoints + " Points !", Toast.LENGTH_LONG).show();
            b.startAnimation(falseAnimation);
            b.getBackground().setColorFilter(0xFFFF0000, PorterDuff.Mode.MULTIPLY);
        }
    }

    public PlayGameWaypoint getCurrentWaypoint() {
        PlayGameWaypoint searchWp = null;
        for(PlayGameWaypoint notReached: pgm) {
            if (notReached.isReached() == false) {
                currentWaypoint = notReached;
                break;
            }
        }
        return currentWaypoint;
    }

    public PlayGame createGame(String player, Game game) {
        String id = "PlayGame-" + UUID.randomUUID();
        return new PlayGame(id, "PlayGame", game.getTitle(), player, PlayGame.toPlayGameWaypoints(game.getWaypoints()), game.getEstimatedTime());
    }

    public void initQuestionView(Question q) {
        TextView tv = (TextView) findViewById(R.id.hintQuestion);
        tv.setText(q.getQuestion());
        aw1.setText(q.getAnswers().get(0).getAnswer());
        aw2.setText(q.getAnswers().get(1).getAnswer());
        aw3.setText(q.getAnswers().get(2).getAnswer());
        aw4.setText(q.getAnswers().get(3).getAnswer());
        aw1.setVisibility(View.VISIBLE);
        aw2.setVisibility(View.VISIBLE);
        aw3.setVisibility(View.VISIBLE);
        aw4.setVisibility(View.VISIBLE);
        aw1.getBackground().getCurrent().clearColorFilter();
        aw2.getBackground().getCurrent().clearColorFilter();
        aw3.getBackground().getCurrent().clearColorFilter();
        aw4.getBackground().getCurrent().clearColorFilter();
    }

    public void initHintView(final Hint hint) {
        // Delay Initialization for 1,5 sec too see success Animation
        int ms=0;
        if(!init) ms=2000;
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ImageView iv = (ImageView) findViewById(R.id.imageView);
                if(getCurrentWaypoint().getImages().get(0)!=null) {
                    Log.d("IMAGE", getCurrentWaypoint().getImages().get(0));
                    new NativeImageAsync(iv).execute(getCurrentWaypoint().getImages().get(0));
                }
                aw1.setVisibility(View.INVISIBLE);
                aw2.setVisibility(View.INVISIBLE);
                aw3.setVisibility(View.INVISIBLE);
                aw4.setVisibility(View.INVISIBLE);
                TextView tv = (TextView) findViewById(R.id.hintQuestion);
                tv.setText(hint.getDescription());
                questionReady = true;
                init = false;
            }
        }, ms);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.playerStatsMenu) {
            DialogFragment df = new PlayerStatsDialog();
            df.show(getFragmentManager(), "test");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBeaconServiceConnect() {
        bm.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    for(Beacon b: beacons) {
                        Log.d("BEACON INFO", "Bluetooth Name: " + b.getBluetoothName());
                        Log.d("BEACON INFO", "Bluetooth Address: " + b.getBluetoothAddress());
                        Log.d("BEACON INFO", "Distance in meters: " + String.valueOf(b.getDistance()));
                        Log.d("BEACON INFO", "UUDI: " + b.getId1().toString());
                        Log.d("BEACON INFO", "Major: " + b.getId2().toString());
                        Log.d("BEACON INFO", "Minor: " + b.getId3().toString());
                        List<com.contexagon.treasurehunt.model.siteaggregat.beacons.Beacon> bUUID = new ArrayList<com.contexagon.treasurehunt.model.siteaggregat.beacons.Beacon>();
                        for(BeaconArea ba: currentWaypoint.getBeaconArea(site)) {
                            Log.d("BEACON IFNO", "ALIAS: "+ Controller.getBeaconByBeaconId(ba.getBeaconId()).getAlias());
                            bUUID.add(Controller.getBeaconByBeaconId(ba.getBeaconId()));
                        }
                        for(com.contexagon.treasurehunt.model.siteaggregat.beacons.Beacon bea: bUUID) {
                            Log.d("BEACON IFNO", "UUDI: "+ bea.getUuid());
                            Log.d("BEACON INFO", "UUDI: " + b.getId1().toString());
                            if(!currentWaypoint.isReached() && bea.getUuid().equals(b.getId1().toString()) && questionReady) {
                                Log.d("REACHED", currentWaypoint.getItem() + " REACHED " + String.valueOf(questionReady));
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Thread.sleep(1500);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                questionReady = false;
                                                nextQuestion();
                                            }
                                        });
                                    }
                                }.run();
                            }
                        }
                    }
                }
            }
        });

        try {
            bm.startRangingBeaconsInRegion(region);
        } catch (RemoteException e) {    }
    }

    @Override
    public void unbindService(ServiceConnection connection) {
        this.unbindService(connection);
    }

    public void initAnimations(AlphaAnimation ani) {
        ani.setDuration(300); // duration - half a second
        ani.setBackgroundColor(Color.RED);
        ani.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        ani.setRepeatCount(5); // Repeat animation infinitely
        ani.setRepeatMode(Animation.REVERSE);
    }

    public void saveGame() {
        Map<String, Object> props = new HashMap<String, Object>();
        props.put("id", game.getId());
        props.put("type", game.getType());
        props.put("title", game.getTitle());
        props.put("name", game.getPlayerName());
        props.put("waypoints", game.getPlayGameWaypoints());
        props.put("estimatedTime", game.getEstimatedTime());
        props.put("endTime", game.getEndTime());
        props.put("date", game.getStartTime());
        props.put("points", game.getPoints());
        try {
            Database cbdb = new CouchbaseLite(this).getDatabaseInstance();
            Document doc = cbdb.getDocument(game.getId());
            doc.putProperties(props);
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
    }

    public void endGame() {
        points = (int)timePoints + truePointsSum - falsePointsSum;
        game.setEndTime(String.valueOf(timePoints));
        game.setPoints(points);
        game.setStartTime(getCurrentTimeStamp());
        Log.d("POINTS SUM:", (int)timePoints + " + " + truePointsSum + " - "  +falsePointsSum);
        Toast.makeText(this, "GAME OVER WITH " + points + " POINTS", Toast.LENGTH_LONG).show();
        saveGame();
        new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finish();
            }
        }.run();
    }

    public String getCurrentTimeStamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        Date now = new Date();
        return sdf.format(now);
    }
}
