package com.contexagon.treasurehunt;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.*;
import android.view.View;
import android.widget.*;
import com.contexagon.treasurehunt.controller.Controller;
import com.contexagon.treasurehunt.database.CouchbaseLite;
import com.contexagon.treasurehunt.dialogs.PlayerStatsDialog;

import com.contexagon.treasurehunt.model.siteaggregat.Site;

import android.util.Log;
import com.contexagon.treasurehunt.model.siteaggregat.games.Game;


public class StartGameActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Site site = null;
    private int choosenGame = 0;
    private String player = null;
    public static final String PLAYER_NAME = "player_name";
    public static final String GAME_INDEX = "game_index";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_game);
        new CouchbaseLite(this);
        site = Controller.getInstance(this).getCurrentSite();
        Controller.contextHolder = this;

        // Android Logic
        TextView playground = (TextView) findViewById(R.id.playGround);
        playground.setText(site.getName());

        Spinner spinner = (Spinner) findViewById(R.id.gameSpinner);
        String[] gameName = new String[site.getGames().size()];
        int i = 0;
        for(Game game: site.getGames()) {
            gameName[i] = game.getTitle();
            i++;
        }
        ArrayAdapter<String> aaGames = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, gameName);
        aaGames.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(aaGames);
        spinner.setOnItemSelectedListener(this);
    }

    public void startGame(View v) {
        Intent startGame = new Intent(this, PlayGameActivity.class);
        EditText name = (EditText) findViewById(R.id.playername);
        player = name.getText().toString();
        startGame.putExtra(GAME_INDEX, choosenGame);
        startGame.putExtra(PLAYER_NAME, player);
        startActivity(startGame);
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
            openDialog();
            return true;
        } else if (id == R.id.showMap) {
            Intent startMap = new Intent(this, MapsActivity.class);
            startActivity(startMap);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d("ARRAYADAPTER parent", parent.toString());
        Log.d("ARRAYADAPTER View", view.toString());
        Log.d("ARRAYADAPTER position", String.valueOf(position));
        Log.d("ARRAYADAPTER long", String.valueOf(id));
        Game game = site.getGames().get(position);
        choosenGame = position;
        String gameInfos =
                "<u>Game Title:</u> <br>"
                        + game.getTitle() + " <br><br>" +
                "<u>Game Description:</u> <br>"
                        + game.getDescription() + " <br><br>" +
                "<u>Estimated Time:</u> <br>"
                        + game.getEstimatedTime() + " <br><br>" +
                "<u>No of Items to collect:</u> <br>"
                        + game.getiTemsToCount();
        TextView gameDetails = (TextView) findViewById(R.id.gameDetails);
        gameDetails.setText(Html.fromHtml(gameInfos));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Log.d("ARRAYADAPTER parent", parent.toString());
    }

    @Override
    public void onRestart() {
        super.onRestart();
        Controller.contextHolder = this;
        openDialog();
    }

    private void openDialog() {
        DialogFragment df = new PlayerStatsDialog();
        df.show(getFragmentManager(), "test");
    }

}
