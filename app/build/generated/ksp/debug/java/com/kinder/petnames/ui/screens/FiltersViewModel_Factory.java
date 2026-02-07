package com.kinder.petnames.ui.screens;

import com.kinder.petnames.core.AnalyticsManager;
import com.kinder.petnames.core.PreferencesManager;
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
public final class FiltersViewModel_Factory implements Factory<FiltersViewModel> {
  private final Provider<PreferencesManager> preferencesManagerProvider;

  private final Provider<AnalyticsManager> analyticsManagerProvider;

  public FiltersViewModel_Factory(Provider<PreferencesManager> preferencesManagerProvider,
      Provider<AnalyticsManager> analyticsManagerProvider) {
    this.preferencesManagerProvider = preferencesManagerProvider;
    this.analyticsManagerProvider = analyticsManagerProvider;
  }

  @Override
  public FiltersViewModel get() {
    return newInstance(preferencesManagerProvider.get(), analyticsManagerProvider.get());
  }

  public static FiltersViewModel_Factory create(
      Provider<PreferencesManager> preferencesManagerProvider,
      Provider<AnalyticsManager> analyticsManagerProvider) {
    return new FiltersViewModel_Factory(preferencesManagerProvider, analyticsManagerProvider);
  }

  public static FiltersViewModel newInstance(PreferencesManager preferencesManager,
      AnalyticsManager analyticsManager) {
    return new FiltersViewModel(preferencesManager, analyticsManager);
  }
}
