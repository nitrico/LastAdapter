package com.github.nitrico.lastadapterproject;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.github.nitrico.lastadapter.LastAdapter;
import com.github.nitrico.lastadapterproject.item.Header;
import com.github.nitrico.lastadapterproject.item.Point;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class JavaListFragment extends ListFragment implements LastAdapter.OnBindListener {

    public JavaListFragment() { }

    private RecyclerView list;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        list = (RecyclerView) view.findViewById(R.id.list);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LastAdapter.with(Data.INSTANCE.getItems(), BR.item)
                .map(Header.class, R.layout.item_header)
                .map(Point.class, R.layout.item_point)
                .onBindListener(this)
                .into(list);
    }

    @Override
    public void onBind(@NotNull Object item, @NotNull View view, int position) {
        System.out.println("Java onBind position " +position +": " +item);
    }

}
