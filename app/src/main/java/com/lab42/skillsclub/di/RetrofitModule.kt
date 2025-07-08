package com.lab42.skillsclub.di

import com.lab42.skillsclub.data.SharedPrefImpl
import com.lab42.skillsclub.data.data_base.AppDB
import com.lab42.skillsclub.data.data_base.db_use_case.DBProfileUseCase
import com.lab42.skillsclub.data.interceptor.DIDInterceptor
import com.lab42.skillsclub.data.interceptor.TokenInterceptor
import com.lab42.skillsclub.data.retrofit.ApiServiceImpl
import com.lab42.skillsclub.data.retrofit.RetrofitClient
import com.lab42.skillsclub.domain.use_cases.api_service_use_case.GetDeviceIDUseCase
import com.lab42.skillsclub.domain.use_cases.mapper.CreatePassMapImpl
import com.lab42.skillsclub.domain.use_cases.mapper.CreatePassMapInterface
import com.lab42.skillsclub.domain.use_cases.mapper.DeactivationMapImpl
import com.lab42.skillsclub.domain.use_cases.mapper.DeactivationMapInterface
import com.lab42.skillsclub.domain.use_cases.mapper.DictionaryMapImpl
import com.lab42.skillsclub.domain.use_cases.mapper.DictionaryMapInterface
import com.lab42.skillsclub.domain.use_cases.mapper.GetHelpMapImpl
import com.lab42.skillsclub.domain.use_cases.mapper.GetHelpMapInterface
import com.lab42.skillsclub.domain.use_cases.mapper.GetProgramsByPosInterface
import com.lab42.skillsclub.domain.use_cases.mapper.GetProgramsByPosMapImpl
import com.lab42.skillsclub.domain.use_cases.mapper.GetStepByIdMapImpl
import com.lab42.skillsclub.domain.use_cases.mapper.GetStepByIdMapInterface
import com.lab42.skillsclub.domain.use_cases.mapper.GetStepTasksMapImpl
import com.lab42.skillsclub.domain.use_cases.mapper.HelloMapImpl
import com.lab42.skillsclub.domain.use_cases.mapper.HelloMapInterface
import com.lab42.skillsclub.domain.use_cases.mapper.LoginCheckMapImpl
import com.lab42.skillsclub.domain.use_cases.mapper.LoginCheckMapInterface
import com.lab42.skillsclub.domain.use_cases.mapper.PositionMapImpl
import com.lab42.skillsclub.domain.use_cases.mapper.PositionMapInterface
import com.lab42.skillsclub.domain.use_cases.mapper.RateStepMapImpl
import com.lab42.skillsclub.domain.use_cases.mapper.RateStepMapInterface
import com.lab42.skillsclub.domain.use_cases.mapper.SectionsMapImpl
import com.lab42.skillsclub.domain.use_cases.mapper.SectionsMapInterface
import com.lab42.skillsclub.domain.use_cases.mapper.SentPositionMapImpl
import com.lab42.skillsclub.domain.use_cases.mapper.SentPositionMapInterface
import com.lab42.skillsclub.domain.use_cases.mapper.StepTasksMapInterface
import com.lab42.skillsclub.domain.use_cases.mapper.StepsMapImpl
import com.lab42.skillsclub.domain.use_cases.mapper.StepsMapInterface
import com.lab42.skillsclub.domain.use_cases.mapper.TokenAuthMapImpl
import com.lab42.skillsclub.domain.use_cases.mapper.TokenAuthMapInterface
import com.lab42.skillsclub.domain.use_cases.mapper.UpdatePassMapImpl
import com.lab42.skillsclub.domain.use_cases.mapper.UpdatePassMapInterface
import com.lab42.skillsclub.domain.use_cases.mapper.UpdateStepPassMapImpl
import com.lab42.skillsclub.domain.use_cases.mapper.UpdateStepPassMapInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Provides
    fun rateStepPassMap() : RateStepMapInterface {
        return RateStepMapImpl()
    }

    @Provides
    fun provideUpdateStepPassMap() : UpdateStepPassMapInterface {
        return UpdateStepPassMapImpl()
    }

    @Provides
    fun provideGetStepTaskMap() : StepTasksMapInterface {
        return GetStepTasksMapImpl()
    }

    @Provides
    fun provideGetStepByIdMap() : GetStepByIdMapInterface {
        return GetStepByIdMapImpl()
    }

    @Provides
    fun provideGetHelpMap() : GetHelpMapInterface {
        return GetHelpMapImpl()
    }

    @Provides
    fun provideDeactivationMap(db: AppDB) : DeactivationMapInterface {
        return DeactivationMapImpl(db)
    }

    @Provides
    fun provideUpdatePassMap() : UpdatePassMapInterface {
        return UpdatePassMapImpl()
    }

    @Provides
    fun provideDictionaryMap() : DictionaryMapInterface {
        return DictionaryMapImpl()
    }

    @Provides
    fun provideStepsMap() : StepsMapInterface {
        return StepsMapImpl()
    }

    @Provides
    fun provideSectionsMap() : SectionsMapInterface {
        return SectionsMapImpl()
    }

    @Provides
    fun provideProgramsByPosMap() : GetProgramsByPosInterface {
        return GetProgramsByPosMapImpl()
    }

    @Provides
    fun provideTokenAuthMap(
        dbProfileUseCase: DBProfileUseCase,
        apiService: ApiServiceImpl
    ): TokenAuthMapInterface {
        return TokenAuthMapImpl(dbProfileUseCase, apiService)
    }

    @Provides
    fun provideSentPositionMap(): SentPositionMapInterface {
        return SentPositionMapImpl()
    }

    @Provides
    fun providePositionMap(): PositionMapInterface {
        return PositionMapImpl()
    }

    @Provides
    fun provideCreatePassMapInterface(
        dbProfileUseCase: DBProfileUseCase,
        sharedPrefImpl: SharedPrefImpl,
        apiService: ApiServiceImpl):
            CreatePassMapInterface {
        return CreatePassMapImpl(dbProfileUseCase, sharedPrefImpl, apiService)
    }

    @Provides
    fun provideLoginCheckMapImpl(): LoginCheckMapInterface {
        return LoginCheckMapImpl()
    }

    @Provides
    fun provideHelloMapImpl(): HelloMapInterface {
        return HelloMapImpl()
    }

    @Provides
    fun provideDIDInterceptor(getDeviceIDUseCase: GetDeviceIDUseCase): DIDInterceptor {
        return DIDInterceptor(getDeviceIDUseCase)
    }

    @Provides
    fun provideRetrofit(
        didInterceptor: DIDInterceptor,
        tokenInterceptor: TokenInterceptor
    ): Retrofit {
        return RetrofitClient(didInterceptor, tokenInterceptor).getClient()
    }


}
