package anirudhrocks.com.animeinformer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class AnimeAdapter extends ArrayAdapter<Anime> {

    private Context context;
    private ArrayList<Anime> animeInfoList;


    // ViewHolder class to make scrolling smoother.
    static class ViewHolder {
        TextView animeTitle;
        TextView latestEpisode;
    }


    //doubt in what to pass super??????
    public AnimeAdapter(Context context, ArrayList<Anime> animeInfoList) {
        super(context, R.layout.anime_component, animeInfoList);
        this.context = context;
        this.animeInfoList = animeInfoList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        if(convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.anime_component, parent, false);

            holder = new ViewHolder();
            holder.animeTitle = convertView.findViewById(R.id.anime_title);
            holder.latestEpisode = convertView.findViewById(R.id.latest_episode);
            // view gets stored in memory
            convertView.setTag(holder);
        } else {
            // a tag is just a way to store view in the memory.
            holder = (ViewHolder) convertView.getTag();
        }

        holder.animeTitle.setText(animeInfoList.get(position).getAnimeTitle());
        holder.latestEpisode.setText(animeInfoList.get(position).getLatestEpisode());

        return convertView;
    }

}
