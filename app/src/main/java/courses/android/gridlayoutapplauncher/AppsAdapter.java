package courses.android.gridlayoutapplauncher;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tonya on 4/17/16.
 */
public class AppsAdapter extends BaseAdapter {

    private List<ActivityInfo> allApps;
    private Context context;
    private PackageManager packageManager;

    public AppsAdapter(Context context) {
        this.context = context;
        packageManager = context.getPackageManager();
        allApps = new ArrayList();

        initAllAppsList();
    }


    @Override
    public int getCount() {
        return allApps.size();
    }

    @Override
    public Object getItem(int position) {
        return allApps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.activity_element, null);

            holder.icon = (ImageView) convertView.findViewById(R.id.grid_item_image);
            holder.label = (TextView) convertView.findViewById(R.id.grid_item_label);
            holder.appActivity = (LinearLayout) convertView.findViewById(R.id.activity_element);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();

        }

        final ActivityInfo activity = allApps.get(position);
        holder.icon.setImageDrawable(activity.getIcon());
        holder.label.setText(activity.getLabel());

        // opening app
        holder.appActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = packageManager.getLaunchIntentForPackage(activity.getName());
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    private void initAllAppsList() {
        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> allActivities = packageManager.queryIntentActivities(mainIntent, 0);

        for (ResolveInfo activity : allActivities) {

            String name = activity.activityInfo.packageName;
            String label = activity.loadLabel(packageManager).toString();
            Drawable icon = activity.loadIcon(packageManager);

            allApps.add(new ActivityInfo(name, label, icon));

        }
    }

    private static class ViewHolder {
        public TextView label;
        public ImageView icon;
        public LinearLayout appActivity;
    }

    private static class ActivityInfo {
        private String name;
        private String label;
        private Drawable icon;

        public ActivityInfo(String name, String label, Drawable icon) {
            this.name = name;
            this.label = label;
            this.icon = icon;
        }

        public String getName() {
            return name;
        }

        public String getLabel() {
            return label;
        }

        public Drawable getIcon() {
            return icon;
        }
    }
}
