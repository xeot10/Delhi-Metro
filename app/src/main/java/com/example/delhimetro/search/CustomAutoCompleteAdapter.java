package com.example.delhimetro.search;

import android.content.Context;
import android.widget.ArrayAdapter;

public class CustomAutoCompleteAdapter extends ArrayAdapter<String> {

    public CustomAutoCompleteAdapter(Context context, int resource, String[] suggestions) {
        super(context, resource, suggestions);
    }

    @Override
    public int getCount() {
        // Limit the count to a maximum of 6 or the number of suggestions if less
        int count = super.getCount();
        return Math.min(count, 6);
    }
}
