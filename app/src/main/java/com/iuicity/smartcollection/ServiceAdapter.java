package com.iuicity.smartcollection;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Shui on 2017/11/22.
 */

public class ServiceAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    private String mSelect;

    public ServiceAdapter(@Nullable List<String> data) {
        super(R.layout.item_service, data);
        mSelect = data.get(0);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.textview, item);
        if (TextUtils.equals(item, mSelect)) {
            //之前选中的项目
            helper.setTextColor(R.id.textview, Color.parseColor("#cee0ff"));
        } else {
            helper.setTextColor(R.id.textview, Color.parseColor("#4179db"));
        }
    }


    public void setSelect(String select) {
        mSelect = select;
        notifyDataSetChanged();
    }
}
