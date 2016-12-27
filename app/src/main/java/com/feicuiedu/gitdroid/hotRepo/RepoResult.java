package com.feicuiedu.gitdroid.hotRepo;

import com.feicuiedu.gitdroid.hotRepo.Repo;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by gqq on 2016/12/9.
 */

public class RepoResult {


    /**
     * total_count : 2074901
     * incomplete_results : false
     * items : []
     */

    @SerializedName("total_count")
    private int totalCount;
    @SerializedName("incomplete_results")
    private boolean incompleteResults;
    private List<Repo> items;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public boolean isIncompleteResults() {
        return incompleteResults;
    }

    public void setIncompleteResults(boolean incompleteResults) {
        this.incompleteResults = incompleteResults;
    }

    public List<Repo> getItems() {
        return items;
    }

    public void setItems(List<Repo> items) {
        this.items = items;
    }
}
