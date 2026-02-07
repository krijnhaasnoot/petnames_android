package com.kinder.petnames;

import android.app.Activity;
import android.app.Service;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.kinder.petnames.core.AnalyticsManager;
import com.kinder.petnames.core.PreferencesManager;
import com.kinder.petnames.core.SessionManager;
import com.kinder.petnames.core.SupabaseModule_ProvideSupabaseClientFactory;
import com.kinder.petnames.data.HouseholdRepository;
import com.kinder.petnames.data.LocalNamesProvider;
import com.kinder.petnames.data.MatchesRepository;
import com.kinder.petnames.data.NamesRepository;
import com.kinder.petnames.data.SwipesRepository;
import com.kinder.petnames.ui.screens.FiltersViewModel;
import com.kinder.petnames.ui.screens.FiltersViewModel_HiltModules;
import com.kinder.petnames.ui.screens.HomeViewModel;
import com.kinder.petnames.ui.screens.HomeViewModel_HiltModules;
import com.kinder.petnames.ui.screens.LikesViewModel;
import com.kinder.petnames.ui.screens.LikesViewModel_HiltModules;
import com.kinder.petnames.ui.screens.MatchesViewModel;
import com.kinder.petnames.ui.screens.MatchesViewModel_HiltModules;
import com.kinder.petnames.ui.screens.OnboardingViewModel;
import com.kinder.petnames.ui.screens.OnboardingViewModel_HiltModules;
import com.kinder.petnames.ui.screens.ProfileViewModel;
import com.kinder.petnames.ui.screens.ProfileViewModel_HiltModules;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.ViewModelLifecycle;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory;
import dagger.hilt.android.internal.managers.ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory;
import dagger.hilt.android.internal.managers.SavedStateHandleHolder;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideContextFactory;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.IdentifierNameString;
import dagger.internal.KeepFieldType;
import dagger.internal.LazyClassKeyMap;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import io.github.jan.supabase.SupabaseClient;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

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
public final class DaggerPetnamesApplication_HiltComponents_SingletonC {
  private DaggerPetnamesApplication_HiltComponents_SingletonC() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private ApplicationContextModule applicationContextModule;

    private Builder() {
    }

    public Builder applicationContextModule(ApplicationContextModule applicationContextModule) {
      this.applicationContextModule = Preconditions.checkNotNull(applicationContextModule);
      return this;
    }

