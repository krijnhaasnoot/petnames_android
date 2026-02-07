package com.kinder.petnames.data;

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
public final class LocalNamesProvider_Factory implements Factory<LocalNamesProvider> {
  private final Provider<Context> contextProvider;

  public LocalNamesProvider_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public LocalNamesProvider get() {
    return newInstance(contextProvider.get());
  }

  public static LocalNamesProvider_Factory create(Provider<Context> contextProvider) {
    return new LocalNamesProvider_Factory(contextProvider);
  }

  public static LocalNamesProvider newInstance(Context context) {
    return new LocalNamesProvider(context);
  }
}
