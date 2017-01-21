package org.dhis2.ehealthMobile.di;

import android.content.Context;

import org.dhis2.ehealthMobile.network.HTTPClient;
import org.dhis2.ehealthMobile.network.IHttpClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DataModule {

	@Singleton
	@Provides
	public IHttpClient provideHttpClient(Context context){
		return new HTTPClient(context);
	}

	// TODO: provide SharedPrefsUtils and TextFileUtils
}
