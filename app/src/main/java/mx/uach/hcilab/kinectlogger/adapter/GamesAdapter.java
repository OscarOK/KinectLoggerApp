package mx.uach.hcilab.kinectlogger.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import mx.uach.hcilab.kinectlogger.GameCard;
import mx.uach.hcilab.kinectlogger.R;

public class GamesAdapter extends RecyclerView.Adapter<GamesAdapter.ViewHolderGames> {

    private ArrayList<GameCard> gameCards;

    public GamesAdapter(ArrayList<GameCard> gameCards) {
        this.gameCards = gameCards;
    }

    @NonNull
    @Override
    public ViewHolderGames onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.game_item, null, false);
        return new ViewHolderGames(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderGames holder, int position) {
        holder.setData(gameCards.get(position));
    }

    @Override
    public int getItemCount() {
        return gameCards.size();
    }

    public class ViewHolderGames extends RecyclerView.ViewHolder {

        private final Context context;
        private ImageView thumbnail;
        private TextView gameTitle;
        private Button buttonPlay;


        public ViewHolderGames(View itemView) {
            super(itemView);
            context = itemView.getContext();
            gameTitle = itemView.findViewById(R.id.game_title);
            thumbnail = itemView.findViewById(R.id.game_thumbnail);
            buttonPlay = itemView.findViewById(R.id.play_game);
        }

        public void setData(GameCard gameCard) {
            final Intent intent = gameCard.getIntent();
            thumbnail.setBackgroundResource(gameCard.getIdThumbnail());
            gameTitle.setText(gameCard.getTitle());
            buttonPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(intent);
                }
            });
        }
    }
}
