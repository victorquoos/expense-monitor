package com.ifsc.expensemonitor.ui.monthlist;

import androidx.recyclerview.widget.GridLayoutManager;

public class CustomSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {
    private final int spanCount;

    public CustomSpanSizeLookup(int spanCount) {
        this.spanCount = spanCount;
    }

    @Override
    public int getSpanSize(int position) {
        if ((position) % 13 == 0){
            return spanCount;
        } else {
            return 1;
        }
    }
}

