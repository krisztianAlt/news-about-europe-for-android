package com.example.newsabouteuropeforandroid;

import android.app.Activity;
import android.content.Intent;
import android.service.voice.VoiceInteractionSession;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;

public class CountryListAdapter extends RecyclerView.Adapter<CountryListAdapter.ViewHolder>{
    private CountryListData[] countryListData;
    private RadioGroup newsAgencyRadioButtonGroup;
    private Activity mainActivity;

    // RecyclerView recyclerView;
    public CountryListAdapter(CountryListData[] listData, RadioGroup newsAgencyRadioButtonGroup, Activity main) {
        this.countryListData = listData;
        this.newsAgencyRadioButtonGroup = newsAgencyRadioButtonGroup;
        this.mainActivity = main;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.country_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final CountryListData countryData = countryListData[position];
        holder.textView.setText(countryListData[position].getCountryName());
        holder.imageView.setImageResource(countryListData[position].getImgId());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Get selected radio button (selected news agency):
                int radioButtonID = newsAgencyRadioButtonGroup.getCheckedRadioButtonId();
                View radioButton = newsAgencyRadioButtonGroup.findViewById(radioButtonID);
                int idx = newsAgencyRadioButtonGroup.indexOfChild(radioButton);
                RadioButton r = (RadioButton) newsAgencyRadioButtonGroup.getChildAt(idx);
                String selectedAgency = r.getText().toString();

                // Start article listing:
                ShowArticlesActivity.updateActivity(mainActivity);
                Intent intent = new Intent(mainActivity.getApplicationContext(), ShowArticlesActivity.class);
                intent.putExtra("selectedAgency", selectedAgency);
                intent.putExtra("selectedCountry", countryData.getCountryName());
                mainActivity.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return countryListData.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;
        public RelativeLayout relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            this.textView = (TextView) itemView.findViewById(R.id.countryName);
            this.imageView = (ImageView) itemView.findViewById(R.id.countryFlag);
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.countryListRelativeLayout);
        }
    }
}
