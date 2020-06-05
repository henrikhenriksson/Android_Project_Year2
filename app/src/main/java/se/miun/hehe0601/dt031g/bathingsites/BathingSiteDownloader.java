package se.miun.hehe0601.dt031g.bathingsites;


import android.content.Context;
import android.os.AsyncTask;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class BathingSiteDownloader {
    Context mContext;
    List<BathingSite> bathingSites;
    int nrOfBathingSites;


    public BathingSiteDownloader(Context mContext) {
        this.mContext = mContext;
    }

    private void getAllBathingSites() {
        bathingSites = AppDataBase.getDataBase(mContext).bathingSiteDao().getAll();
    }

    public BathingSite returnLastBathingSite() {
        getAllBathingSites();
        if (bathingSites.isEmpty()) {
            return null;
        }
        return bathingSites.get(bathingSites.size() - 1);
    }

    public BathingSite returnRandomBathingSite() {
        getAllBathingSites();
        if (bathingSites.isEmpty()) {
            return null;
        }
        int count = bathingSites.size();

        int random = ThreadLocalRandom.current().nextInt(0, count);

        return bathingSites.get(random);
    }

}
