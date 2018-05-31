package com.example.ishaforenko.tictactoe.ui;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ishaforenko.tictactoe.DBHelper;
import com.example.ishaforenko.tictactoe.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScoresActivity extends ActionBarActivity {

    List<String> scores;
    ArrayAdapter<String> arrayAdapter;

    ListView scorelist;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        dbHelper = new DBHelper(this);
        scorelist = (ListView) findViewById(R.id.scorelist);

        scores = new ArrayList<String>();
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, scores);
        scorelist.setAdapter(arrayAdapter);

        fetchScores();
    }

    protected void fetchScores(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.query("tictactoe", null, null, null, null, null, null);

        if (c.moveToFirst()) {
            int idColIndex = c.getColumnIndex("id");
            int winnerColIndex = c.getColumnIndex("winner");

            do {
                if(c.getString(winnerColIndex).equals("Dead heat"))
                    scores.add(c.getInt(idColIndex) + ". Dead heat");
                else
                    scores.add(c.getInt(idColIndex) + ". " + c.getString(winnerColIndex) + " winner");

                arrayAdapter.notifyDataSetChanged();

            } while (c.moveToNext());
        }
        c.close();


        dbHelper.close();
    }
}