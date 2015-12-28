package com.mine.mylauncher;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    private List<ResolveInfo> mApps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadApps();
        GridView gvAppList = (GridView) findViewById(R.id.apps_list);
        gvAppList.setAdapter(new AppsAdapter());
        gvAppList.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ResolveInfo info = mApps.get(position);

        //该应用的包名
        String pkg = info.activityInfo.packageName;
        //应用的主activity类
        String cls = info.activityInfo.name;

        ComponentName componet = new ComponentName(pkg, cls);

        Intent i = new Intent();
        i.setComponent(componet);
        startActivity(i);
    }

    private void loadApps() {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        mApps = getPackageManager().queryIntentActivities(mainIntent, 0);
    }

    public class AppsAdapter extends BaseAdapter {
        private int size = 0;

        public AppsAdapter() {
            size = (getResources().getDisplayMetrics().widthPixels - 10) / 3;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LinearLayout rl;
            if (convertView == null) {
                rl = new LinearLayout(MainActivity.this);
                rl.setLayoutParams(new GridView.LayoutParams(size, size));
                rl.setOrientation(LinearLayout.VERTICAL);
                ImageView i;
                i = new ImageView(MainActivity.this);
                i.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.gravity = Gravity.CENTER_HORIZONTAL;
                i.setLayoutParams(params);
                rl.addView(i, 0);
                TextView labelView = new TextView(MainActivity.this);
                labelView.setLayoutParams(params);
                rl.addView(labelView, 1);
            } else {
                rl = (LinearLayout) convertView;
            }
            ImageView i = (ImageView) rl.getChildAt(0);
            ResolveInfo info = mApps.get(position);
            TextView labelView = (TextView) rl.getChildAt(1);
            String appName = info.loadLabel(getPackageManager()).toString();
            labelView.setText(appName);
            i.setImageDrawable(info.activityInfo.loadIcon(getPackageManager()));

            return rl;
        }

        public final int getCount() {
            return mApps.size();
        }

        public final Object getItem(int position) {
            return mApps.get(position);
        }

        public final long getItemId(int position) {
            return position;
        }
    }
}