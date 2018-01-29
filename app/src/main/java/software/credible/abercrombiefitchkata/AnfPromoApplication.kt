package software.credible.abercrombiefitchkata

import android.app.Application
import android.content.Intent

import io.realm.Realm
import io.realm.RealmConfiguration
import software.credible.abercrombiefitchkata.remote.FetchPromosIntentService

class AnfPromoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initRealmWithDefaultConfig()
        schedulePromoFetch()
    }

    private fun initRealmWithDefaultConfig() {
        Realm.init(this)
        Realm.setDefaultConfiguration(
                RealmConfiguration.Builder()
                        .deleteRealmIfMigrationNeeded()
                        .build())
    }

    private fun schedulePromoFetch() {
        val intent = Intent(this, FetchPromosIntentService::class.java)
        startService(intent)
    }

}
