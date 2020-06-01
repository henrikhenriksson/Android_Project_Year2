package se.miun.hehe0601.dt031g.bathingsites;


import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

public class BathingSiteDownloader {
    Context mContext;
    List<BathingSite> bathingSites;
    int nrOfBathingSites;


    public BathingSiteDownloader(Context mContext) {
        this.mContext = mContext;
        getAllBathingSites();

    }

    private void getAllBathingSites() {

        bathingSites = AppDataBase.getDataBase(mContext).bathingSiteDao().getAll();


    }

    public BathingSite returnLastBathingSite() {
        BathingSite bathingSite = bathingSites.get(bathingSites.size() - 1);

        return bathingSite;
    }


}
