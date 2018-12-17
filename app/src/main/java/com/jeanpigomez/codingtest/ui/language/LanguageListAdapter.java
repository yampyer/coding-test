package com.jeanpigomez.codingtest.ui.language;

import android.databinding.DataBindingUtil;

import com.jeanpigomez.codingtest.databinding.ItemLanguageBinding;

import android.os.CountDownTimer;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.jeanpigomez.codingtest.R;
import com.jeanpigomez.codingtest.model.Language;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class LanguageListAdapter extends RecyclerView.Adapter<LanguageListAdapter.ViewHolder> {

    List<? extends Language> languageList = new ArrayList<>();

    public LanguageListAdapter() {
    }

    public void setLanguageList(final List<? extends Language> languageList) {
        if (this.languageList == null) {
            this.languageList = languageList;
            notifyItemRangeInserted(0, languageList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return LanguageListAdapter.this.languageList.size();
                }

                @Override
                public int getNewListSize() {
                    return languageList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return LanguageListAdapter.this.languageList.get(oldItemPosition).getName().equals(
                            languageList.get(newItemPosition).getName());
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    Language language = languageList.get(newItemPosition);
                    Language old = languageList.get(oldItemPosition);
                    return language.getName().equals(old.getName())
                            && Objects.equals(language.getScore(), old.getScore());
                }
            });
            this.languageList = languageList;
            result.dispatchUpdatesTo(this);
        }
    }

    @Override
    public LanguageListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemLanguageBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.item_language,
                        parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(LanguageListAdapter.ViewHolder holder, int position) {
        holder.binding.setLanguage(languageList.get(position));
        holder.binding.executePendingBindings();

        if (holder.timerCount != null) {
            holder.timerCount.cancel();
        }

        Date dateConverted = parseDateToFormat(languageList.get(position).getEndDate());
        Date now = new Date();

        Long diff = dateConverted.getTime() - now.getTime();

        Long secondsInMilli = 1000L;
        Long minutesInMilli = secondsInMilli * 60L;
        Long hoursInMilli = minutesInMilli * 60L;
        Long daysInMilli = hoursInMilli * 24L;

        Long elapsedDays = diff / daysInMilli;
        diff %= daysInMilli;

        Long elapsedHours = diff / hoursInMilli;
        diff %= hoursInMilli;

        Long elapsedMinutes = diff / minutesInMilli;
        diff %= minutesInMilli;

        Long elapsedSeconds = diff / secondsInMilli;

        if (diff < 0) {
            holder.binding.languageRemainingTime.setText(R.string.time_passed);
        } else if (elapsedDays < 1) {
            holder.timerCount = new CountDownTimer(dateConverted.getTime() - now.getTime(), 1000) {

                @Override
                public void onTick(long millisUntilFinished) {
                    holder.binding.languageRemainingTime.setText(String.format(Locale.US, "The remaining time is: %d hours, %d minutes, %d seconds",
                            TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                }

                @Override
                public void onFinish() {
                    holder.binding.languageRemainingTime.setText(R.string.time_passed);
                }
            }.start();
        } else {
            holder.binding.languageRemainingTime.setText(String.format(Locale.US, "The remaining time is: %d days, %d hours, %d minutes, %d seconds",
                    elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds));
        }
    }

    private Date parseDateToFormat(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertedDate;
    }

    @Override
    public int getItemCount() {
        return languageList == null ? 0 : languageList.size();
    }

    public void clearData() {
        languageList.clear();
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        final ItemLanguageBinding binding;
        CountDownTimer timerCount;

        public ViewHolder(ItemLanguageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
