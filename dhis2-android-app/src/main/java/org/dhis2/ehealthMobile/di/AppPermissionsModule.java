package org.dhis2.ehealthMobile.di;

import org.dhis2.ehealthMobile.utils.AppPermissions;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppPermissionsModule {

	@Singleton
	@Provides
	public AppPermissions provideAppPermissions(){
		return new AppPermissions();
	}
}
