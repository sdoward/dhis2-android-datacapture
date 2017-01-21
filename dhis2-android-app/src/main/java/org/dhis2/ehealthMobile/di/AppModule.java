package org.dhis2.ehealthMobile.di;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

	private Context context;

	public AppModule(Context context){
		this.context = context.getApplicationContext();
	}

	@Singleton
	@Provides
	public Context provideContext(){
		return context;
	}
}
