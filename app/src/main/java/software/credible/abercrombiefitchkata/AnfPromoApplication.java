package software.credible.abercrombiefitchkata;

import android.app.Application;
import android.content.Intent;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import software.credible.abercrombiefitchkata.remote.FetchPromosIntentService;

public class AnfPromoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        setDefaultRealmConfig();
        schedulePromoFetch();
    }

    private void setDefaultRealmConfig() {
        Realm.setDefaultConfiguration(
                new RealmConfiguration.Builder(this)
                        .deleteRealmIfMigrationNeeded()
                        .build());
    }

    private void schedulePromoFetch() {
        Intent intent = new Intent(this, FetchPromosIntentService.class);
        startService(intent);
    }

}
