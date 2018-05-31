package com.example.ishaforenko.tictactoe.ui;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ishaforenko.tictactoe.DBHelper;
import com.example.ishaforenko.tictactoe.R;
import com.example.ishaforenko.tictactoe.game.Game;

import java.util.ArrayList;
import java.util.List;

public class GameActivity extends ActionBarActivity {
    private Game game;
    private List<Button> buttons;

    protected DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        initGameBoard();
        game = new Game(3);

        dbHelper = new DBHelper(this);
    }

    private void initGameBoard() {
        LinearLayout gameBoard = (LinearLayout) findViewById(R.id.gameBoard);
        buttons = new ArrayList<>();
        for (int i = 0; i < gameBoard.getChildCount(); i++) {
            if (gameBoard.getChildAt(i) instanceof LinearLayout) {
                LinearLayout layout = (LinearLayout) gameBoard.getChildAt(i);
                for (int j = 0; j < layout.getChildCount(); j++) {
                    Button button = (Button) layout.getChildAt(j);
                    buttons.add(button);
                    button.setOnClickListener(new GameBoardButtonOnClickListener(j, i));
                }
            }
        }
    }

    private void cleanUpBoard() {
        for (Button button : buttons) {
            button.setText("");
            button.setClickable(true);
            //button.setEnabled(true);
        }
    }

    private class GameBoardButtonOnClickListener implements View.OnClickListener {
        private int x = 0;
        private int y = 0;


        public GameBoardButtonOnClickListener(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public void onClick(View view) {
            Game.Player player = game.getCurrentPlayer();
            game.makeTurn(x, y);

            Button button = (Button) view;
            button.setText(player.toString());
            if (player.equals(Game.Player.PLAYER_1)) {
                button.setTextColor(Color.BLACK);
            } else {
                button.setTextColor(Color.WHITE);
            }
            //button.setEnabled(false);
            button.setClickable(false);
            //button.setAlpha(1);

            if (game.isFinished()) {
                Game.Player winner = game.getWinner();
                Toast toast;

                if (winner == null) {
                    toast = Toast.makeText(getApplicationContext(), getString(R.string.no_one_won), Toast.LENGTH_SHORT);

                    saveWinner(getString(R.string.no_one_won));
                } else {
                    toast = Toast.makeText(getApplicationContext(), getString(R.string.player_won, winner.toString()), Toast.LENGTH_SHORT);

                    saveWinner(winner.toString());
                }
                toast.show();
                game.reset();
                cleanUpBoard();
            }
        }

        protected void saveWinner(String winner){
            ContentValues cv = new ContentValues();

            SQLiteDatabase db = dbHelper.getWritableDatabase();

            cv.put("winner", winner);

            db.insert("tictactoe", null, cv);

            dbHelper.close();
        }
    }
}