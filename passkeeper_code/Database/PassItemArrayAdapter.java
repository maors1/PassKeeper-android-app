package appdev.sapir.maor.passkeeper.Database;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Filter;
import java.util.logging.LogRecord;

import appdev.sapir.maor.passkeeper.PassKeeperMainActivity;
import appdev.sapir.maor.passkeeper.R;



public class PassItemArrayAdapter extends ArrayAdapter<PassItem> implements Filterable {

    private int layoutResource;

    public List<PassItem> originalData = null;
//    public List<PassItem> filteredData = null;
//    public LayoutInflater mInflater;

    public PassItemArrayAdapter(Context context, int layoutResource, List<PassItem> objects) {
        super(context, layoutResource);
        this.addAll(objects);
        this.layoutResource = layoutResource;
        this.originalData = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            LayoutInflater vi = LayoutInflater.from(getContext());
            view = vi.inflate(layoutResource, null);
        }

        PassItem passItem = getItem(position);

        if (passItem != null) {
//            TextView passID = (TextView) view.findViewById(R.id.tvID);
            TextView passName = (TextView) view.findViewById(R.id.tvPassName);
            TextView passUserName = (TextView) view.findViewById(R.id.tvUserName);
//            TextView passValue = (TextView) view.findViewById(R.id.tvPassValue);
            TextView passURL = (TextView) view.findViewById(R.id.tvURL);

//            if (passID != null) {
//                passID.setText((passItem.getId()+""));
//            }

            if (passName != null) {
                passName.setText(passItem.getPassName());
            }
            if (passUserName != null) {
                passUserName.setText(passItem.getUserName());
            }
//            if (passValue != null) {
//                passValue.setText(passItem.getPassValue());
//            }
            if (passURL != null) {
                passURL.setText(passItem.getPassUrl());
            }
        }
        return view;
    }

    public void filterSearch(String value) {
        this.clear();
        for (int i = 0; i < originalData.size(); i++) {
            PassItem check = this.originalData.get(i);
            if (check.getPassName().contains(value) || check.getUserName().contains(value)) {
                this.add(check);

            }
        }
        notifyDataSetChanged();
    }
    public void restoreOriginList(){
        this.clear();
        this.addAll(originalData);
        this.notifyDataSetChanged();
    }
}

