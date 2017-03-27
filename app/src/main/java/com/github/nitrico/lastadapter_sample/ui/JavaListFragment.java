package com.github.nitrico.lastadapter_sample.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.github.nitrico.lastadapter.BaseType;
import com.github.nitrico.lastadapter.ItemType;
import com.github.nitrico.lastadapter.LastAdapter;
import com.github.nitrico.lastadapter.LayoutHandler;
import com.github.nitrico.lastadapter.TypeHandler;
import com.github.nitrico.lastadapter.ViewHolder;
import com.github.nitrico.lastadapter_sample.BR;
import com.github.nitrico.lastadapter_sample.R;
import com.github.nitrico.lastadapter_sample.data.Car;
import com.github.nitrico.lastadapter_sample.data.Data;
import com.github.nitrico.lastadapter_sample.data.Header;
import com.github.nitrico.lastadapter_sample.data.Person;
import com.github.nitrico.lastadapter_sample.data.Point;
import com.github.nitrico.lastadapter_sample.data.StableData;
import com.github.nitrico.lastadapter_sample.databinding.ItemCarBinding;
import com.github.nitrico.lastadapter_sample.databinding.ItemHeaderBinding;
import com.github.nitrico.lastadapter_sample.databinding.ItemHeaderFirstBinding;
import com.github.nitrico.lastadapter_sample.databinding.ItemPersonBinding;
import com.github.nitrico.lastadapter_sample.databinding.ItemPointBinding;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;

public class JavaListFragment extends ListFragment {

    public static final String TAG = JavaListFragment.class.getSimpleName();

    private final ItemType<ItemHeaderFirstBinding> TYPE_HEADER_FIRST = new ItemType<ItemHeaderFirstBinding>(R.layout.item_header_first) {
        @Override
        public void onBind(@NotNull ViewHolder<ItemHeaderFirstBinding> viewHolder) {
            ItemHeaderFirstBinding binding = viewHolder.getBinding();
            Header item = binding.getItem();
            int position = viewHolder.getAdapterPosition();
            Log.d(TAG, "Bound " +item + " at #" + position);
        }
        @Override
        public void onRecycle(@NotNull ViewHolder<ItemHeaderFirstBinding> viewHolder) {
            Log.d(TAG, "Recycled " +viewHolder.getBinding().getItem() + " at #" + viewHolder.getAdapterPosition());
        }
    };

    private final ItemType<ItemHeaderBinding> TYPE_HEADER = new ItemType<ItemHeaderBinding>(R.layout.item_header) {
        @Override
        public void onBind(@NotNull ViewHolder<ItemHeaderBinding> viewHolder) {
            Log.d(TAG, "Bound " +viewHolder.getBinding().getItem() + " at #" + viewHolder.getAdapterPosition());
        }
        @Override
        public void onRecycle(@NotNull ViewHolder<ItemHeaderBinding> viewHolder) {
            Log.d(TAG, "Recycled " +viewHolder.getBinding().getItem() + " at #" + viewHolder.getAdapterPosition());
        }
    };

    private final ItemType<ItemPointBinding> TYPE_POINT = new ItemType<ItemPointBinding>(R.layout.item_point) {
        @Override
        public void onBind(@NotNull ViewHolder<ItemPointBinding> viewHolder) {
            Log.d(TAG, "Bound " +viewHolder.getBinding().getItem() + " at #" + viewHolder.getAdapterPosition());
        }
        @Override
        public void onRecycle(@NotNull ViewHolder<ItemPointBinding> viewHolder) {
            Log.d(TAG, "Recycled " +viewHolder.getBinding().getItem() + " at #" + viewHolder.getAdapterPosition());
        }
    };

