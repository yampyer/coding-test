package com.jeanpigomez.codingtest.ui.language;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.jeanpigomez.codingtest.R;
import com.jeanpigomez.codingtest.base.BaseActivity;
import com.jeanpigomez.codingtest.model.Language;

import java.util.List;

import javax.inject.Inject;

public class LanguageListActivity extends BaseActivity implements LanguageContract.View {

    Toolbar mainToolbar;

    RecyclerView rvLanguages;

    SwipeRefreshLayout refreshLayout;

    ProgressBar progressBar;

    private Snackbar errorSnackbar;

    private LanguageListAdapter adapter;
    @Inject
    LanguagePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_language_list);
        rvLanguages = findViewById(R.id.language_list);
        mainToolbar = findViewById(R.id.mainToolbar);
        refreshLayout = findViewById(R.id.refresh);
        progressBar = findViewById(R.id.progressBar);
        setSupportActionBar(mainToolbar);
        initializePresenter();
        setupWidgets();
        setTitle(getString(R.string.app_name));
    }

    private void initializePresenter() {
        DaggerLanguageComponent.builder()
                .languagePresenterModule(new LanguagePresenterModule(this))
                .languageRepositoryComponent(getLanguageRepositoryComponent())
                .build()
                .inject(this);
    }

    private void setupWidgets() {
        // Setup recycler view
        adapter = new LanguageListAdapter();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvLanguages.setLayoutManager(layoutManager);
        rvLanguages.setAdapter(adapter);
        rvLanguages.setItemAnimator(new DefaultItemAnimator());

        // Refresh layout
        refreshLayout.setOnRefreshListener(() -> presenter.loadLanguages(true));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_language_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.pick_sort_option)
                        .setItems(R.array.sort_options, (dialogInterface, which) -> presenter.sort(which))
                        .show();
                return true;
            case R.id.action_add_language:
                showAddLanguageDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void showLanguages(List<Language> languages) {
        adapter.setLanguageList(languages);
    }

    @Override
    public void showNoDataMessage() {
        showNotification(getString(R.string.no_data_error));
    }

    @Override
    public void showErrorMessage(String error) {
        showNotification(error);
    }

    @Override
    public void clearLanguages() {
        adapter.clearData();
    }

    @Override
    public void stopLoadingIndicator() {
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
    }

    private void showAddLanguageDialog() {
        Dialog dialog = new Dialog(this, R.style.Dialog);
        dialog.setContentView(R.layout.dialog_add_language);
        dialog.setTitle(R.string.action_add_language);

        EditText etName = dialog.findViewById(R.id.etName);
        EditText etScore = dialog.findViewById(R.id.etScore);

        Button btnAddLanguage = dialog.findViewById(R.id.btAdd);
        btnAddLanguage.setOnClickListener(v -> {
            if (!etName.getText().toString().isEmpty() || !etScore.getText().toString().isEmpty()) {
                presenter.addLanguage(etName.getText().toString(),
                        Integer.parseInt(etScore.getText().toString()));
                dialog.dismiss();
            }
        });

        Button btnCancel = dialog.findViewById(R.id.btCancel);
        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void showNotification(String message) {
        errorSnackbar = Snackbar.make(refreshLayout, message, Snackbar.LENGTH_INDEFINITE);
        errorSnackbar.setAction(R.string.close, v -> errorSnackbar.dismiss());
        errorSnackbar.show();
    }
}
