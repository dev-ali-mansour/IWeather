// Generated by Dagger (https://dagger.dev).
package dev.alimansour.iweather.presentation.di;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dev.alimansour.iweather.data.local.WeatherDatabase;
import javax.inject.Provider;

@DaggerGenerated
@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class RoomModule_ProvideWeatherDatabaseFactory implements Factory<WeatherDatabase> {
  private final Provider<Context> contextProvider;

  public RoomModule_ProvideWeatherDatabaseFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public WeatherDatabase get() {
    return provideWeatherDatabase(contextProvider.get());
  }

  public static RoomModule_ProvideWeatherDatabaseFactory create(Provider<Context> contextProvider) {
    return new RoomModule_ProvideWeatherDatabaseFactory(contextProvider);
  }

  public static WeatherDatabase provideWeatherDatabase(Context context) {
    return Preconditions.checkNotNullFromProvides(RoomModule.INSTANCE.provideWeatherDatabase(context));
  }
}