    private final ItemType<ItemCarBinding> TYPE_CAR = new ItemType<ItemCarBinding>(R.layout.item_car) {
        @Override
        public void onBind(@NotNull ViewHolder<ItemCarBinding> viewHolder) {
            Log.d(TAG, "Bound " +viewHolder.getBinding().getItem() + " at #" + viewHolder.getAdapterPosition());
        }
        @Override
        public void onRecycle(@NotNull ViewHolder<ItemCarBinding> viewHolder) {
            Log.d(TAG, "Recycled " +viewHolder.getBinding().getItem() + " at #" + viewHolder.getAdapterPosition());
        }
    };

    private final ItemType<ItemPersonBinding> TYPE_PERSON = new ItemType<ItemPersonBinding>(R.layout.item_person) {
        @Override
        public void onBind(@NotNull ViewHolder<ItemPersonBinding> viewHolder) {
            Log.d(TAG, "Bound " +viewHolder.getBinding().getItem() + " at #" + viewHolder.getAdapterPosition());
        }
        @Override
        public void onRecycle(@NotNull ViewHolder<ItemPersonBinding> viewHolder) {
            Log.d(TAG, "Recycled " +viewHolder.getBinding().getItem() + " at #" + viewHolder.getAdapterPosition());
        }
    };


    public JavaListFragment() { }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        List<Object> items = Data.INSTANCE.getItems();
        boolean stableIds = items == StableData.INSTANCE.getItems();

        //setMapAdapter(items, stableIds);
        //setMapAdapterWithListeners(items, stableIds);
        //setLayoutHandlerAdapter(items, stableIds);
        setTypeHandlerAdapter(items, stableIds);
    }

    private void setMapAdapter(List<Object> items, boolean stableIds) {
        LastAdapter.with(items, BR.item, stableIds)
                .map(Car.class, R.layout.item_car)
                .map(Person.class, R.layout.item_person)
                .map(Header.class, R.layout.item_header)
                .map(Point.class, R.layout.item_point)
                .into(list);
    }

    private void setMapAdapterWithListeners(List<Object> items, boolean stableIds) {
        LastAdapter.with(items, BR.item, stableIds)
                .map(Car.class, TYPE_CAR)
                .map(Person.class, TYPE_PERSON)
                .map(Point.class, new ItemType<ItemPointBinding>(R.layout.item_point) {
                    @Override
                    public void onBind(@NotNull ViewHolder<ItemPointBinding> viewHolder) {
                        System.out.println("Bound " +viewHolder.getBinding().getItem() + " at #" + viewHolder.getAdapterPosition());
                    }
                    @Override
                    public void onRecycle(@NotNull ViewHolder<ItemPointBinding> viewHolder) {
                        System.out.println("Recycled " +viewHolder.getBinding().getItem() + " at #" + viewHolder.getAdapterPosition());
                    }
                })
                .map(Header.class, TYPE_HEADER)
                .into(list);
    }

    private void setLayoutHandlerAdapter(List<Object> items, boolean stableIds) {
        LastAdapter.with(items, BR.item, stableIds).handler(new LayoutHandler() {
            @Override
            public int getItemLayout(@NotNull Object item, int position) {
                if (item instanceof Header) return position % 2 == 0 ? R.layout.item_header_first : R.layout.item_header;
                else if (item instanceof Point) return R.layout.item_point;
                else if (item instanceof Person) return R.layout.item_person;
                else if (item instanceof Car) return R.layout.item_car;
                else return -1;
            }
        }).into(list);
    }

    private void setTypeHandlerAdapter(List<Object> items, boolean stableIds) {
        LastAdapter.with(items, BR.item, stableIds).handler(new TypeHandler() {
            @Override
            public BaseType<?> getItemType(@NotNull Object item, int position) {
                if (item instanceof Header) return position % 2 == 0 ? TYPE_HEADER_FIRST : TYPE_HEADER;
                else if (item instanceof Point) return TYPE_POINT;
                else if (item instanceof Person) return TYPE_PERSON;
                else if (item instanceof Car) return TYPE_CAR;
                return null;
            }
        }).into(list);
    }

    private static void toast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

}
