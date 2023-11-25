package org.helios.mythicdoors.dependencies

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import org.helios.mythicdoors.App
import org.helios.mythicdoors.model.DataController
import org.helios.mythicdoors.navigation.INavFunctions
import org.helios.mythicdoors.navigation.NavFunctionsImp
import org.helios.mythicdoors.utils.connection.Connection
import javax.inject.Singleton

/* Esta clase permitiría usar la DI en la app en una futura factorization. */

@Module
@InstallIn(App::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDataController(dbHelper: Connection): DataController {
        return DataController(dbHelper)
    }

    @Binds
    fun bindNavFunctions(navFunctionsImp: NavFunctionsImp): INavFunctions {
        return navFunctionsImp
    }
}