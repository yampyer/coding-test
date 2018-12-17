package com.jeanpigomez.codingtest.network.repository;

import com.jeanpigomez.codingtest.model.Language;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.subscribers.TestSubscriber;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

public class LanguageRepositoryTest {
    private static final Language LANGUAGE1 = new Language("Php", 16, "2018-12-5T05:30:00.000+0000");
    private static final Language LANGUAGE2 = new Language("Node", 20, "2018-12-18T05:30:00.000+0000");
    private static final Language LANGUAGE3 = new Language("Java", 12, "2019-02-27T20:30:00.000+0000");
    private static final List<Language> languages = Arrays.asList(LANGUAGE1, LANGUAGE2, LANGUAGE3);

    @Mock
    @Local private LanguageDataSource localDataSource;

    @Mock
    @Remote private LanguageDataSource remoteDataSource;

    private LanguageRepository repository;

    private TestSubscriber<List<Language>> languagesTestSubscriber;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        repository = new LanguageRepository(localDataSource, remoteDataSource);

        languagesTestSubscriber = new TestSubscriber<>();
    }

    @Test
    public void loadLanguages_ShouldReturnCache_IfItIsAvailable() {
        // Given
        repository.caches.addAll(languages);

        // When
        repository.loadLanguages(false).subscribe(languagesTestSubscriber);

        // Then
        // No interaction with local storage or remote source
        verifyZeroInteractions(localDataSource);
        verifyZeroInteractions(remoteDataSource);

        languagesTestSubscriber.assertValue(languages);
    }

    @Test public void loadLanguages_ShouldReturnFromLocal_IfCacheIsNotAvailable() {
        // Given
        // No cache
        doReturn(Flowable.just(languages)).when(localDataSource).loadLanguages(false);
        doReturn(Flowable.just(languages)).when(remoteDataSource).loadLanguages(true);

        // When
        repository.loadLanguages(false).subscribe(languagesTestSubscriber);

        // Then
        // Loads from local storage
        verify(localDataSource).loadLanguages(false);
        // Will load from remote source if there is no local data available
        verify(remoteDataSource).loadLanguages(true);

        languagesTestSubscriber.assertValue(languages);
    }

    @Test public void loadLanguages_ShouldReturnFromRemote_WhenItIsRequired() {
        // Given
        doReturn(Flowable.just(languages)).when(remoteDataSource).loadLanguages(true);

        // When
        repository.loadLanguages(true).subscribe(languagesTestSubscriber);

        // Then
        // Load from remote not from local storage
        verify(remoteDataSource).loadLanguages(true);
        verify(localDataSource, never()).loadLanguages(true);
        // Cache and local storage data are clear and are filled with new data
        verify(localDataSource).clearData();
        assertEquals(repository.caches, languages);
        verify(localDataSource).addLanguage(LANGUAGE1);
        verify(localDataSource).addLanguage(LANGUAGE2);
        verify(localDataSource).addLanguage(LANGUAGE3);

        languagesTestSubscriber.assertValue(languages);
    }

    @Test public void refreshData_ShouldClearOldDataFromLocal() {
        // Given
        given(remoteDataSource.loadLanguages(true)).willReturn(Flowable.just(languages));

        // When
        repository.refreshData().subscribe(languagesTestSubscriber);

        // Then
        then(localDataSource).should().clearData();
    }

    @Test public void refreshData_ShouldAddNewDataToCache() {
        // Given
        given(remoteDataSource.loadLanguages(true)).willReturn(Flowable.just(languages));

        // When
        repository.refreshData().subscribe(languagesTestSubscriber);

        // Then
        assertThat(repository.caches, equalTo(languages));
    }

    @Test public void refreshData_ShouldAddNewDataToLocal() {
        // Given
        given(remoteDataSource.loadLanguages(true)).willReturn(Flowable.just(languages));

        // When
        repository.refreshData().subscribe(languagesTestSubscriber);

        // Then
        then(localDataSource).should().addLanguage(LANGUAGE1);
        then(localDataSource).should().addLanguage(LANGUAGE2);
        then(localDataSource).should().addLanguage(LANGUAGE3);
    }

    @Test public void clearData_ShouldClearCachesAndLocalData() {
        // Given
        repository.caches.addAll(languages);

        // When
        repository.clearData();

        // Then
        assertThat(repository.caches, empty());
        then(localDataSource).should().clearData();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void addLanguage_ShouldThrowException() {
        repository.addLanguage(LANGUAGE1);
    }

}