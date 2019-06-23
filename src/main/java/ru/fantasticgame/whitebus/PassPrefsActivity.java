package ru.fantasticgame.whitebus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PassPrefsActivity extends AppCompatActivity {
    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_prefs);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String bus_stops = prefs.getString("bus_stops", "null");

        index = getIntent().getIntExtra("index", 0);

        String[] bus_stopsA = bus_stops.split("\n");
        List<String> stops = new ArrayList<String>();
        Spinner start = (Spinner) findViewById(R.id.start_stop);
        Spinner end = (Spinner) findViewById(R.id.end_stop);
        Collections.addAll(stops, bus_stopsA);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, stops);
        start.setAdapter(adapter);
        end.setAdapter(adapter);

        end.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                save();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        start.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                save();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void save() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String array = prefs.getString("pass_array", "");
        try {
            JSONObject jp;
            try{
                jp = new JSONObject(array);
            }catch (JSONException e){
                jp = new JSONObject();
            }
            JSONArray newpass = jp.getJSONArray("passengers");
            JSONObject info = newpass.getJSONObject(index);
            Spinner part = (Spinner) findViewById(R.id.start_stop);
            String mark = part.getSelectedItem().toString();
            Spinner part2 = (Spinner) findViewById(R.id.end_stop);
            String mark2 = part2.getSelectedItem().toString();

            info.put("start_station", mark);
            info.put("end_station", mark2);

            newpass.put(index, info);
            jp.put("passengers", newpass);

            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
            editor.putString("pass_array", jp.toString());
            editor.commit();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
