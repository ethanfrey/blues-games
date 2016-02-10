package com.orderbird.ethanblue;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.TooManyListenersException;

/**
 * Created by ethan on 10/02/16.
 */

public class ListAdapter extends BaseAdapter {
    private Activity mContext;
    private List<String> mList;
    private LayoutInflater mLayoutInflater = null;
    public ListAdapter(Activity context, List<String> list) {
        mContext = context;
        mList = list;
        mLayoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int pos) {
        return mList.get(pos);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        CompleteListViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.list_layout, null);
            viewHolder = new CompleteListViewHolder(v);
            v.setTag(viewHolder);
        } else {
            viewHolder = (CompleteListViewHolder) v.getTag();
        }
        viewHolder.mListItem.setText(mList.get(position));
        viewHolder.mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("name", mList.get(position));
                Intent details = new Intent(v.getContext(), DetailActivity.class);
                details.putExtras(bundle);
                v.getContext().startActivity(details);
                Intent phone = new Intent(Intent.ACTION_CALL);
                phone.setData(Uri.parse("tel:1234-5678"));
//                v.getContext().startActivity(phone);
                Intent web = new Intent(Intent.ACTION_VIEW);
                web.setData(Uri.parse("http://www.pimcore.com/"));
//                v.getContext().startActivity(web);
            }
        });
        return v;
    }
}

class CompleteListViewHolder {
    public TextView mListItem;
    public ImageButton mImageButton;
    public CompleteListViewHolder(View base) {
        mListItem = (TextView) base.findViewById(R.id.list_item);
        mImageButton = (ImageButton) base.findViewById(R.id.list_image);
    }
}
