package mg.amas.amasstore.di

import org.koin.dsl.module

val presentationModule =
    module {
        includes(viewModelModule)
    }
