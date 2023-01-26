package anirudhrocks.com.animeinformer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AnimeProfile extends AppCompatActivity {

    private TextView animeTitle, latestEpisode, languageType;
    private String latestEpLink;
    private Button gotToBtn, refreshBtn;
    private AnimeDbHelper dbHelper;
    private static final String ANIME_TITLE = "animeTitle";
    private static final String LATEST_EPISODE = "latestEpisode";
    private static final String LANGUAGE_TYPE = "languageType";
    private static final String basicUrl = "https://ww4.gogoanime2.org/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime_profile);

        // getting frontend object variables
        animeTitle = findViewById(R.id.anime_title);
        latestEpisode = findViewById(R.id.latest_episode);
        languageType = findViewById(R.id.language_type);

        gotToBtn = findViewById(R.id.go_to_btn);
        refreshBtn = findViewById(R.id.refresh_btn);
        dbHelper = new AnimeDbHelper(this);

        // data recieved from MainActivity
        Intent intent = getIntent();
        final String animeTitleRcvd = intent.getStringExtra(ANIME_TITLE);
        String latestEpisodeRcvd = intent.getStringExtra(LATEST_EPISODE);
        final String languageTypeRcvd = intent.getStringExtra(LANGUAGE_TYPE);
        //-------------------------------------------------------------------

        // setting front-end objects.
        frontEndObjectSetter(animeTitleRcvd, latestEpisodeRcvd, languageTypeRcvd);
        //-----------------------------------------


        // updating latestEpisode only when new ep found
        if(latestEpisode.getText().equals("--")) {
            gotToBtn.setVisibility(View.GONE);
        } else {
            latestEpLink = linkMaker(animeTitleRcvd, latestEpisodeRcvd, languageTypeRcvd);
        }


        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LatestEpInfo epInfo = new LatestEpInfo(animeTitleRcvd, languageTypeRcvd, view);
                Thread thread = new Thread(epInfo);
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String[] infoResult = epInfo.getLatestEpInfo();
                if(infoResult[0] == null || infoResult[0].trim().isEmpty() || infoResult[1] == null || infoResult[1].trim().isEmpty()) {
                    Toast.makeText(view.getContext(), "Some Problem Occured! " +
                            "Could not find the latest episode", Toast.LENGTH_LONG).show();
                } else {
                    if(!latestEpisode.getText().equals("--") && Integer.valueOf(String.valueOf(latestEpisode.getText())) >= Integer.valueOf(infoResult[0])) {
                        Snackbar.make(view, "No new episode released yet", Snackbar.LENGTH_LONG).show();

                    } else if(latestEpisode.getText().equals("--") || (!latestEpisode.getText().equals("--")
                            && Integer.valueOf(String.valueOf(latestEpisode.getText())) < Integer.valueOf(infoResult[0]))) {
                        dbHelper.updateLatestEpisode(new Anime(animeTitleRcvd, infoResult[0], languageTypeRcvd));
                        latestEpisode.setText(infoResult[0]);
                        Snackbar.make(view, "Episode " + infoResult[0] + " added.", Snackbar.LENGTH_LONG).show();

                        gotToBtn.setVisibility(View.VISIBLE);
                        latestEpLink = infoResult[1];

                        Intent resultIntent = new Intent();
                        resultIntent.putExtra(ANIME_TITLE, animeTitle.getText());
                        resultIntent.putExtra(LATEST_EPISODE, infoResult[0]);
                        resultIntent.putExtra(LANGUAGE_TYPE, languageTypeRcvd);
                        setResult(RESULT_OK, resultIntent);
                    }
                }
            }
        });

        gotToBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uriUrl = Uri.parse(latestEpLink);
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(launchBrowser);
            }
        });
    }

    private void frontEndObjectSetter(String title, String episode, String language) {
        animeTitle.setText(title);
        latestEpisode.setText(episode);
        languageType.setText(language);
    }


    private String linkMaker(String animeTitle, String latestEp, String languageType) {
        String title = animeTitle.toLowerCase().replace(' ', '-');
        String link = basicUrl + "watch/" + title;
        if(languageType.equals("DUB")) {
            link += "-" + languageType.toLowerCase();
        }
        link += "/" + latestEpisode.getText();
        return link;
    }


}
