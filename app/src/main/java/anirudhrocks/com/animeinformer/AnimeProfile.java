package anirudhrocks.com.animeinformer;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class AnimeProfile extends AppCompatActivity {

    private TextView animeTitle, latestEpisode;
    private Button refreshBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime_profile);

        animeTitle = findViewById(R.id.anime_title);
        latestEpisode = findViewById(R.id.latest_episode);

        refreshBtn = findViewById(R.id.refresh_btn);

        Intent intent = getIntent();
        animeTitle.setText(intent.getStringExtra("animeTitle"));
        latestEpisode.setText(intent.getStringExtra("latestEpisode"));

        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Refresh Button CLicked.", Snackbar.LENGTH_LONG).show();
            }
        });
    }
}
