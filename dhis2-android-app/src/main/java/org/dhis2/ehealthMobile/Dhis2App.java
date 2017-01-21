package org.dhis2.ehealthMobile;

import android.app.Application;
import android.content.Context;

import org.dhis2.ehealthMobile.di.AppModule;
import org.dhis2.ehealthMobile.di.DataModule;

import javax.inject.Singleton;

import dagger.Component;

public class Dhis2App extends Application {

	@Singleton
	@Component(modules = {AppModule.class, DataModule.class})
	public interface AppComponent {
		WorkService inject(WorkService workService);
	}

	public static Dhis2App get(Context context){
		return (Dhis2App) context.getApplicationContext();
	}

	private AppComponent appComponent;

	private AppModule appModule;
	private DataModule dataModule;

	@Override
	public void onCreate() {
		super.onCreate();
		createAppComponent();
	}

	public void setDataModule(DataModule dataModule) {
		this.dataModule = dataModule;
		createAppComponent();
	}

	private void createAppComponent() {

		if (appModule == null)
			appModule = new AppModule(this);

		if (dataModule == null)
			dataModule = new DataModule();

		appComponent = DaggerDhis2App_AppComponent.builder()
				.appModule(appModule)
				.dataModule(dataModule)
				.build();
	}

	public AppComponent getComponent() {
		return appComponent;
	}
}
