package com.example.rajeev.newer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

/**
 * Created by rjvjha on 11/2/18.
 */

public class NewsSourceAdapter extends ArrayAdapter<NewsSource> {

    public NewsSourceAdapter(@NonNull Context context, @NonNull List<NewsSource> newsSourceList) {
        super(context, 0, newsSourceList);
    }

    private static class ViewHolder{
        Switch newsSourceSwitch;
        TextView newsSourceDescription;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //current newsSource
        final NewsSource currentNewsSource = getItem(position);
        ViewHolder holder;
        View itemView = convertView;
        if(itemView==null){
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.news_source_item_list, parent,false);
            holder = new ViewHolder();
            holder.newsSourceDescription = itemView.findViewById(R.id.news_source_description);
            holder.newsSourceSwitch = itemView.findViewById(R.id.news_source_switch);
            itemView.setTag(holder);

        } else{
            holder = (ViewHolder)itemView.getTag();
        }
        // get string values from current object
        String newsSourceName = currentNewsSource.getSourceName();
        String newsSourceDescription = currentNewsSource.getSourceDescription();

        holder.newsSourceSwitch.setText(newsSourceName);

        // if news Source is selected toogle the switch to checked state.
        if(currentNewsSource.isNewsSourceSelected()){
            holder.newsSourceSwitch.setChecked(true);
        } else{
            holder.newsSourceSwitch.setChecked(false);
        }

        if(!newsSourceDescription.equals("null") && !TextUtils.isEmpty(newsSourceDescription) ){
            holder.newsSourceDescription.setText(newsSourceDescription);
        }
        // Implementing onCheckedChanged Listener on switch
        holder.newsSourceSwitch.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        // when switch is checked add source id to selected source Ids list else remove it.
                        String selectedNewsSourceId = currentNewsSource.getSourceId();
                        if(isChecked){
                            NewsSource.addNewSourceId(selectedNewsSourceId);
                        } else{
                            NewsSource.removeExistingNewsSourceId(selectedNewsSourceId);
                        }

                    }
                }


        );
        return itemView;
    }
}
