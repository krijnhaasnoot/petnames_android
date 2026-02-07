package com.kinder.petnames.ui.screens;

import com.kinder.petnames.core.AnalyticsManager;
import com.kinder.petnames.core.PreferencesManager;
import com.kinder.petnames.data.HouseholdRepository;
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
public final class OnboardingViewModel_Factory implements Factory<OnboardingViewModel> {
  private final Provider<HouseholdRepository> householdRepositoryProvider;

  private final Provider<PreferencesManager> preferencesManagerProvider;

  private final Provider<AnalyticsManager> analyticsManagerProvider;

  public OnboardingViewModel_Factory(Provider<HouseholdRepository> householdRepositoryProvider,
      Provider<PreferencesManager> preferencesManagerProvider,
      Provider<AnalyticsManager> analyticsManagerProvider) {
    this.householdRepositoryProvider = householdRepositoryProvider;
    this.preferencesManagerProvider = preferencesManagerProvider;
    this.analyticsManagerProvider = analyticsManagerProvider;
  }

  @Override
  public OnboardingViewModel get() {
    return newInstance(householdRepositoryProvider.get(), preferencesManagerProvider.get(), analyticsManagerProvider.get());
  }

  public static OnboardingViewModel_Factory create(
      Provider<HouseholdRepository> householdRepositoryProvider,
      Provider<PreferencesManager> preferencesManagerProvider,
      Provider<AnalyticsManager> analyticsManagerProvider) {
    return new OnboardingViewModel_Factory(householdRepositoryProvider, preferencesManagerProvider, analyticsManagerProvider);
  }

  public static OnboardingViewModel newInstance(HouseholdRepository householdRepository,
      PreferencesManager preferencesManager, AnalyticsManager analyticsManager) {
    return new OnboardingViewModel(householdRepository, preferencesManager, analyticsManager);
  }
}
