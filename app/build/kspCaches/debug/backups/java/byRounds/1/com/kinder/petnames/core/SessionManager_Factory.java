package com.kinder.petnames.core;

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
public final class SessionManager_Factory implements Factory<SessionManager> {
  private final Provider<SupabaseClient> supabaseProvider;

  public SessionManager_Factory(Provider<SupabaseClient> supabaseProvider) {
    this.supabaseProvider = supabaseProvider;
  }

  @Override
  public SessionManager get() {
    return newInstance(supabaseProvider.get());
  }

  public static SessionManager_Factory create(Provider<SupabaseClient> supabaseProvider) {
    return new SessionManager_Factory(supabaseProvider);
  }

  public static SessionManager newInstance(SupabaseClient supabase) {
    return new SessionManager(supabase);
  }
}
