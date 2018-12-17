package com.jeanpigomez.codingtest.utils;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.jeanpigomez.codingtest.R;

public class CustomBindingAdapters {

    @BindingAdapter("imageUrl")
    public static void loadImage(ImageView view, String imageUrl) {
        switch (imageUrl) {
            case "C":
                view.setImageResource(R.drawable.ic_c);
                break;
            case "Objective-C":
                view.setImageResource(R.drawable.ic_objectivec);
                break;
            case "Python":
                view.setImageResource(R.drawable.ic_python);
                break;
            case "Ruby":
                view.setImageResource(R.drawable.ic_ruby);
                break;
            case "Swift":
                view.setImageResource(R.drawable.ic_swift);
                break;
            default:
                view.setImageResource(R.drawable.abc_ic_search_api_material);
                break;
        }
    }
}
