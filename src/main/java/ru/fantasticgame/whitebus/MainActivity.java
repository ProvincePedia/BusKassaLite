package ru.fantasticgame.whitebus;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.CardView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity
        implements NavigationView.OnNavigationItemSelectedListener {
    TimerTask timerTask;
    Timer timer;
    final Handler handler = new Handler();

    final String NOTIFICATION_CHANNEL_IN_WORK = "inworknotification";


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        AppCompatButton btn_start = (AppCompatButton) findViewById(R.id.btn_start);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Start(view);
            }
        });

        AppCompatButton btn_reset = (AppCompatButton) findViewById(R.id.btn_reset);
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ResetDialog(view);
            }
        });

        AppCompatButton btn_add_pass = (AppCompatButton) findViewById(R.id.btn_add_pass);
        btn_add_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPass(view);
            }
        });

        AppCompatButton btn_add_route = (AppCompatButton) findViewById(R.id.btn_add_route);
        btn_add_route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRoute(view);
            }
        });

        AppCompatButton btn_stop = (AppCompatButton) findViewById(R.id.btn_stop);
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Stop(view);
            }
        });

        navigationView.getMenu().getItem(0).setChecked(true);
        toolbar.setTitle(R.string.title_activity_main);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if(prefs.getBoolean("work_started", false)){
            if(timer == null){
                Start(new View(getApplicationContext()));
            }
            btn_start.setClickable(false);
            btn_start.setEnabled(false);
            btn_reset.setClickable(false);
            btn_reset.setEnabled(false);

            btn_add_pass.setClickable(true);
            btn_add_pass.setEnabled(true);
            btn_add_route.setClickable(true);
            btn_add_route.setEnabled(true);
            btn_stop.setClickable(true);
            btn_stop.setEnabled(true);
        }else{
            btn_start.setClickable(true);
            btn_start.setEnabled(true);
            btn_reset.setClickable(true);
            btn_reset.setEnabled(true);

            btn_add_pass.setClickable(false);
            btn_add_pass.setEnabled(false);
            btn_add_route.setClickable(false);
            btn_add_route.setEnabled(false);
            btn_stop.setClickable(false);
            btn_stop.setEnabled(false);
        }
        TextView work_start = (TextView) findViewById(R.id.work_start);
        TextView work_end = (TextView) findViewById(R.id.work_end);
        TextView work_time = (TextView) findViewById(R.id.work_time);
        TextView passengers = (TextView) findViewById(R.id.passengers);
        TextView routes = (TextView) findViewById(R.id.routes);
        TextView money = (TextView) findViewById(R.id.money);
        Long start = prefs.getLong("start_time", 0);
        Long end = prefs.getLong("end_time", 0);
        Long diff = (end - start)/1000;
        Long hours = diff/3600;
        Long minutes = (diff - (3600*hours))/60;

        Long passengersL = prefs.getLong("passengers", 0);
        Long routesL = prefs.getLong("routes", 0);
        Long moneyL = prefs.getLong("money", 0);

        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
        Long milies = System.currentTimeMillis();
        editor.putLong("end_time", milies);
        editor.commit();

        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String end_time = sdf.format(milies);
        String start_time = sdf.format(start);

        work_end.setText(end_time);
        work_start.setText(start_time);
        work_time.setText(hours.toString() + " часов " + minutes.toString() + " минут");

        passengers.setText(passengersL.toString());
        routes.setText(routesL.toString());
        money.setText(moneyL.toString());

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        final View main = (View) findViewById(R.id.main);
        final ScrollView costs_report = (ScrollView) findViewById(R.id.costs_report_SV);
        final ScrollView pass_report = (ScrollView) findViewById(R.id.pass_report_SV);
        final View report = (View) findViewById(R.id.report);
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (id == R.id.nav_main) {
            report.setVisibility(View.GONE);
            main.setVisibility(View.VISIBLE);
            costs_report.setVisibility(View.GONE);
            pass_report.setVisibility(View.GONE);
            toolbar.setTitle(R.string.title_activity_main);
        } else if (id == R.id.nav_report) {
            if(prefs.getBoolean("work_started", false)){
                AlertDialog.Builder registration_alert = new AlertDialog.Builder(MainActivity.this);
                registration_alert.setTitle("Завершить работу?")
                        .setCancelable(false)
                        .setMessage("Вы уверены, что хотите завершить работу и открыть отчёт? После завершения работы данные остануться в кэше и вы сможете возобновить работу, нажав на кнопку \"Начать работу\", но возможы баги и потеря данных!")
                        .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                        .setPositiveButton("Завершить работу и открыть отчёт", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                report.setVisibility(View.VISIBLE);
                                main.setVisibility(View.GONE);
                                pass_report.setVisibility(View.GONE);
                                costs_report.setVisibility(View.GONE);
                                createReport();
                                Stop(new View(MainActivity.this));
                            }
                        });
                AlertDialog alert = registration_alert.create();
                alert.show();
            }else {
                report.setVisibility(View.VISIBLE);
                costs_report.setVisibility(View.GONE);
                main.setVisibility(View.GONE);
                pass_report.setVisibility(View.GONE);
                createReport();
                toolbar.setTitle(R.string.today_report);
            }

        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_costs_report) {
            report.setVisibility(View.GONE);
            costs_report.setVisibility(View.VISIBLE);
            main.setVisibility(View.GONE);
            pass_report.setVisibility(View.GONE);
            createCostsReport();
            toolbar.setTitle(R.string.today_costs_report);
        }else if (id == R.id.nav_pass_report) {
            report.setVisibility(View.GONE);
            costs_report.setVisibility(View.GONE);
            main.setVisibility(View.GONE);
            createPassReport();
            pass_report.setVisibility(View.VISIBLE);
            toolbar.setTitle(R.string.today_pass_report);
        } else if (id == R.id.nav_add_costs) {
            addCosts();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onPause(){
        super.onPause();
        Notify();
    }

    public void onResume(){
        super.onResume();
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
        editor.putBoolean("need_notify", false);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(101);
        editor.commit();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            notificationManager.deleteNotificationChannel(NOTIFICATION_CHANNEL_IN_WORK);
        }
    }

    public void Notify(){
        Intent intent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        Long passengers = prefs.getLong("passengers", 0);
        Long profit = prefs.getLong("money", 0);
        Long routesL = prefs.getLong("routes", 0);

        Long start = prefs.getLong("start_time", 0);
        Long end = prefs.getLong("end_time", 0);
        Long diff = (end - start)/1000;
        Long hours = diff/3600;
        Long minutes = (diff - (3600*hours))/60;
        String work_time;
        if(hours == 0) {
            work_time = minutes.toString() + " минут";
        }else{
            work_time = hours.toString() + " часов " + minutes.toString() + " минут";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String start_time = sdf.format(start);

        String text = "Начало работы: " + start_time +
                "\nВремя в работе: " + work_time +
                "\nПассажиры: " + passengers.toString() +
                "\nКругорейсы: " + routesL.toString() +
                "\nЗаработок: " + profit.toString();

        Uri ringURI =
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);



        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_IN_WORK, "My Notifications", NotificationManager.IMPORTANCE_DEFAULT);

            // Configure the notification channel.
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setSound(null, null);
            notificationChannel.setVibrationPattern(new long[] { 0 });
            notificationChannel.enableVibration(false);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_IN_WORK)
                .setSmallIcon(R.drawable.ic_bus)
                .setContentTitle("Content Title")
                .setContentText(text)
                .setSound(null)
                .setDefaults(0)
                .setVibrate(new long[] { 0 })
                .setColor(getResources().getColor(R.color.colorAccent))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(text));


        // Creates an explicit intent for an Activity in your app
        Intent intenSt = getApplicationContext().getPackageManager().getLaunchIntentForPackage(getApplicationContext().getPackageName());
        PendingIntent resultPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intenSt, 0);



        builder.setContentIntent(resultPendingIntent);




        // mId allows you to update the notification later on.
       // mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(text));
        //mNotificationManager.notify(228, mBuilder.build());
        if(prefs.getBoolean("work_started", false)) {
            mNotificationManager.notify(101, builder.build());
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
            editor.putBoolean("need_notify", true);
            editor.commit();
        }
    }

    public void ResetDialog(View v){
        AlertDialog.Builder registration_alert = new AlertDialog.Builder(MainActivity.this);
        registration_alert.setTitle("Обнулить статистику?")
                .setCancelable(false)
                .setMessage("Вы уверены, что хотите обнулить данные? После обнуления все данные будут стерты с устройства без возможности восстановления!")
                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("Обнулить", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Reset();
                    }
                });
        AlertDialog alert = registration_alert.create();
        alert.show();
    }

    public void Reset(){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
        TextView work_start = (TextView) findViewById(R.id.work_start);
        TextView work_end = (TextView) findViewById(R.id.work_end);
        TextView work_time = (TextView) findViewById(R.id.work_time);
        TextView passengers = (TextView) findViewById(R.id.passengers);
        TextView routes = (TextView) findViewById(R.id.routes);
        TextView money = (TextView) findViewById(R.id.money);

        work_start.setText("03:00");
        work_end.setText("03:00");
        work_time.setText("0 часов 00 минут");
        passengers.setText("0");
        routes.setText("0");
        money.setText("0");

        editor.putBoolean("work_started", false);
        editor.putString("costs_array", "");
        editor.putString("pass_array", "");
        editor.putLong("start_time", 0);
        editor.putLong("end_time", 0);
        editor.putLong("passengers", 0);
        editor.putLong("routes", 0);
        editor.putLong("money", 0);
        editor.putLong("costs", 0);
        editor.commit();
    }

    public void Start(View v){
        AppCompatButton btn_start = (AppCompatButton) findViewById(R.id.btn_start);
        btn_start.setClickable(false);
        btn_start.setEnabled(false);
        AppCompatButton btn_reset = (AppCompatButton) findViewById(R.id.btn_reset);
        btn_reset.setClickable(false);
        btn_reset.setEnabled(false);

        AppCompatButton btn_add_pass = (AppCompatButton) findViewById(R.id.btn_add_pass);
        btn_add_pass.setClickable(true);
        btn_add_pass.setEnabled(true);
        AppCompatButton btn_add_route = (AppCompatButton) findViewById(R.id.btn_add_route);
        btn_add_route.setClickable(true);
        btn_add_route.setEnabled(true);
        AppCompatButton btn_stop = (AppCompatButton) findViewById(R.id.btn_stop);
        btn_stop.setClickable(true);
        btn_stop.setEnabled(true);


        timer = new Timer();
        initializeTimerTask();
        timer.schedule(timerTask,0, 10000);
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


        Long milies = System.currentTimeMillis();

        editor.putBoolean("work_started", true);
        if(prefs.getLong("start_time", milies) == 0){
            editor.putLong("start_time", milies);
        }else{
            editor.putLong("start_time", prefs.getLong("start_time", milies));
        }

        editor.putLong("end_time", prefs.getLong("end_time", milies));
        editor.putLong("passengers",  prefs.getLong("passengers", 0));
        editor.putLong("routes",  prefs.getLong("routes", 0));
        editor.putLong("money",  prefs.getLong("money", 0));
        editor.commit();
    }

    public void Stop(View v){
        AppCompatButton btn_start = (AppCompatButton) findViewById(R.id.btn_start);
        btn_start.setClickable(true);
        btn_start.setEnabled(true);
        AppCompatButton btn_reset = (AppCompatButton) findViewById(R.id.btn_reset);
        btn_reset.setClickable(true);
        btn_reset.setEnabled(true);

        AppCompatButton btn_add_pass = (AppCompatButton) findViewById(R.id.btn_add_pass);
        btn_add_pass.setClickable(false);
        btn_add_pass.setEnabled(false);
        AppCompatButton btn_add_route = (AppCompatButton) findViewById(R.id.btn_add_route);
        btn_add_route.setClickable(false);
        btn_add_route.setEnabled(false);
        AppCompatButton btn_stop = (AppCompatButton) findViewById(R.id.btn_stop);
        btn_stop.setClickable(false);
        btn_stop.setEnabled(false);

        timer.purge();
        timerTask.cancel();
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
        editor.putBoolean("work_started", false);
        editor.commit();
    }


    @SuppressLint("StaticFieldLeak")

    public void Kassa (){

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            Long start = prefs.getLong("start_time", 0);
            Long end = System.currentTimeMillis();
            Long diff = (end - start)/1000;
            Long hours = diff/3600;
            Long minutes = (diff - (3600*hours))/60;

            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
            Long milies = System.currentTimeMillis();
            editor.putLong("end_time", milies);
            editor.commit();

            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            String end_time = sdf.format(milies);
            String start_time = sdf.format(start);

            TextView work_end = (TextView) findViewById(R.id.work_end);
            TextView work_start = (TextView) findViewById(R.id.work_start);
            TextView work_time = (TextView) findViewById(R.id.work_time);
            work_end.setText(end_time);
            work_start.setText(start_time);
            work_time.setText(hours.toString() + " часов " + minutes.toString() + " минут");
            if(prefs.getBoolean("need_notify", false)) {
                Notify();
            }
    }
    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Kassa();
                    }
                });
            }
        };


    }

    public void addPass(View v){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Long pass = prefs.getLong("passengers", 0);
        Long new_pass = pass+1;
        Long money = prefs.getLong("money", 0);
        Long price = prefs.getLong("ticket_price", 25);
        String pass_list = prefs.getString("pass_array", "");
        Long new_money = money+price;
        String new_pass_list = "";
        Long millis = System.currentTimeMillis();
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();

        try {
            JSONArray array;

            JSONObject jp;
            try{
                jp = new JSONObject(pass_list);
            }catch (JSONException e){
                jp = new JSONObject();
            }
            JSONObject newpass = new JSONObject();
            try{
                array = jp.getJSONArray("passengers");
            }catch (JSONException e){
                array = new JSONArray();
            }
            newpass.put("start_station", "null").put("end_station", "null").put("time", millis);
            array.put(newpass);
            jp.put("passengers", array);
            editor.putString("pass_array", jp.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            new_pass_list = "{}";
        }



        editor.putLong("passengers", new_pass);
        editor.putLong("money", new_money);
        editor.commit();

        TextView passengers = (TextView) findViewById(R.id.passengers);
        TextView money_tv = (TextView) findViewById(R.id.money);
        passengers.setText(new_pass.toString());
        money_tv.setText(new_money.toString());
    }

    public void addRoute(View v){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Long routes = prefs.getLong("routes", 0);
        Long new_routes = routes+1;

        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
        editor.putLong("routes", new_routes);
        editor.commit();

        TextView routes_tv = (TextView) findViewById(R.id.routes);
        routes_tv.setText(new_routes.toString());
    }

    public void createReport(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String report = prefs.getString("report_format", getString(R.string.default_report_format));

        String route = prefs.getString("route_number", "075");

        Long passengers = prefs.getLong("passengers", 0);
        Long profit = prefs.getLong("money", 0);
        Long costs = prefs.getLong("costs", 0);
        Long routesL = prefs.getLong("routes", 0);

        Long start = prefs.getLong("start_time", 0);
        Long end = prefs.getLong("end_time", 0);
        Long diff = (end - start)/1000;
        Long hours = diff/3600;
        Long minutes = (diff - (3600*hours))/60;
        String work_time;
        if(hours == 0) {
            work_time = minutes.toString() + " минут";
        }else{
            work_time = hours.toString() + " часов " + minutes.toString() + " минут";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String start_time = sdf.format(start);
        String end_time = sdf.format(end);

        SimpleDateFormat date = new SimpleDateFormat("dd MMMM YYYY");
        String end_date = date.format(end);
        Long diff_cost = profit - costs;

        String report_text = report.replace("{date}", end_date)
                .replace("{end_time}", end_time)
                .replace("{start_time}", start_time)
                .replace("{work_time}", work_time)
                .replace("{route}", route)
                .replace("{passengers}", passengers.toString())
                .replace("{routes}", routesL.toString())
                .replace("{costs}", costs.toString())
                .replace("{summ}", diff_cost.toString())
                .replace("{profit}", profit.toString() + " руб.");
        TextView report_tv = (TextView) findViewById(R.id.report_textview);
        report_tv.setText(report_text);
    }

    public void createCostsReport(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String array = prefs.getString("costs_array", "");
        Long costs = prefs.getLong("costs", 0);
        //Double costs = Double.parseDouble(costsS);
        String[] cost = array.split(";");
        LinearLayout report = (LinearLayout) findViewById(R.id.costs_report);
        LinearLayout no_costs = (LinearLayout) findViewById(R.id.no_costs);
        report.removeAllViews();
        int n = 0;
        if(costs == 0){
            report.setVisibility(View.GONE);
            no_costs.setVisibility(View.VISIBLE);
        }else {
            report.setVisibility(View.VISIBLE);
            no_costs.setVisibility(View.GONE);
            for (String aCost : cost) {
                n++;
                String[] info = aCost.split(",");
                LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                View Post = inflater.inflate(R.layout.post, (ViewGroup) report);

                CardView card = (CardView) findViewById(R.id.post);
                TextView title = (TextView) card.findViewById(R.id.title);
                TextView cost_name = (TextView) card.findViewById(R.id.cost_name);
                TextView date = (TextView) card.findViewById(R.id.date);
                TextView cost_tv = (TextView) card.findViewById(R.id.cost);
                TextView note = (TextView) card.findViewById(R.id.note);
                card.setId(n);

                cost_name.setText(info[0]);
                cost_tv.setText(info[1]);

                SimpleDateFormat format = new SimpleDateFormat("dd.MM.YYYY в HH:mm:ss");
                date.setText(format.format(Long.parseLong(info[2])));
                title.setText("Отчет о расходах #" + String.valueOf(n));

                //title.setId(R.id.title + n);
                //cost_name.setId(R.id.cost_name + n);
                // date.setId(R.id.date + n);
                //cost_tv.setId(R.id.cost + n);
                //note.setId(R.id.note + n);
            }
        }
    }


    public void createPassReport(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String array = prefs.getString("pass_array", "");
        Long passengers = prefs.getLong("passengers", 0);
        //Double costs = Double.parseDouble(costsS);
        //String[] cost = array.split(";");
        LinearLayout report = (LinearLayout) findViewById(R.id.pass_report);
        LinearLayout no_costs = (LinearLayout) findViewById(R.id.no_costs);
        report.removeAllViews();
        int n = 0;
        if(passengers == 0){
            report.setVisibility(View.GONE);
            no_costs.setVisibility(View.VISIBLE);
        }else {
            report.setVisibility(View.VISIBLE);
            no_costs.setVisibility(View.GONE);
            try {
                JSONObject jp;
                try{
                    jp = new JSONObject(array);
                }catch (JSONException e){
                    jp = new JSONObject();
                }
                JSONArray newpass = jp.getJSONArray("passengers");
                //  jp.put("passengers", newpass.put("first_station", "null").put("end_station", "null").put("time", millis));
                while (n<passengers) {

                    JSONObject info = newpass.getJSONObject(n);
                    LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                    View Post = inflater.inflate(R.layout.passenger, (ViewGroup) report);
                    n++;

                    CardView card = (CardView) findViewById(R.id.passenger);
                    TextView title = (TextView) card.findViewById(R.id.title);
                    TextView passenger_add = (TextView) card.findViewById(R.id.passenger_add);
                    TextView date = (TextView) card.findViewById(R.id.date);
                    TextView passenger_remove = (TextView) card.findViewById(R.id.passenger_remove);
                    TextView note = (TextView) card.findViewById(R.id.note);
                    card.setId(n);
                    final int finalN = n;
                    card.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(MainActivity.this, PassPrefsActivity.class);
                            intent.putExtra("index", finalN-1);
                            startActivity(intent);
                        }
                    });

                    passenger_add.setText(info.getString("start_station"));
                    passenger_remove.setText(info.getString("end_station"));

                    SimpleDateFormat format = new SimpleDateFormat("dd.MM.YYYY в HH:mm:ss");
                    date.setText(format.format(info.getLong("time")));
                    title.setText("Пассажир #" + String.valueOf(n));

                    //title.setId(R.id.title + n);
                    //cost_name.setId(R.id.cost_name + n);
                    // date.setId(R.id.date + n);
                    //cost_tv.setId(R.id.cost + n);
                    //note.setId(R.id.note + n);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void addCosts(){
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        final SharedPreferences.Editor settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();

        //final String costs_list = prefs.getString("costs_array", "");
        final Long costs = prefs.getLong("costs", 0);
        //final String new_costs_list;

        final AppCompatEditText name = new AppCompatEditText(this);
        name.setInputType(InputType.TYPE_CLASS_TEXT);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.costs_name))
                .setView(name)
                .setPositiveButton(getString(R.string.dialog_next), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        final AppCompatEditText cost = new AppCompatEditText(MainActivity.this);
                        cost.setInputType(InputType.TYPE_CLASS_NUMBER);
                        builder.setTitle(getString(R.string.cost))
                                .setView(cost)
                                .setPositiveButton(getString(R.string.dialog_save), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        String new_cost = cost.getText().toString();
                                        String cost_name = prefs.getString("temphash", "noname");
                                        settings.putLong("costs", Long.parseLong(new_cost) + costs);
                                        String new_costs_list;
                                        Long milies = System.currentTimeMillis();

                                        String costs_list = prefs.getString("costs_array", "");
                                        if(costs > 0){
                                            new_costs_list = costs_list + ";" + cost_name + "," + new_cost + "," + milies.toString();
                                        }else{
                                            new_costs_list = cost_name + "," + new_cost + "," + milies.toString();
                                        }
                                        settings.putString("costs_array", new_costs_list);
                                        settings.commit();
                                        dialogInterface.cancel();
                                    }
                                });
                        builder.show();
                        settings.putString("temphash", name.getText().toString());
                        settings.commit();
                        dialogInterface.cancel();
                    }
                });
        builder.show();
    }
}
