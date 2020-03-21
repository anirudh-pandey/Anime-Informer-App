package anirudhrocks.com.animeinformer;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    private ArrayList<Anime> animeInfoList;
    private AnimeAdapter animeAdapter;
    private AnimeDbHelper dbHelper;
    private ListView listView;
    private static final int ANIME_PROFILE_CODE = 1;
    private static final String ANIME_TITLE = "animeTitle";
    private static final String LATEST_EPISODE = "latestEpisode";
    private static final String LANGUAGE_TYPE = "languageType";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = findViewById(R.id.anime_list);

        dbHelper = new AnimeDbHelper(this);
        animeInfoList = dbHelper.getAnimeInfoList();

        animeAdapter = new AnimeAdapter(this, animeInfoList);
        listView.setAdapter(animeAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), AnimeProfile.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra(ANIME_TITLE, animeInfoList.get(position).getAnimeTitle());
                intent.putExtra(LATEST_EPISODE, animeInfoList.get(position).getLatestEpisode());
                intent.putExtra(LANGUAGE_TYPE, animeInfoList.get(position).getLanguageType());
                startActivityForResult(intent, ANIME_PROFILE_CODE);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                deleteDialogMaker(animeInfoList.get(position));
                return true;
            }
        });
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputDialogMaker();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ANIME_PROFILE_CODE && resultCode == RESULT_OK) {
            String animeTitle = data.getStringExtra(ANIME_TITLE);
            String latestEp = data.getStringExtra(LATEST_EPISODE);
            String languageType = data.getStringExtra(LANGUAGE_TYPE);
            for(Anime anime: animeInfoList) {
                if(anime.getAnimeTitle().equals(animeTitle) && anime.getLanguageType().equals(languageType)) {
                    anime.setLatestEpisode(latestEp);
                }
            }
            animeAdapter.notifyDataSetChanged();
        }
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

    private void inputDialogMaker() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Enter the anime name:");

        LayoutInflater inflater = LayoutInflater.from(this);
        View InflatedView = inflater.inflate(R.layout.activity_input_dialog, (ViewGroup) findViewById(android.R.id.content), false);

        final EditText input = InflatedView.findViewById(R.id.anime_name_input);
        final Switch dubSubToggle = InflatedView.findViewById(R.id.dub_sub_toggle);

        builder.setView(InflatedView)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (input != null) {
                            String enteredName = input.getText().toString();

                            if (!enteredName.isEmpty()) {
                                Anime anime = new Anime(enteredName, getSwitchValue(dubSubToggle.isChecked()));
                                boolean isSuccess = dbHelper.addAnimeInfo(anime);
                                if(!isSuccess) {
                                    Snackbar.make(findViewById(R.id.fab), "Similar Anime Component already exists.", Snackbar.LENGTH_SHORT).show();
                                } else {
                                    animeInfoList.add(anime);
                                    Snackbar.make(findViewById(R.id.fab), "Anime name added successfully.", Snackbar.LENGTH_SHORT).show();
                                }
                            } else {
                                Snackbar.make(findViewById(R.id.fab), "Anime Name cannot be empty", Snackbar.LENGTH_SHORT).show();
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


    private void deleteDialogMaker(final Anime anime) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Do you want to delete: " + anime.getAnimeTitle() + " ?");

        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        animeInfoList.remove(anime);
                        dbHelper.deleteAimeInfo(anime);

                        animeAdapter.notifyDataSetChanged();
                        Snackbar.make(findViewById(R.id.fab), "Anime Deleted successfully.", Snackbar.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    private String getSwitchValue(boolean switchSelection) {
        if(switchSelection)
            return "DUB";
        else
            return "SUB";
    }


}
