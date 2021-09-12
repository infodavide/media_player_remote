package org.infodavid.util.android;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class ViewUtils {
    /**
     * The singleton.
     */
    private static WeakReference<ViewUtils> instance = null;

    /**
     * Instantiates a new util.
     */
    private ViewUtils() {
        super();
    }

    /**
     * returns the singleton.
     *
     * @return the singleton
     */
    public static synchronized ViewUtils getInstance() {
        if (instance == null || instance.get() == null) {
            instance = new WeakReference<>(new ViewUtils());
        }

        return instance.get();
    }

    public <T> List<T> get(final ViewGroup layout, Class<T> type) {
        List<T> results = new ArrayList<>();

        for (int i = 0; i < layout.getChildCount(); i++) {
            View v = layout.getChildAt(i);

            if (type.isInstance(v)) {
                results.add((T) v);
            } else if (v instanceof ViewGroup) {
                results.addAll(get((ViewGroup) v, type));
            }
        }

        return results;
    }

    public ViewGroup getViewGroup(final AppCompatActivity activity, final  int id) {
        return (ViewGroup) ((ViewGroup) (activity.findViewById(id))).getChildAt(0);
    }
}
