package demo.yc.formalmanagersystem.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import demo.yc.formalmanagersystem.R;
import demo.yc.formalmanagersystem.models.Property;

/**
 * Created by Administrator on 2016/7/20.
 */
public class MyAdapterForProperty extends ArrayAdapter<Property> {

    private int resourceId;

    public MyAdapterForProperty(Context context, int resource, List objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        Property property = getItem(position);
        ViewHolder holder = new ViewHolder();
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            holder.textView1 = (TextView) view.findViewById(R.id.name_of_property);
            holder.textView2 = (TextView) view.findViewById(R.id.identifier_of_property);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.textView1.setText("资产名称：" + property.getName());
        holder.textView2.setText("编号：" + property.getIdentifier());
        return view;
    }

    class ViewHolder {
        TextView textView1;
        TextView textView2;
    }
}