    public PetnamesApplication_HiltComponents.SingletonC build() {
      Preconditions.checkBuilderRequirement(applicationContextModule, ApplicationContextModule.class);
      return new SingletonCImpl(applicationContextModule);
    }
  }

  private static final class ActivityRetainedCBuilder implements PetnamesApplication_HiltComponents.ActivityRetainedC.Builder {
    private final SingletonCImpl singletonCImpl;

    private SavedStateHandleHolder savedStateHandleHolder;

    private ActivityRetainedCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ActivityRetainedCBuilder savedStateHandleHolder(
        SavedStateHandleHolder savedStateHandleHolder) {
      this.savedStateHandleHolder = Preconditions.checkNotNull(savedStateHandleHolder);
      return this;
    }

    @Override
    public PetnamesApplication_HiltComponents.ActivityRetainedC build() {
      Preconditions.checkBuilderRequirement(savedStateHandleHolder, SavedStateHandleHolder.class);
      return new ActivityRetainedCImpl(singletonCImpl, savedStateHandleHolder);
    }
  }

  private static final class ActivityCBuilder implements PetnamesApplication_HiltComponents.ActivityC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private Activity activity;

    private ActivityCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ActivityCBuilder activity(Activity activity) {
      this.activity = Preconditions.checkNotNull(activity);
      return this;
    }

    @Override
    public PetnamesApplication_HiltComponents.ActivityC build() {
      Preconditions.checkBuilderRequirement(activity, Activity.class);
      return new ActivityCImpl(singletonCImpl, activityRetainedCImpl, activity);
    }
  }

  private static final class FragmentCBuilder implements PetnamesApplication_HiltComponents.FragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private Fragment fragment;

    private FragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public FragmentCBuilder fragment(Fragment fragment) {
      this.fragment = Preconditions.checkNotNull(fragment);
      return this;
    }

    @Override
    public PetnamesApplication_HiltComponents.FragmentC build() {
      Preconditions.checkBuilderRequirement(fragment, Fragment.class);
      return new FragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragment);
    }
  }

  private static final class ViewWithFragmentCBuilder implements PetnamesApplication_HiltComponents.ViewWithFragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private View view;

    private ViewWithFragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;
    }

    @Override
    public ViewWithFragmentCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public PetnamesApplication_HiltComponents.ViewWithFragmentC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewWithFragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl, view);
    }
  }

  private static final class ViewCBuilder implements PetnamesApplication_HiltComponents.ViewC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private View view;

    private ViewCBuilder(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public ViewCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public PetnamesApplication_HiltComponents.ViewC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, view);
    }
  }

  private static final class ViewModelCBuilder implements PetnamesApplication_HiltComponents.ViewModelC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private SavedStateHandle savedStateHandle;

    private ViewModelLifecycle viewModelLifecycle;

    private ViewModelCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ViewModelCBuilder savedStateHandle(SavedStateHandle handle) {
      this.savedStateHandle = Preconditions.checkNotNull(handle);
      return this;
    }

    @Override
    public ViewModelCBuilder viewModelLifecycle(ViewModelLifecycle viewModelLifecycle) {
      this.viewModelLifecycle = Preconditions.checkNotNull(viewModelLifecycle);
      return this;
    }

    @Override
    public PetnamesApplication_HiltComponents.ViewModelC build() {
      Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
      Preconditions.checkBuilderRequirement(viewModelLifecycle, ViewModelLifecycle.class);
      return new ViewModelCImpl(singletonCImpl, activityRetainedCImpl, savedStateHandle, viewModelLifecycle);
    }
  }

  private static final class ServiceCBuilder implements PetnamesApplication_HiltComponents.ServiceC.Builder {
    private final SingletonCImpl singletonCImpl;

    private Service service;

    private ServiceCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ServiceCBuilder service(Service service) {
      this.service = Preconditions.checkNotNull(service);
      return this;
    }

    @Override
    public PetnamesApplication_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonCImpl, service);
    }
  }

  private static final class ViewWithFragmentCImpl extends PetnamesApplication_HiltComponents.ViewWithFragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private final ViewWithFragmentCImpl viewWithFragmentCImpl = this;

    private ViewWithFragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;


    }
  }

  private static final class FragmentCImpl extends PetnamesApplication_HiltComponents.FragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl = this;

    private FragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        Fragment fragmentParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return activityCImpl.getHiltInternalFactoryFactory();
    }

    @Override
    public ViewWithFragmentComponentBuilder viewWithFragmentComponentBuilder() {
      return new ViewWithFragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl);
    }
  }

  private static final class ViewCImpl extends PetnamesApplication_HiltComponents.ViewC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final ViewCImpl viewCImpl = this;

    private ViewCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }
  }

  private static final class ActivityCImpl extends PetnamesApplication_HiltComponents.ActivityC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl = this;

    private ActivityCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, Activity activityParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;


    }

    @Override
    public void injectMainActivity(MainActivity arg0) {
      injectMainActivity2(arg0);
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(getViewModelKeys(), new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl));
    }

    @Override
    public Map<Class<?>, Boolean> getViewModelKeys() {
      return LazyClassKeyMap.<Boolean>of(ImmutableMap.<String, Boolean>builderWithExpectedSize(6).put(LazyClassKeyProvider.com_kinder_petnames_ui_screens_FiltersViewModel, FiltersViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_kinder_petnames_ui_screens_HomeViewModel, HomeViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_kinder_petnames_ui_screens_LikesViewModel, LikesViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_kinder_petnames_ui_screens_MatchesViewModel, MatchesViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_kinder_petnames_ui_screens_OnboardingViewModel, OnboardingViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_kinder_petnames_ui_screens_ProfileViewModel, ProfileViewModel_HiltModules.KeyModule.provide()).build());
    }

    @Override
    public ViewModelComponentBuilder getViewModelComponentBuilder() {
      return new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public FragmentComponentBuilder fragmentComponentBuilder() {
      return new FragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @Override
    public ViewComponentBuilder viewComponentBuilder() {
      return new ViewCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @CanIgnoreReturnValue
    private MainActivity injectMainActivity2(MainActivity instance) {
      MainActivity_MembersInjector.injectPreferencesManager(instance, singletonCImpl.preferencesManagerProvider.get());
      return instance;
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_kinder_petnames_ui_screens_MatchesViewModel = "com.kinder.petnames.ui.screens.MatchesViewModel";

      static String com_kinder_petnames_ui_screens_ProfileViewModel = "com.kinder.petnames.ui.screens.ProfileViewModel";

      static String com_kinder_petnames_ui_screens_HomeViewModel = "com.kinder.petnames.ui.screens.HomeViewModel";

      static String com_kinder_petnames_ui_screens_LikesViewModel = "com.kinder.petnames.ui.screens.LikesViewModel";

      static String com_kinder_petnames_ui_screens_FiltersViewModel = "com.kinder.petnames.ui.screens.FiltersViewModel";

      static String com_kinder_petnames_ui_screens_OnboardingViewModel = "com.kinder.petnames.ui.screens.OnboardingViewModel";

      @KeepFieldType
      MatchesViewModel com_kinder_petnames_ui_screens_MatchesViewModel2;

      @KeepFieldType
      ProfileViewModel com_kinder_petnames_ui_screens_ProfileViewModel2;

      @KeepFieldType
      HomeViewModel com_kinder_petnames_ui_screens_HomeViewModel2;

      @KeepFieldType
      LikesViewModel com_kinder_petnames_ui_screens_LikesViewModel2;

      @KeepFieldType
      FiltersViewModel com_kinder_petnames_ui_screens_FiltersViewModel2;

      @KeepFieldType
      OnboardingViewModel com_kinder_petnames_ui_screens_OnboardingViewModel2;
    }
  }

  private static final class ViewModelCImpl extends PetnamesApplication_HiltComponents.ViewModelC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    private Provider<FiltersViewModel> filtersViewModelProvider;

    private Provider<HomeViewModel> homeViewModelProvider;

    private Provider<LikesViewModel> likesViewModelProvider;

    private Provider<MatchesViewModel> matchesViewModelProvider;

    private Provider<OnboardingViewModel> onboardingViewModelProvider;

    private Provider<ProfileViewModel> profileViewModelProvider;

    private ViewModelCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, SavedStateHandle savedStateHandleParam,
        ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;

      initialize(savedStateHandleParam, viewModelLifecycleParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.filtersViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.homeViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1);
      this.likesViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 2);
      this.matchesViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 3);
      this.onboardingViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 4);
      this.profileViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 5);
    }

    @Override
    public Map<Class<?>, javax.inject.Provider<ViewModel>> getHiltViewModelMap() {
      return LazyClassKeyMap.<javax.inject.Provider<ViewModel>>of(ImmutableMap.<String, javax.inject.Provider<ViewModel>>builderWithExpectedSize(6).put(LazyClassKeyProvider.com_kinder_petnames_ui_screens_FiltersViewModel, ((Provider) filtersViewModelProvider)).put(LazyClassKeyProvider.com_kinder_petnames_ui_screens_HomeViewModel, ((Provider) homeViewModelProvider)).put(LazyClassKeyProvider.com_kinder_petnames_ui_screens_LikesViewModel, ((Provider) likesViewModelProvider)).put(LazyClassKeyProvider.com_kinder_petnames_ui_screens_MatchesViewModel, ((Provider) matchesViewModelProvider)).put(LazyClassKeyProvider.com_kinder_petnames_ui_screens_OnboardingViewModel, ((Provider) onboardingViewModelProvider)).put(LazyClassKeyProvider.com_kinder_petnames_ui_screens_ProfileViewModel, ((Provider) profileViewModelProvider)).build());
    }

    @Override
    public Map<Class<?>, Object> getHiltViewModelAssistedMap() {
      return ImmutableMap.<Class<?>, Object>of();
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_kinder_petnames_ui_screens_FiltersViewModel = "com.kinder.petnames.ui.screens.FiltersViewModel";

      static String com_kinder_petnames_ui_screens_OnboardingViewModel = "com.kinder.petnames.ui.screens.OnboardingViewModel";

      static String com_kinder_petnames_ui_screens_LikesViewModel = "com.kinder.petnames.ui.screens.LikesViewModel";

      static String com_kinder_petnames_ui_screens_ProfileViewModel = "com.kinder.petnames.ui.screens.ProfileViewModel";

      static String com_kinder_petnames_ui_screens_HomeViewModel = "com.kinder.petnames.ui.screens.HomeViewModel";

      static String com_kinder_petnames_ui_screens_MatchesViewModel = "com.kinder.petnames.ui.screens.MatchesViewModel";

      @KeepFieldType
      FiltersViewModel com_kinder_petnames_ui_screens_FiltersViewModel2;

      @KeepFieldType
      OnboardingViewModel com_kinder_petnames_ui_screens_OnboardingViewModel2;

      @KeepFieldType
      LikesViewModel com_kinder_petnames_ui_screens_LikesViewModel2;

      @KeepFieldType
      ProfileViewModel com_kinder_petnames_ui_screens_ProfileViewModel2;

      @KeepFieldType
      HomeViewModel com_kinder_petnames_ui_screens_HomeViewModel2;

      @KeepFieldType
      MatchesViewModel com_kinder_petnames_ui_screens_MatchesViewModel2;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final ViewModelCImpl viewModelCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          ViewModelCImpl viewModelCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.viewModelCImpl = viewModelCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.kinder.petnames.ui.screens.FiltersViewModel 
          return (T) new FiltersViewModel(singletonCImpl.preferencesManagerProvider.get(), singletonCImpl.analyticsManagerProvider.get());

          case 1: // com.kinder.petnames.ui.screens.HomeViewModel 
          return (T) new HomeViewModel(singletonCImpl.namesRepositoryProvider.get(), singletonCImpl.swipesRepositoryProvider.get(), singletonCImpl.preferencesManagerProvider.get(), singletonCImpl.analyticsManagerProvider.get());

          case 2: // com.kinder.petnames.ui.screens.LikesViewModel 
          return (T) new LikesViewModel(singletonCImpl.swipesRepositoryProvider.get(), singletonCImpl.preferencesManagerProvider.get(), singletonCImpl.analyticsManagerProvider.get());

          case 3: // com.kinder.petnames.ui.screens.MatchesViewModel 
          return (T) new MatchesViewModel(singletonCImpl.matchesRepositoryProvider.get(), singletonCImpl.preferencesManagerProvider.get(), singletonCImpl.analyticsManagerProvider.get());

          case 4: // com.kinder.petnames.ui.screens.OnboardingViewModel 
          return (T) new OnboardingViewModel(singletonCImpl.householdRepositoryProvider.get(), singletonCImpl.preferencesManagerProvider.get(), singletonCImpl.analyticsManagerProvider.get(), singletonCImpl.sessionManagerProvider.get());

          case 5: // com.kinder.petnames.ui.screens.ProfileViewModel 
          return (T) new ProfileViewModel(singletonCImpl.householdRepositoryProvider.get(), singletonCImpl.swipesRepositoryProvider.get(), singletonCImpl.preferencesManagerProvider.get(), singletonCImpl.analyticsManagerProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends PetnamesApplication_HiltComponents.ActivityRetainedC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl = this;

    private Provider<ActivityRetainedLifecycle> provideActivityRetainedLifecycleProvider;

    private ActivityRetainedCImpl(SingletonCImpl singletonCImpl,
        SavedStateHandleHolder savedStateHandleHolderParam) {
      this.singletonCImpl = singletonCImpl;

      initialize(savedStateHandleHolderParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandleHolder savedStateHandleHolderParam) {
      this.provideActivityRetainedLifecycleProvider = DoubleCheck.provider(new SwitchingProvider<ActivityRetainedLifecycle>(singletonCImpl, activityRetainedCImpl, 0));
    }

    @Override
    public ActivityComponentBuilder activityComponentBuilder() {
      return new ActivityCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public ActivityRetainedLifecycle getActivityRetainedLifecycle() {
      return provideActivityRetainedLifecycleProvider.get();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // dagger.hilt.android.ActivityRetainedLifecycle 
          return (T) ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory.provideActivityRetainedLifecycle();

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ServiceCImpl extends PetnamesApplication_HiltComponents.ServiceC {
    private final SingletonCImpl singletonCImpl;

    private final ServiceCImpl serviceCImpl = this;

    private ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }
  }

  private static final class SingletonCImpl extends PetnamesApplication_HiltComponents.SingletonC {
    private final ApplicationContextModule applicationContextModule;

    private final SingletonCImpl singletonCImpl = this;

    private Provider<PreferencesManager> preferencesManagerProvider;

    private Provider<AnalyticsManager> analyticsManagerProvider;

    private Provider<SupabaseClient> provideSupabaseClientProvider;

    private Provider<LocalNamesProvider> localNamesProvider;

    private Provider<NamesRepository> namesRepositoryProvider;

    private Provider<SwipesRepository> swipesRepositoryProvider;

    private Provider<MatchesRepository> matchesRepositoryProvider;

    private Provider<HouseholdRepository> householdRepositoryProvider;

    private Provider<SessionManager> sessionManagerProvider;

    private SingletonCImpl(ApplicationContextModule applicationContextModuleParam) {
      this.applicationContextModule = applicationContextModuleParam;
      initialize(applicationContextModuleParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final ApplicationContextModule applicationContextModuleParam) {
      this.preferencesManagerProvider = DoubleCheck.provider(new SwitchingProvider<PreferencesManager>(singletonCImpl, 0));
      this.analyticsManagerProvider = DoubleCheck.provider(new SwitchingProvider<AnalyticsManager>(singletonCImpl, 1));
      this.provideSupabaseClientProvider = DoubleCheck.provider(new SwitchingProvider<SupabaseClient>(singletonCImpl, 3));
      this.localNamesProvider = DoubleCheck.provider(new SwitchingProvider<LocalNamesProvider>(singletonCImpl, 4));
      this.namesRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<NamesRepository>(singletonCImpl, 2));
      this.swipesRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<SwipesRepository>(singletonCImpl, 5));
      this.matchesRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<MatchesRepository>(singletonCImpl, 6));
      this.householdRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<HouseholdRepository>(singletonCImpl, 7));
      this.sessionManagerProvider = DoubleCheck.provider(new SwitchingProvider<SessionManager>(singletonCImpl, 8));
    }

    @Override
    public void injectPetnamesApplication(PetnamesApplication petnamesApplication) {
    }

    @Override
    public Set<Boolean> getDisableFragmentGetContextFix() {
      return ImmutableSet.<Boolean>of();
    }

    @Override
    public ActivityRetainedComponentBuilder retainedComponentBuilder() {
      return new ActivityRetainedCBuilder(singletonCImpl);
    }

    @Override
    public ServiceComponentBuilder serviceComponentBuilder() {
      return new ServiceCBuilder(singletonCImpl);
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.kinder.petnames.core.PreferencesManager 
          return (T) new PreferencesManager(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 1: // com.kinder.petnames.core.AnalyticsManager 
          return (T) new AnalyticsManager(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 2: // com.kinder.petnames.data.NamesRepository 
          return (T) new NamesRepository(singletonCImpl.provideSupabaseClientProvider.get(), singletonCImpl.localNamesProvider.get(), singletonCImpl.preferencesManagerProvider.get());

          case 3: // io.github.jan.supabase.SupabaseClient 
          return (T) SupabaseModule_ProvideSupabaseClientFactory.provideSupabaseClient();

          case 4: // com.kinder.petnames.data.LocalNamesProvider 
          return (T) new LocalNamesProvider(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 5: // com.kinder.petnames.data.SwipesRepository 
          return (T) new SwipesRepository(singletonCImpl.provideSupabaseClientProvider.get());

          case 6: // com.kinder.petnames.data.MatchesRepository 
          return (T) new MatchesRepository(singletonCImpl.provideSupabaseClientProvider.get());

          case 7: // com.kinder.petnames.data.HouseholdRepository 
          return (T) new HouseholdRepository(singletonCImpl.provideSupabaseClientProvider.get());

          case 8: // com.kinder.petnames.core.SessionManager 
          return (T) new SessionManager(singletonCImpl.provideSupabaseClientProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }
}
