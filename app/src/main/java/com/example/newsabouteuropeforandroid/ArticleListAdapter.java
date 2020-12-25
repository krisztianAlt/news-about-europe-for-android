package com.example.newsabouteuropeforandroid;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ArticleListAdapter extends RecyclerView.Adapter<ArticleListAdapter.ViewHolder> {

    private ArrayList<ArticleListData> articleListDatas;
    private RadioGroup newsAgencyRadioButtonGroup;
    private Activity showArticlesActivity;

    // RecyclerView recyclerView;
    public ArticleListAdapter(ArrayList<ArticleListData> articleListDatas, Activity showArticlesActivity) {
        this.articleListDatas = articleListDatas;
        this.showArticlesActivity = showArticlesActivity;
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
        holder.titleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Clicked: " + articleData.getTitle(), Toast.LENGTH_LONG).show();
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
        public RelativeLayout relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            this.titleTextView = (TextView) itemView.findViewById(R.id.articleTitle);
            this.authorTextView = (TextView) itemView.findViewById(R.id.author);
            this.dateTextView = (TextView) itemView.findViewById(R.id.date);
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.articleListRelativeLayout);
        }
    }
}
