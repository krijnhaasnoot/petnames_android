package com.kinder.petnames.ui.screens;

import com.kinder.petnames.core.AnalyticsManager;
import com.kinder.petnames.core.PreferencesManager;
import com.kinder.petnames.data.MatchesRepository;
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
public final class MatchesViewModel_Factory implements Factory<MatchesViewModel> {
  private final Provider<MatchesRepository> matchesRepositoryProvider;

  private final Provider<PreferencesManager> preferencesManagerProvider;

  private final Provider<AnalyticsManager> analyticsManagerProvider;

  public MatchesViewModel_Factory(Provider<MatchesRepository> matchesRepositoryProvider,
      Provider<PreferencesManager> preferencesManagerProvider,
      Provider<AnalyticsManager> analyticsManagerProvider) {
    this.matchesRepositoryProvider = matchesRepositoryProvider;
    this.preferencesManagerProvider = preferencesManagerProvider;
    this.analyticsManagerProvider = analyticsManagerProvider;
  }

  @Override
  public MatchesViewModel get() {
    return newInstance(matchesRepositoryProvider.get(), preferencesManagerProvider.get(), analyticsManagerProvider.get());
  }

  public static MatchesViewModel_Factory create(
      Provider<MatchesRepository> matchesRepositoryProvider,
      Provider<PreferencesManager> preferencesManagerProvider,
      Provider<AnalyticsManager> analyticsManagerProvider) {
    return new MatchesViewModel_Factory(matchesRepositoryProvider, preferencesManagerProvider, analyticsManagerProvider);
  }

  public static MatchesViewModel newInstance(MatchesRepository matchesRepository,
      PreferencesManager preferencesManager, AnalyticsManager analyticsManager) {
    return new MatchesViewModel(matchesRepository, preferencesManager, analyticsManager);
  }
}
