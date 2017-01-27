package org.dhis2.ehealthMobile;

import android.app.Application;
import android.content.Context;

import org.dhis2.ehealthMobile.di.AppModule;
import org.dhis2.ehealthMobile.di.AppPermissionsModule;
import org.dhis2.ehealthMobile.di.DataModule;
import org.dhis2.ehealthMobile.ui.activities.DataEntryActivity;
import org.dhis2.ehealthMobile.ui.activities.LoginActivity;

import javax.inject.Singleton;

import dagger.Component;

public class Dhis2App extends Application {

	@Singleton
	@Component(modules = {AppModule.class, DataModule.class, AppPermissionsModule.class})
	public interface AppComponent {
		WorkService inject(WorkService workService);
		LoginActivity inject(LoginActivity loginActivity);
		DataEntryActivity inject(DataEntryActivity dataEntryActivity);
	}

	public static Dhis2App get(Context context){
		return (Dhis2App) context.getApplicationContext();
	}

	private AppComponent appComponent;

	private AppModule appModule;
	private DataModule dataModule;
	private AppPermissionsModule appPermissionsModule;

	@Override
	public void onCreate() {
		super.onCreate();

		appModule = new AppModule(this);
		dataModule = new DataModule();
		appPermissionsModule = new AppPermissionsModule();

		createAppComponent();
	}

	public void setDataModule(DataModule dataModule) {
		this.dataModule = dataModule;
		createAppComponent();
	}

	public void setAppPermissionsModule(AppPermissionsModule appPermissionsModule){
		this.appPermissionsModule = appPermissionsModule;
		createAppComponent();
	}

	private void createAppComponent() {

		appComponent = DaggerDhis2App_AppComponent.builder()
				.appModule(appModule)
				.dataModule(dataModule)
				.appPermissionsModule(appPermissionsModule)
				.build();
	}

	public AppComponent getComponent() {
		return appComponent;
	}
}
