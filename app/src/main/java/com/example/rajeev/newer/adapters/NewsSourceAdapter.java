package com.example.rajeev.newer.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.TextView;

import com.example.rajeev.newer.R;
import com.example.rajeev.newer.custom_classes.NewsSource;

import java.util.List;

/**
 * Created by rjvjha on 11/2/18.
 */

public class NewsSourceAdapter extends ArrayAdapter<NewsSource> {
    private static final String LOG_TAG = NewsSourceAdapter.class.getName();
    private List<String> savedSourcesIdList;


    public NewsSourceAdapter(@NonNull Context context, @NonNull List<NewsSource> newsSourceList, List<String> savedSourcesIdList) {
        super(context, 0, newsSourceList);
        this.savedSourcesIdList = savedSourcesIdList;
    }

    private void onSwitchClickEvent(final Switch switchToggleButton, final String newsSourceId) {
        switchToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (switchToggleButton.isChecked()) {
                    // only add newsSource which are not selected
                    NewsSource.addNewSourceId(newsSourceId);
                } else {
                    NewsSource.removeExistingNewsSourceId(newsSourceId);
                }
            }
        });

    }


    private static class ViewHolder{
        Switch newsSourceSwitch;
        TextView newsSourceDescription;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //current newsSource
        NewsSource currentNewsSource = getItem(position);
        ViewHolder holder;
        View itemView = convertView;
        if(itemView==null){
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.news_source_item_list, parent,false);
            holder = new ViewHolder();
            holder.newsSourceDescription = itemView.findViewById(R.id.news_source_description);
            holder.newsSourceSwitch = itemView.findViewById(R.id.news_source_switch);
            NewsSource.setSelectedSourceIds(savedSourcesIdList);
            itemView.setTag(holder);

        } else{
            holder = (ViewHolder) itemView.getTag();
        }

        // get string values from current object
        String newsSourceName = currentNewsSource.getSourceName();
        final String newsSourceId = currentNewsSource.getSourceId();
        String newsSourceDescription = currentNewsSource.getSourceDescription();

        holder.newsSourceSwitch.setText(newsSourceName);

       // Log.v(LOG_TAG,"isNewsSourceSelected: "+ currentNewsSource.isNewsSourceSelected() + " " + newsSourceName);

        // if news Source is selected toogle the switch to checked state.
        if(currentNewsSource.isNewsSourceSelected()){
            holder.newsSourceSwitch.setChecked(true);
        }else{
            holder.newsSourceSwitch.setChecked(false);
        }

        if(!newsSourceDescription.equals("null") && !TextUtils.isEmpty(newsSourceDescription) ){
            holder.newsSourceDescription.setText(newsSourceDescription);
        }

        onSwitchClickEvent(holder.newsSourceSwitch,newsSourceId);

        return itemView;
    }
}
