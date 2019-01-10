package com.jeanpigomez.codingtest.ui.language;

import com.jeanpigomez.codingtest.base.BasePresenter;
import com.jeanpigomez.codingtest.model.Language;

import java.util.List;

public interface LanguageContract {
    interface View {
        void showLanguages(List<Language> languages);

        void clearLanguages();

        void showNoDataMessage();

        void showErrorMessage(String error);

        void stopLoadingIndicator();
    }

    interface Presenter extends BasePresenter<View> {
        void loadLanguages(boolean onlineRequired);

        void sort(int option);

        void addLanguage(String name, int score);
    }
}
