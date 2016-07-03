package com.github.nitrico.lastadapterproject;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;
import com.github.nitrico.lastadapter.LastAdapter;
import com.github.nitrico.lastadapterproject.item.Header;
import com.github.nitrico.lastadapterproject.item.Point;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class JavaListFragment extends ListFragment implements LastAdapter.LayoutHandler,
                                                              LastAdapter.OnBindListener,
                                                              LastAdapter.OnClickListener,
                                                              LastAdapter.OnLongClickListener {

    public JavaListFragment() { }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView list = (RecyclerView) view.findViewById(R.id.list);

        LastAdapter.with(Data.INSTANCE.getItems(), BR.item)
                .map(Header.class, R.layout.item_header)
                .map(Point.class, R.layout.item_point)
                //.layoutHandler(this)
                .onBindListener(this)
                .onClickListener(this)
                .onLongClickListener(this)
                .into(list);
    }

    @Override
    public int getItemLayout(@NotNull Object item, int position) {
        if (item instanceof Header) {
            if (position == 0) return R.layout.item_header;
            else return R.layout.item_header_first;
        }
        else return R.layout.item_point;
    }

    @Override
    public void onBind(@NotNull Object item, @NotNull View view, int position) {
        System.out.println("onBind position " +position +": " +item);
    }

    @Override
    public void onClick(@NotNull Object item, @NotNull View view, int position) {
        Toast.makeText(getActivity(), "onClick position " +position +": " +item, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLongClick(@NotNull Object item, @NotNull View view, int position) {
        Toast.makeText(getActivity(), "onLongClick position " +position +": " +item, Toast.LENGTH_SHORT).show();
    }

}
