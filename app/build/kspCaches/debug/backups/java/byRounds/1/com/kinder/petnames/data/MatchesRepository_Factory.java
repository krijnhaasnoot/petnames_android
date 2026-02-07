package com.kinder.petnames.data;

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
public final class MatchesRepository_Factory implements Factory<MatchesRepository> {
  private final Provider<SupabaseClient> supabaseProvider;

  public MatchesRepository_Factory(Provider<SupabaseClient> supabaseProvider) {
    this.supabaseProvider = supabaseProvider;
  }

  @Override
  public MatchesRepository get() {
    return newInstance(supabaseProvider.get());
  }

  public static MatchesRepository_Factory create(Provider<SupabaseClient> supabaseProvider) {
    return new MatchesRepository_Factory(supabaseProvider);
  }

  public static MatchesRepository newInstance(SupabaseClient supabase) {
    return new MatchesRepository(supabase);
  }
}
