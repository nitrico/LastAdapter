package com.github.nitrico.lastadapterproject.ui;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.github.nitrico.lastadapter.LastAdapter;
import com.github.nitrico.lastadapterproject.BR;
import com.github.nitrico.lastadapterproject.R;
import com.github.nitrico.lastadapterproject.data.Data;
import com.github.nitrico.lastadapterproject.data.Header;
import com.github.nitrico.lastadapterproject.data.Point;
import com.github.nitrico.lastadapterproject.databinding.ItemHeaderBinding;
import com.github.nitrico.lastadapterproject.databinding.ItemHeaderFirstBinding;
import com.github.nitrico.lastadapterproject.databinding.ItemPointBinding;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class JavaListFragment extends ListFragment implements LastAdapter.LayoutHandler,
        LastAdapter.OnBindListener,
        LastAdapter.OnClickListener,
        LastAdapter.OnLongClickListener {

    public JavaListFragment() { }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LastAdapter.with(Data.INSTANCE.getItems(), BR.item)
                //.map(Header.class, R.layout.item_header)
                //.map(Point.class, R.layout.item_point)
                .layoutHandler(this)
                .onBindListener(this)
                .onClickListener(this)
                .onLongClickListener(this)
                .into(list);
    }

    @Override
    public int getItemLayout(@NotNull Object item, int position) {
        if (item instanceof Header) {
            if (position == 0) return R.layout.item_header_first;
            else return R.layout.item_header;
        }
        else return R.layout.item_point;
    }

    @Override
    public void onBind(@NotNull Object item, @NotNull View view, int type, int position) {
        switch (type) {
            case R.layout.item_header_first:
                ItemHeaderFirstBinding headerFirstBinding = DataBindingUtil.getBinding(view);
                headerFirstBinding.headerFirstText.setTag("firstHeader");
                break;
            case R.layout.item_header:
                ItemHeaderBinding headerBinding = DataBindingUtil.getBinding(view);
                Header header = (Header) item;
                headerBinding.headerText.setTag("header" + header.getText());
                break;
            case R.layout.item_point:
                ItemPointBinding pointBinding = DataBindingUtil.getBinding(view);
                Point point = (Point) item;
                pointBinding.pointX.setTag("X:" + point.getX());
                pointBinding.pointY.setTag("Y:" + point.getY());
                break;
        }
    }

    @Override
    public void onClick(@NotNull Object item, @NotNull View view, int type, int position) {
        Toast.makeText(getActivity(), "onClick position " +position +": " +item, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLongClick(@NotNull Object item, @NotNull View view, int type, int position) {
        Toast.makeText(getActivity(), "onLongClick position " +position +": " +item, Toast.LENGTH_SHORT).show();
    }

    private void toast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

}
