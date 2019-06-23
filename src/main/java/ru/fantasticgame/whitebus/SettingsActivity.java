package ru.fantasticgame.whitebus;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
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
        Long price = prefs.getLong("ticket_price", 25);
        String route_name = prefs.getString("route_number", "075");
        String nickname = prefs.getString("user_nickname", "Leonid_Belkin");
        String bus_stops = prefs.getString("bus_stops", "null");

        TextView price_tv = (TextView) findViewById(R.id.ticket_price);
        TextView route_number_tv = (TextView) findViewById(R.id.route_number);
        TextView nickname_tv = (TextView) findViewById(R.id.nickname);
        TextView bus_stops_tv = (TextView) findViewById(R.id.bus_stops);

        price_tv.setText(price.toString());
        route_number_tv.setText(route_name);
        nickname_tv.setText(nickname);
       // bus_stops_tv.setText(bus_stops);

        CardView price_card = (CardView) findViewById(R.id.card_ticket_price);
        CardView route_number_card = (CardView) findViewById(R.id.card_route_number);
        CardView nickname_card = (CardView) findViewById(R.id.card_nickname);
        CardView report_format_card = (CardView) findViewById(R.id.card_report_format);
        CardView bus_stops_card = (CardView) findViewById(R.id.card_bus_stops);

        price_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditPrice();
            }
        });
        route_number_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditRoteName();
            }
        });
        nickname_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditNickname();
            }
        });
        report_format_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditReportFormat();
            }
        });
        bus_stops_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditBusStops();
            }
        });
    }

    public void EditPrice(){
        final SharedPreferences.Editor settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Long price = prefs.getLong("ticket_price", 25);
        
        final AppCompatEditText edit = new AppCompatEditText(this);
        edit.setInputType(InputType.TYPE_CLASS_NUMBER);
        edit.setText(price.toString());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.ticket_price))
                .setView(edit)
                .setPositiveButton(getString(R.string.dialog_save), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Long new_price = Long.parseLong(edit.getText().toString());
                        settings.putLong("ticket_price", new_price);
                        settings.commit();
                        RefreshPrefsList();
                        dialogInterface.cancel();
                    }
                });
        builder.show();
    }

    public void EditRoteName(){
        final SharedPreferences.Editor settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String route_name = prefs.getString("route_number", "075");
        
        final AppCompatEditText edit = new AppCompatEditText(this);
        edit.setInputType(InputType.TYPE_CLASS_TEXT);
        edit.setText(route_name);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.route_number))
                .setView(edit)
                .setPositiveButton(getString(R.string.dialog_save), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String new_route_name = edit.getText().toString();
                        settings.putString("route_number", new_route_name);
                        settings.commit();
                        RefreshPrefsList();
                        dialogInterface.cancel();
                    }
                });
        builder.show();
    }

    public void EditNickname(){
        final SharedPreferences.Editor settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String nickname = prefs.getString("user_nickname", "Leonid_Belkin");

        final AppCompatEditText edit = new AppCompatEditText(this);
        edit.setInputType(InputType.TYPE_CLASS_TEXT);
        edit.setText(nickname);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.your_nickname))
                .setView(edit)
                .setPositiveButton(getString(R.string.dialog_save), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String new_nickname = edit.getText().toString();
                        settings.putString("user_nickname", new_nickname);
                        settings.commit();
                        RefreshPrefsList();
                        dialogInterface.cancel();
                    }
                });
        builder.show();
    }

    public void RefreshPrefsList(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Long price = prefs.getLong("ticket_price", 25);
        String route_name = prefs.getString("route_number", "075");
        String nickname = prefs.getString("user_nickname", "Leonid_Belkin");

        TextView price_tv = (TextView) findViewById(R.id.ticket_price);
        TextView route_number_tv = (TextView) findViewById(R.id.route_number);
        TextView nickname_tv = (TextView) findViewById(R.id.nickname);

        price_tv.setText(price.toString());
        route_number_tv.setText(route_name);
        nickname_tv.setText(nickname);
    }

    public void EditReportFormat(){
        final LinearLayout main = (LinearLayout) findViewById(R.id.settings_main);
        final LinearLayout report = (LinearLayout) findViewById(R.id.report_form);

        main.setVisibility(View.GONE);
        report.setVisibility(View.VISIBLE);

        final SharedPreferences.Editor settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String format = prefs.getString("report_format", getString(R.string.default_report_format));

        Button help = (Button) findViewById(R.id.report_help);
        Button save = (Button) findViewById(R.id.report_save);
        final EditText edit = (EditText) findViewById(R.id.report_form_editor) ;
        edit.setText(format);

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder help = new AlertDialog.Builder(SettingsActivity.this);
                help.setTitle(getString(R.string.tags_help))
                        .setMessage(getString(R.string.report_tags_help))
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                //dialogInterface.cancel();
                help.show();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String new_format = edit.getText().toString();
                settings.putString("report_format", new_format);
                settings.commit();
                main.setVisibility(View.VISIBLE);
                report.setVisibility(View.GONE);
            }
        });


    }

    public void EditBusStops(){
        final LinearLayout main = (LinearLayout) findViewById(R.id.settings_main);
        final LinearLayout report = (LinearLayout) findViewById(R.id.report_form);

        main.setVisibility(View.GONE);
        report.setVisibility(View.VISIBLE);

        final SharedPreferences.Editor settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String format = prefs.getString("bus_stops", "null");

        Button help = (Button) findViewById(R.id.report_help);
        Button save = (Button) findViewById(R.id.report_save);
        final EditText edit = (EditText) findViewById(R.id.report_form_editor) ;
        edit.setText(format);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String new_format = edit.getText().toString();
                settings.putString("bus_stops", new_format);
                settings.commit();
                main.setVisibility(View.VISIBLE);
                report.setVisibility(View.GONE);
            }
        });


    }

}
