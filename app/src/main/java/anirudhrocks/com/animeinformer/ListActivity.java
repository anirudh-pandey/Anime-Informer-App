package anirudhrocks.com.animeinformer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    private ArrayList<Anime> animeInfoList;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = findViewById(R.id.anime_list);

        animeInfoList = new ArrayList<>();

        AnimeAdapter animeAdapter = new AnimeAdapter(this, animeInfoList);
        listView.setAdapter(animeAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), AnimeProfile.class);

                intent.putExtra("animeTitle", animeInfoList.get(position).getAnimeTitle());
                intent.putExtra("latestEpisode", animeInfoList.get(position).getLatestEpisode());
                startActivity(intent);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogMaker();
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
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
            Toast.makeText(ListActivity.this, "Settings clicked", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void dialogMaker() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Enter the anime name:");

        LayoutInflater inflater = LayoutInflater.from(this);
        View InflatedView = inflater.inflate(R.layout.activity_dialog, (ViewGroup) findViewById(android.R.id.content), false);

        final EditText input = InflatedView.findViewById(R.id.anime_name_input);
        builder.setView(InflatedView)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (input != null) {
                            String enteredName = input.getText().toString();
                            if (!enteredName.isEmpty()) {
                                Anime anime = new Anime(enteredName);
                                animeInfoList.add(anime);
                                Snackbar.make(findViewById(R.id.fab), "Anime name added successfully.", Snackbar.LENGTH_SHORT).show();
                            } else if (enteredName.isEmpty()) {
                                Snackbar.make(findViewById(R.id.fab), "Anime could not be added.", Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }


}
