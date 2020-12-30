package com.example.newsabouteuropeforandroid;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ArticleListAdapter extends RecyclerView.Adapter<ArticleListAdapter.ViewHolder> {

    private ArrayList<ArticleListData> articleListDatas;
    private Activity showArticlesActivity;
    private String mode;
    private ArticleListAdapter adapter;

    public ArticleListAdapter(ArrayList<ArticleListData> articleListDatas, Activity showArticlesActivity, String mode) {
        this.articleListDatas = articleListDatas;
        this.showArticlesActivity = showArticlesActivity;
        this.mode = mode;
        this.adapter = this;
    }

    @Override
    public ArticleListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.article_list_item, parent, false);
        ArticleListAdapter.ViewHolder viewHolder = new ArticleListAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ArticleListAdapter.ViewHolder holder, int position) {
        final ArticleListData articleData = articleListDatas.get(position);
        holder.titleTextView.setText(articleListDatas.get(position).getTitle());
        holder.authorTextView.setText(articleListDatas.get(position).getAuthor());
        holder.dateTextView.setText(articleListDatas.get(position).getDate());
        FavoriteArticles favoriteArticlesHandler = new FavoriteArticles();
        boolean isArticleInFavorites = favoriteArticlesHandler.isArticleInFavorites(articleListDatas.get(position));
        if (mode.equals("selection")){
            if (isArticleInFavorites) {
                holder.iconImageView.setImageResource(R.drawable.favorite_icon_star_full);
            } else {
                holder.iconImageView.setImageResource(R.drawable.favorite_icon_star);
            }
        } else if (mode.equals("favorites")){
            holder.iconImageView.setImageResource(R.drawable.exit);
        }
        holder.titleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = articleData.getUrl();
                Intent i = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse(url));
                showArticlesActivity.startActivity(i);
            }
        });
        holder.iconImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message;
                String status;
                if (mode.equals("selection")){
                    if (isArticleInFavorites) {
                        status = favoriteArticlesHandler.deleteArticle(articleListDatas.get(position));
                        if (status.equals("ok")){
                            message = "Deleted from favorites.";
                            adapter.notifyDataSetChanged();
                        } else {
                            message = "Sorry, problem occurred.";
                        }
                    } else {
                        status = favoriteArticlesHandler.addArticle(articleListDatas.get(position));
                        if (status.equals("ok")){
                            message = "Added to favorites.";
                            adapter.notifyDataSetChanged();
                        } else {
                            message = "Sorry, problem occurred.";
                        }

                    }
                } else {
                    status = favoriteArticlesHandler.deleteArticle(articleListDatas.get(position));
                    if (status.equals("ok")){
                        articleListDatas.clear();
                        articleListDatas = favoriteArticlesHandler.loadFavoriteArticles();
                        message = "Deleted.";
                        adapter.notifyDataSetChanged();
                    } else {
                        message = "Sorry, problem occurred.";
                    }
                }

                showArticlesActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(showArticlesActivity.getApplicationContext(),
                                message,
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return articleListDatas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public TextView authorTextView;
        public TextView dateTextView;
        public ImageView iconImageView;
        public LinearLayout linearLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            this.titleTextView = (TextView) itemView.findViewById(R.id.articleTitle);
            this.authorTextView = (TextView) itemView.findViewById(R.id.author);
            this.dateTextView = (TextView) itemView.findViewById(R.id.date);
            this.iconImageView = (ImageView) itemView.findViewById(R.id.addFavorites);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.articleListLayout);
        }
    }
}
