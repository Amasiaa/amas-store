package mg.amas.amasstore

import android.app.Application
import mg.amas.amasstore.di.presentationModule
import mg.amas.data.di.dataModule
import mg.amas.domain.di.domainModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ShopperApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ShopperApp)
            modules(
                listOf(
                    presentationModule,
                    dataModule,
                    domainModule,
                ),
            )
        }
    }
}
