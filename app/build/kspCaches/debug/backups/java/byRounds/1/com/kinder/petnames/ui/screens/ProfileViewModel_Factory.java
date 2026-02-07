package com.kinder.petnames.ui.screens;

import com.kinder.petnames.core.AnalyticsManager;
import com.kinder.petnames.core.PreferencesManager;
import com.kinder.petnames.data.HouseholdRepository;
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
public final class ProfileViewModel_Factory implements Factory<ProfileViewModel> {
  private final Provider<HouseholdRepository> householdRepositoryProvider;

  private final Provider<SwipesRepository> swipesRepositoryProvider;

  private final Provider<PreferencesManager> preferencesManagerProvider;

  private final Provider<AnalyticsManager> analyticsManagerProvider;

  public ProfileViewModel_Factory(Provider<HouseholdRepository> householdRepositoryProvider,
      Provider<SwipesRepository> swipesRepositoryProvider,
      Provider<PreferencesManager> preferencesManagerProvider,
      Provider<AnalyticsManager> analyticsManagerProvider) {
    this.householdRepositoryProvider = householdRepositoryProvider;
    this.swipesRepositoryProvider = swipesRepositoryProvider;
    this.preferencesManagerProvider = preferencesManagerProvider;
    this.analyticsManagerProvider = analyticsManagerProvider;
  }

  @Override
  public ProfileViewModel get() {
    return newInstance(householdRepositoryProvider.get(), swipesRepositoryProvider.get(), preferencesManagerProvider.get(), analyticsManagerProvider.get());
  }

  public static ProfileViewModel_Factory create(
      Provider<HouseholdRepository> householdRepositoryProvider,
      Provider<SwipesRepository> swipesRepositoryProvider,
      Provider<PreferencesManager> preferencesManagerProvider,
      Provider<AnalyticsManager> analyticsManagerProvider) {
    return new ProfileViewModel_Factory(householdRepositoryProvider, swipesRepositoryProvider, preferencesManagerProvider, analyticsManagerProvider);
  }

  public static ProfileViewModel newInstance(HouseholdRepository householdRepository,
      SwipesRepository swipesRepository, PreferencesManager preferencesManager,
      AnalyticsManager analyticsManager) {
    return new ProfileViewModel(householdRepository, swipesRepository, preferencesManager, analyticsManager);
  }
}
