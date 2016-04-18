package com.contexagon.treasurehunt.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.GridLayout;
import android.widget.TextView;
import com.contexagon.treasurehunt.controller.Controller;
import com.contexagon.treasurehunt.database.CouchbaseLite;
import com.contexagon.treasurehunt.R;
import com.contexagon.treasurehunt.model.playGame.PlayGame;

import java.util.List;

/**
 * Created by ankaufma on 01.03.2016.
 */
public class PlayerStatsDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.dialog_player_stats, null);
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(v)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                       dialog.dismiss();
                    }
                });
        // Create the AlertDialog object and return it

        AlertDialog ad = builder.create();

        Log.d("WINDOW WIDTH", String.valueOf(ad.getWindow().getAttributes().width));
        Log.d("WINDOW HEIGHT", String.valueOf(ad.getWindow().getAttributes().height));

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(ad.getWindow().getAttributes());
        lp.width = 500;
        ad.getWindow().setAttributes(lp);

        Log.d("WINDOW WIDTH", String.valueOf(ad.getWindow().getAttributes().width));
        Log.d("WINDOW HEIGHT", String.valueOf(ad.getWindow().getAttributes().height));

        GridLayout gl = (GridLayout) v.findViewById(R.id.psGrid);
        int columns = 4;

        gl.removeAllViews();
        List<PlayGame> pgms = new CouchbaseLite(Controller.contextHolder).queryPlayGames("Planetarium");
        gl.setRowCount(pgms.size()+1);
        gl.setColumnCount(columns);
        TextView[][] tvArray = new TextView[pgms.size()+1][columns];
        Display display = ad.getWindow().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x / columns;
        for(int i=0; i<=pgms.size(); i++) {
            for(int j=0; j<columns; j++) {
                tvArray[i][j] = new TextView(Controller.contextHolder);
                if(i>0) {
                    switch (j) {
                        case 0:
                            tvArray[i][j].setText(pgms.get(i-1).getTitle() + " ");
                            break;
                        case 1:
                            tvArray[i][j].setText(pgms.get(i-1).getPlayerName() + " ");
                            break;
                        case 2:
                            tvArray[i][j].setText(pgms.get(i-1).getPoints() + " ");
                            break;
                        case 3:
                            tvArray[i][j].setText(pgms.get(i-1).getStartTime() + " ");
                            break;
                    }
                } else if (i==0 && j==3) {
                    tvArray[0][0].setText("Game Title");
                    tvArray[0][0].setTextAppearance(Controller.contextHolder, android.R.style.TextAppearance_Medium);
                    tvArray[0][1].setText("Player Name");
                    tvArray[0][1].setTextAppearance(Controller.contextHolder, android.R.style.TextAppearance_Medium);
                    tvArray[0][2].setText("Points");
                    tvArray[0][2].setTextAppearance(Controller.contextHolder, android.R.style.TextAppearance_Medium);
                    tvArray[0][3].setText("Date");
                    tvArray[0][3].setTextAppearance(Controller.contextHolder, android.R.style.TextAppearance_Medium);
                }
                    GridLayout.LayoutParams param = new GridLayout.LayoutParams();
                    param.height = GridLayout.LayoutParams.WRAP_CONTENT;
                    param.width = 230;
                    param.leftMargin = 0;
                    param.rightMargin = 0;
                    param.topMargin = 5;
                    param.setGravity(Gravity.CENTER);
                    param.columnSpec = GridLayout.spec(j);
                    param.rowSpec = GridLayout.spec(i);
                    tvArray[i][j].setLayoutParams(param);
                    gl.addView(tvArray[i][j]);
            }
        }

        return ad;
    }

}
