package com.kinder.petnames.data;

import com.kinder.petnames.core.PreferencesManager;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import io.github.jan.supabase.SupabaseClient;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class NamesRepository_Factory implements Factory<NamesRepository> {
  private final Provider<SupabaseClient> supabaseProvider;

  private final Provider<LocalNamesProvider> localNamesProvider;

  private final Provider<PreferencesManager> preferencesManagerProvider;

  public NamesRepository_Factory(Provider<SupabaseClient> supabaseProvider,
      Provider<LocalNamesProvider> localNamesProvider,
      Provider<PreferencesManager> preferencesManagerProvider) {
    this.supabaseProvider = supabaseProvider;
    this.localNamesProvider = localNamesProvider;
    this.preferencesManagerProvider = preferencesManagerProvider;
  }

  @Override
  public NamesRepository get() {
    return newInstance(supabaseProvider.get(), localNamesProvider.get(), preferencesManagerProvider.get());
  }

  public static NamesRepository_Factory create(Provider<SupabaseClient> supabaseProvider,
      Provider<LocalNamesProvider> localNamesProvider,
      Provider<PreferencesManager> preferencesManagerProvider) {
    return new NamesRepository_Factory(supabaseProvider, localNamesProvider, preferencesManagerProvider);
  }

  public static NamesRepository newInstance(SupabaseClient supabase,
      LocalNamesProvider localNamesProvider, PreferencesManager preferencesManager) {
    return new NamesRepository(supabase, localNamesProvider, preferencesManager);
  }
}
