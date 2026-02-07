package com.kinder.petnames.core;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class AnalyticsManager_Factory implements Factory<AnalyticsManager> {
  private final Provider<Context> contextProvider;

  public AnalyticsManager_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public AnalyticsManager get() {
    return newInstance(contextProvider.get());
  }

  public static AnalyticsManager_Factory create(Provider<Context> contextProvider) {
    return new AnalyticsManager_Factory(contextProvider);
  }

  public static AnalyticsManager newInstance(Context context) {
    return new AnalyticsManager(context);
  }
}
