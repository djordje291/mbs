package com.djordjeratkovic.mbs.util;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class CommonUtils {

    public static void loading(View v, boolean b) {
        if (b) {
            v.setVisibility(View.VISIBLE);
        } else {
            v.setVisibility(View.GONE);
        }
    }

    public static void refreshAdapter(RecyclerView.Adapter adapter, Integer position) {
        if (adapter != null) {
            if (position == null) {
                adapter.notifyDataSetChanged();
            } else {
                adapter.notifyItemChanged(position);
            }
        }
    }

    public static void changeIntent(Activity current, Class newActivity) {
        Intent intent = new Intent(current, newActivity);
        current.startActivity(intent);
        current.finish();
    }
}
