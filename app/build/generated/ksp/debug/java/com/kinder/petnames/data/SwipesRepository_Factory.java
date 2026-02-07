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
public final class SwipesRepository_Factory implements Factory<SwipesRepository> {
  private final Provider<SupabaseClient> supabaseProvider;

  public SwipesRepository_Factory(Provider<SupabaseClient> supabaseProvider) {
    this.supabaseProvider = supabaseProvider;
  }

  @Override
  public SwipesRepository get() {
    return newInstance(supabaseProvider.get());
  }

  public static SwipesRepository_Factory create(Provider<SupabaseClient> supabaseProvider) {
    return new SwipesRepository_Factory(supabaseProvider);
  }

  public static SwipesRepository newInstance(SupabaseClient supabase) {
    return new SwipesRepository(supabase);
  }
}
