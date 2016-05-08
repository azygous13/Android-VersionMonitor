package com.github.azygous13.versionmonitor;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.ContextThemeWrapper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created on 3/21/2016 AD.
 */
public class VersionMonitor {

    private static final String PLAY_STORE_URL = "http://play.google.com/store/apps/details?id=";
    private static VersionMonitor versionMonitor;
    private Context context;
    private String packageName;

    public static VersionMonitor init(Context context) {
        if (versionMonitor == null) {
            versionMonitor = new VersionMonitor(context);
        }
        return versionMonitor;
    }

    private VersionMonitor(Context context) {
        this.context = context;
        packageName = context.getPackageName();
    }

    public void monitor() {
        new Monitor().execute();
    }

    protected boolean isNewVersion(String currentVersionString, String newVersionString) {
        boolean isCurrentVersionHasPatch = isHasPatchVersion(currentVersionString);
        boolean isNewVersionHasPatch = isHasPatchVersion(newVersionString);

        float currentVersion, newVersion;
        float currentPatchVersion = 0;
        float newPatchVersion = 0;

        if (isCurrentVersionHasPatch && isNewVersionHasPatch) {
            currentVersion = getRemovePatchVersion(currentVersionString);
            newVersion = getRemovePatchVersion(newVersionString);
            if (currentVersion == newVersion) {
                currentPatchVersion = getPatchVersion(currentVersionString);
                newPatchVersion = getPatchVersion(newVersionString);
                return currentPatchVersion < newPatchVersion;
            }
        } else if (isCurrentVersionHasPatch){
            currentVersion = getRemovePatchVersion(currentVersionString);
            currentPatchVersion = getPatchVersion(currentVersionString);
            newVersion = getNoPatchVersion(newVersionString);
        } else if (isNewVersionHasPatch) {
            currentVersion = getNoPatchVersion(currentVersionString);
            newVersion = getRemovePatchVersion(newVersionString);
            newPatchVersion = getPatchVersion(newVersionString);
        } else {
            currentVersion = getNoPatchVersion(currentVersionString);
            newVersion = getNoPatchVersion(newVersionString);
        }

        if (currentVersion == newVersion) {
            if (currentPatchVersion < newPatchVersion) {
                return true;
            }
        }
        return currentVersion < newVersion;
    }

    protected boolean isHasPatchVersion(String version) {
        Pattern isHasPatch = Pattern.compile("^\\d+\\.\\d+\\.\\d+$");
        Matcher matcher = isHasPatch.matcher(version);
        return matcher.matches();
    }

    protected float getNoPatchVersion(String version) {
        return Float.parseFloat(version);
    }

    protected float getRemovePatchVersion(String version) {
        if (isHasPatchVersion(version)) {
            String noPatchVersion = version.substring(0, version.lastIndexOf("."));
            return getNoPatchVersion(noPatchVersion);
        }
        return getNoPatchVersion(version);
    }

    protected float getPatchVersion(String version) {
        if (isHasPatchVersion(version)) {
            String patchVersion = version.substring(version.lastIndexOf(".") + 1);
            return Float.parseFloat(patchVersion);
        }
        return 0;
    }

    private String getCurrentVersion(String packageName) {
        String currentVersion = "";
        try {
            currentVersion = context.getPackageManager().getPackageInfo(packageName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return currentVersion;
    }

    private String getNewVersion() throws IOException {
        String newVersion =  Jsoup.connect(PLAY_STORE_URL + packageName)
                .timeout(15000)
                .get()
                .select("div[itemprop=softwareVersion]")
                .first()
                .ownText();
        return newVersion;
    }

    private String getWhatNew() throws IOException {
        ListIterator<Element> elementList = Jsoup.connect(PLAY_STORE_URL + packageName)
                .timeout(15000)
                .get()
                .select(".recent-change")
                .listIterator();
        return combineWhatNewMessage(elementList);
    }

    private String combineWhatNewMessage(ListIterator<Element> elementList) {
        String whatNew = "";
        while (elementList.hasNext()) {
            whatNew += elementList.next().ownText() + "\n";
        }
        return whatNew;
    }

    private void showDialog(String whatNew) {
        new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.VersionMonitorStyle))
                .setTitle(R.string.vm_dialog_title)
                .setMessage(whatNew)
                .setNegativeButton(R.string.vm_later, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(R.string.vm_update_now, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openPlayStore();
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }

    private void openPlayStore() {
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, uri));
        } catch (ActivityNotFoundException e) {
            uri = Uri.parse(PLAY_STORE_URL + context.getPackageName());
            context.startActivity(new Intent(Intent.ACTION_VIEW, uri));
        }
    }

    private class Monitor extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            String whatNew = "";
            try {
                String newVersion = getNewVersion();
                if (isNewVersion(getCurrentVersion(packageName), newVersion)) {
                    whatNew = getWhatNew();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return whatNew;
        }

        @Override
        protected void onPostExecute(String whatNew) {
            super.onPostExecute(whatNew);
            showDialog(whatNew);
        }
    }
}
