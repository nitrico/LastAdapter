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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView list = (RecyclerView) view.findViewById(R.id.list);

        LastAdapter.with(Data.INSTANCE.getItems(), BR.item)
                /*.layoutHandler(new LastAdapter.LayoutHandler() {
                    @Override
                    public int getItemLayout(@NotNull Object item, int index) {
                        if (item instanceof Header) return R.layout.item_header;
                        else return R.layout.item_point;
                    }
                })*/
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
