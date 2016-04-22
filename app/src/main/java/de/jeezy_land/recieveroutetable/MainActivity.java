package de.jeezy_land.recieveroutetable;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public final static String GRAPH_DATE = "de.maurice-ayasse.recieveroutetable.DATE";
    public final static String LOG_DATA = "de.maurice-ayasse.recieveroutetable.LOGDATA";

    private LogDataHandler logDataHandler = null;
    private ListView listLoggedDays = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        new recieveData().execute("http://jeezy-land.de/route/app/getLogData.php");
        listLoggedDays = (ListView) findViewById(R.id.listLoggedDays);

    }

    private void startGraphActivity(String graphDate) {
        Intent intent = new Intent(this, GraphActivity.class);
        intent.putExtra(GRAPH_DATE, graphDate);

        LogDataHandler logDataGraphDate = new LogDataHandler(logDataHandler.getLoggedDay(graphDate));
        System.out.println("Fucking size yo " + logDataGraphDate.getListLog().size());
        intent.putExtra(LOG_DATA, logDataGraphDate);
        startActivity(intent);
    }

    public void setListView() {
        //Um Die Liste in ein Array um zuwandeln muss zuerst die das Array Objekt mit der gegeben größe erstellt werden
        String[] logDatas = new String[logDataHandler.getLoggedDays().size()];
        //Das Array-Objekt wird dem to Array Aufruf übergeben und überschrieben
        logDataHandler.getLoggedDays().toArray(logDatas);
        //Der Array Adapter ist für das füllen der Liste von Nöten
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, logDatas);
        //Dieser wird nun ihr übergeben
        listLoggedDays.setAdapter(arrayAdapter);
        listLoggedDays.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String infoMessage = logDataHandler.getLoggedDays().get(position);
                String extractedDate = infoMessage.substring(infoMessage.length() - 10);
                startGraphActivity(extractedDate);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    private class recieveData extends AsyncTask<String, Void, String> {
        URL url;
        String strOutput;

        private void getData() {
            HttpURLConnection urlConnection = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                this.readData(in);
                urlConnection.disconnect();
            } catch (IOException e) {
                System.out.println("fukc you");
            }
            InputStream in = null;
        }

        private void readData(InputStream inputStream) throws IOException {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            this.strOutput = stringBuilder.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            com.google.gson.Gson gson = new Gson();
            Type listType = new TypeToken<List<LogData>>() {
            }.getType();
            List<LogData> listLog = gson.fromJson(s, listType);
            logDataHandler = new LogDataHandler(listLog);
            System.out.println(logDataHandler.getLoggedDay("2016-04-12").size());
            setListView();
        }

        @Override
        protected String doInBackground(String... url) {
            try {
                this.url = new URL(url[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            this.getData();
            return this.strOutput;
        }
    }



}
