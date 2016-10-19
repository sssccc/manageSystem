package demo.yc.formalmanagersystem.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import demo.yc.formalmanagersystem.R;
import demo.yc.formalmanagersystem.models.Purchase;

/**
 * Created by Administrator on 2016/7/20.
 */
public class MyAdapterForPurchase extends ArrayAdapter<Purchase> {

    private int resourceId;

    public MyAdapterForPurchase(Context context, int resource, List objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        Purchase purchase = getItem(position);
        ViewHolder holder = new ViewHolder();
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            holder.textView1 = (TextView) view.findViewById(R.id.name_of_property);
            holder.textView2 = (TextView) view.findViewById(R.id.identifier_of_property);
            holder.checkStatus = (TextView) view.findViewById(R.id.check_status_text);
            holder.checkStatus.setVisibility(View.VISIBLE);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        if(purchase.getCheckState().equals("通过")){
            holder.checkStatus.setText("通过");
            holder.checkStatus.setBackgroundColor(Color.rgb(116,200,246));
        }else if(purchase.getCheckState().equals("拒绝")){
            holder.checkStatus.setText("拒绝");
            holder.checkStatus.setBackgroundColor(Color.rgb(215,83,93));
        }else if(purchase.getCheckState().equals("待审核")){
            holder.checkStatus.setText("待审核");
            holder.checkStatus.setBackgroundColor(Color.rgb(255,213,106));
        }
        holder.textView1.setText("资产名称：" + purchase.getName());
        holder.textView2.setText("申请时间：" + purchase.getApplyTime());
        return view;
    }

    class ViewHolder {
        TextView textView1;
        TextView textView2;
        TextView checkStatus;
    }
}
