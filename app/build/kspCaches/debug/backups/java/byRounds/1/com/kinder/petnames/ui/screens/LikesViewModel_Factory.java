package com.kinder.petnames.ui.screens;

import com.kinder.petnames.core.AnalyticsManager;
import com.kinder.petnames.core.PreferencesManager;
import com.kinder.petnames.core.SessionManager;
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
public final class LikesViewModel_Factory implements Factory<LikesViewModel> {
  private final Provider<SwipesRepository> swipesRepositoryProvider;

  private final Provider<PreferencesManager> preferencesManagerProvider;

  private final Provider<AnalyticsManager> analyticsManagerProvider;

  private final Provider<SessionManager> sessionManagerProvider;

  public LikesViewModel_Factory(Provider<SwipesRepository> swipesRepositoryProvider,
      Provider<PreferencesManager> preferencesManagerProvider,
      Provider<AnalyticsManager> analyticsManagerProvider,
      Provider<SessionManager> sessionManagerProvider) {
    this.swipesRepositoryProvider = swipesRepositoryProvider;
    this.preferencesManagerProvider = preferencesManagerProvider;
    this.analyticsManagerProvider = analyticsManagerProvider;
    this.sessionManagerProvider = sessionManagerProvider;
  }

  @Override
  public LikesViewModel get() {
    return newInstance(swipesRepositoryProvider.get(), preferencesManagerProvider.get(), analyticsManagerProvider.get(), sessionManagerProvider.get());
  }

  public static LikesViewModel_Factory create(Provider<SwipesRepository> swipesRepositoryProvider,
      Provider<PreferencesManager> preferencesManagerProvider,
      Provider<AnalyticsManager> analyticsManagerProvider,
      Provider<SessionManager> sessionManagerProvider) {
    return new LikesViewModel_Factory(swipesRepositoryProvider, preferencesManagerProvider, analyticsManagerProvider, sessionManagerProvider);
  }

  public static LikesViewModel newInstance(SwipesRepository swipesRepository,
      PreferencesManager preferencesManager, AnalyticsManager analyticsManager,
      SessionManager sessionManager) {
    return new LikesViewModel(swipesRepository, preferencesManager, analyticsManager, sessionManager);
  }
}
