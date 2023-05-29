package com.bestoffers.enjoeepharmacy.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bestoffers.enjoeepharmacy.Models.CategoriesListModel;
import com.bestoffers.enjoeepharmacy.Models.DataCategories;
import com.bestoffers.enjoeepharmacy.R;

import java.util.ArrayList;

public class SpinnerArrayAdapter extends ArrayAdapter<DataCategories> {

    public SpinnerArrayAdapter(Context context,
                               ArrayList<DataCategories> datacategoriesListModelArrayList)
    {
        super(context, 0, datacategoriesListModelArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable
            View convertView, @NonNull ViewGroup parent)
    {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable
            View convertView, @NonNull ViewGroup parent)
    {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView,
                          ViewGroup parent)
    {
        // It is used to set our custom view.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.category_spinner_layout, parent, false);
        }

        TextView textViewName = convertView.findViewById(R.id.textView);
        DataCategories currentItem = getItem(position);

        if (currentItem != null) {
            textViewName.setText(currentItem.getName());
        }
        return convertView;
    }
}
