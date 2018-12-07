package com.jeanpigomez.codingtest.utils

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.databinding.BindingAdapter
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.jeanpigomez.codingtest.R
import com.jeanpigomez.codingtest.utils.extension.getParentActivity
import com.squareup.picasso.Picasso

@BindingAdapter("adapter")
fun setAdapter(view: RecyclerView, adapter: RecyclerView.Adapter<*>) {
    view.adapter = adapter
}

@BindingAdapter("mutableVisibility")
fun setMutableVisibility(view: View, visibility: MutableLiveData<Int>?) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if (parentActivity != null && visibility != null) {
        visibility.observe(parentActivity, Observer { value -> view.visibility = value ?: View.VISIBLE })
    }
}

@BindingAdapter("mutableText")
fun setMutableText(view: TextView, text: MutableLiveData<String>?) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if (parentActivity != null && text != null) {
        text.observe(parentActivity, Observer { value -> view.text = value ?: "" })
    }
}

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, imageUrl: String) {
    when (imageUrl) {
        "C" -> view.setImageResource(R.drawable.ic_c)
        "Objective-C" -> view.setImageResource(R.drawable.ic_objectivec)
        "Python" -> view.setImageResource(R.drawable.ic_python)
        "Ruby" -> view.setImageResource(R.drawable.ic_ruby)
        "Swift" -> view.setImageResource(R.drawable.ic_swift)
        else -> view.setImageResource(R.drawable.abc_ic_search_api_material)

    }
}