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
public final class HouseholdRepository_Factory implements Factory<HouseholdRepository> {
  private final Provider<SupabaseClient> supabaseProvider;

  public HouseholdRepository_Factory(Provider<SupabaseClient> supabaseProvider) {
    this.supabaseProvider = supabaseProvider;
  }

  @Override
  public HouseholdRepository get() {
    return newInstance(supabaseProvider.get());
  }

  public static HouseholdRepository_Factory create(Provider<SupabaseClient> supabaseProvider) {
    return new HouseholdRepository_Factory(supabaseProvider);
  }

  public static HouseholdRepository newInstance(SupabaseClient supabase) {
    return new HouseholdRepository(supabase);
  }
}
