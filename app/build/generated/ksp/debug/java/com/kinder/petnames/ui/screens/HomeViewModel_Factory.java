package com.kinder.petnames.ui.screens;

import com.kinder.petnames.core.AnalyticsManager;
import com.kinder.petnames.core.PreferencesManager;
import com.kinder.petnames.core.SessionManager;
import com.kinder.petnames.data.NamesRepository;
import com.kinder.petnames.data.SwipesRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class HomeViewModel_Factory implements Factory<HomeViewModel> {
  private final Provider<NamesRepository> namesRepositoryProvider;

  private final Provider<SwipesRepository> swipesRepositoryProvider;

  private final Provider<PreferencesManager> preferencesManagerProvider;

  private final Provider<AnalyticsManager> analyticsManagerProvider;

  private final Provider<SessionManager> sessionManagerProvider;

  public HomeViewModel_Factory(Provider<NamesRepository> namesRepositoryProvider,
      Provider<SwipesRepository> swipesRepositoryProvider,
      Provider<PreferencesManager> preferencesManagerProvider,
      Provider<AnalyticsManager> analyticsManagerProvider,
      Provider<SessionManager> sessionManagerProvider) {
    this.namesRepositoryProvider = namesRepositoryProvider;
    this.swipesRepositoryProvider = swipesRepositoryProvider;
    this.preferencesManagerProvider = preferencesManagerProvider;
    this.analyticsManagerProvider = analyticsManagerProvider;
    this.sessionManagerProvider = sessionManagerProvider;
  }

  @Override
  public HomeViewModel get() {
    return newInstance(namesRepositoryProvider.get(), swipesRepositoryProvider.get(), preferencesManagerProvider.get(), analyticsManagerProvider.get(), sessionManagerProvider.get());
  }

  public static HomeViewModel_Factory create(Provider<NamesRepository> namesRepositoryProvider,
      Provider<SwipesRepository> swipesRepositoryProvider,
      Provider<PreferencesManager> preferencesManagerProvider,
      Provider<AnalyticsManager> analyticsManagerProvider,
      Provider<SessionManager> sessionManagerProvider) {
    return new HomeViewModel_Factory(namesRepositoryProvider, swipesRepositoryProvider, preferencesManagerProvider, analyticsManagerProvider, sessionManagerProvider);
  }

  public static HomeViewModel newInstance(NamesRepository namesRepository,
      SwipesRepository swipesRepository, PreferencesManager preferencesManager,
      AnalyticsManager analyticsManager, SessionManager sessionManager) {
    return new HomeViewModel(namesRepository, swipesRepository, preferencesManager, analyticsManager, sessionManager);
  }
}
