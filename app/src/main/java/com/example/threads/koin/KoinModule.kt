import com.example.threads.data.api.AuthAPI
import com.example.threads.data.api.ThreadActionAPI
import com.example.threads.data.api.UserDataAPI
import com.example.threads.data.repositories.AuthRepository
import com.example.threads.data.repositories.ThreadRepository
import com.example.threads.data.repositories.UserDataRepository
import com.example.threads.utils.AuthInterceptor
import com.example.threads.utils.Constants
import com.example.threads.utils.PreferenceManager
import com.example.threads.view_models.AuthViewModel
import com.example.threads.view_models.ThreadViewModel
import com.example.threads.view_models.UserDataViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


val retrofitModule = module {
    single { getOkHttp() }
    single { getRetrofitInstance(okHttpClient = get()) }
    single { getAuthApi(retrofit = get()) }
    single { getUserDataAPI(retrofit = get()) }
    single { getThreadActionAPI(retrofit = get()) }
    factory { AuthRepository(authAPI = get()) }
    factory { UserDataRepository(userDataAPI = get()) }
    factory { ThreadRepository(threadActionAPI = get()) }
}

val viewModels = module {
    viewModel { AuthViewModel(authRepository = get()) }
    viewModel { UserDataViewModel(userDataRepository = get()) }
    viewModel { ThreadViewModel(threadRepository = get())}
}

fun getOkHttp(): OkHttpClient {
    return OkHttpClient.Builder()
        .addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .connectTimeout(3000, TimeUnit.SECONDS)
//        .addInterceptor(AuthInterceptor(preferenceManager))
        .readTimeout(3000, TimeUnit.SECONDS)
        .build()
}

fun getRetrofitInstance(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()
}

fun getAuthApi(retrofit: Retrofit): AuthAPI {
    return retrofit.create(AuthAPI::class.java)
}

fun getUserDataAPI(retrofit: Retrofit): UserDataAPI {
    return retrofit.create(UserDataAPI::class.java)
}

fun getThreadActionAPI(retrofit: Retrofit): ThreadActionAPI {
    return retrofit.create(ThreadActionAPI::class.java)
}