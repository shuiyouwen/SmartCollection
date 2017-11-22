package com.iuicity.smartcollection;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.iuicity.smartcollection.utils.Utils;

import java.util.List;

/**
 * Created by Shui on 2017/11/22.
 */

public class ServicePopup {

    private final PopupWindow mPopupWindow;
    private final View mTarget;
    private final Context mContext;
    private final OnItemSelect mOnItemSelect;

    public ServicePopup(View target, Context context, List<String> data, OnItemSelect onItemSelect) {
        mTarget = target;
        mContext = context;
        mOnItemSelect = onItemSelect;

        View inflate = View.inflate(context, R.layout.popup_service, null);
        mPopupWindow = new PopupWindow(inflate, target.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);
        RecyclerView recyclerView = inflate.findViewById(R.id.recycler_view);
        ServiceAdapter adapter = new ServiceAdapter(data);
        recyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        adapter.setOnItemClickListener((adapter1, view, position) -> {
            String select = (String) adapter1.getData().get(position);
            adapter.setSelect(select);
            mOnItemSelect.onItemSelect(select);
            dismiss();
        });
    }

    public void show() {
        if (!mPopupWindow.isShowing()) {
            mPopupWindow.showAsDropDown(mTarget, 0, Utils.dip2px(mContext, 3));
        }
    }

    public void dismiss() {
        if (mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }

    public interface OnItemSelect {
        void onItemSelect(String select);
    }
}
