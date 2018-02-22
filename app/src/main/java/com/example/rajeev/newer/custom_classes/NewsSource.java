package com.example.rajeev.newer.custom_classes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rjvjha on 11/2/18.
 */

public class NewsSource {
    private static List<String> selectedSourceIds;
    public static boolean isNewsSourcesIdChanged;
    private String sourceId;
    private String sourceName;
    private String sourceDescription;
    private String sourceUrl;
    private String sourceCategory;
    private String sourceLanguage;
    private String sourceCountry;

    public NewsSource(String sourceId, String sourceName, String sourceDescription, String sourceUrl) {
        this.sourceId = sourceId;
        this.sourceName = sourceName;
        this.sourceDescription = sourceDescription;
        this.sourceUrl = sourceUrl;
    }

    public NewsSource(String sourceId, String sourceName, String sourceDescription, String sourceUrl, String sourceCategory, String sourceLanguage, String sourceCountry) {
        this.sourceId = sourceId;
        this.sourceName = sourceName;
        this.sourceDescription = sourceDescription;
        this.sourceUrl = sourceUrl;
        this.sourceCategory = sourceCategory;
        this.sourceLanguage = sourceLanguage;
        this.sourceCountry = sourceCountry;
    }

    static {
        selectedSourceIds = new ArrayList<>(20);
    }

    public static List<String> getSelectedSourceIds() {
        return selectedSourceIds;
    }

    public String getSourceId() {
        return sourceId;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getSourceDescription() {
        return sourceDescription;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public static void addNewSourceId(String selectedSourceId){
        selectedSourceIds.add(selectedSourceId);
        isNewsSourcesIdChanged = true;
    }
    public static void removeExistingNewsSourceId(String unselectSourceId){
        selectedSourceIds.remove(unselectSourceId);
        isNewsSourcesIdChanged = true;
    }

    public static void setSelectedSourceIds(List<String> savedSelectedSourceIds) {
        if(savedSelectedSourceIds.size() > 0) {
            selectedSourceIds.addAll(savedSelectedSourceIds);
        }
    }

    public boolean isNewsSourceSelected(){
        // return true if newsSource is already selected
        return selectedSourceIds.contains(sourceId);
    }

}
