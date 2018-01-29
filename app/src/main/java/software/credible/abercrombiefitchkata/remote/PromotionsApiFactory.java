package software.credible.abercrombiefitchkata.remote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import software.credible.abercrombiefitchkata.BuildConfig;
import software.credible.abercrombiefitchkata.dto.ButtonDto;

public class PromotionsApiFactory {

    public static PromotionsApi createService() {
        // This can be defined at an environmental level in the build.gradle file for the app module.
        // An overloaded version that accepts a string is also helpful for testing where the url may be dynamic.
        return createService(BuildConfig.ANF_BASE_URL);
    }

    public static PromotionsApi createService(String url) {

        Retrofit adapter = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return adapter.create(PromotionsApi.class);
    }

    public static Gson gson() {
        // Button sometimes comes down in the payload as an array and sometimes as an object.
        // We're registering a type adapter here to account for that inconsistency by safely
        // converting it into a List of Buttons regardless of which way it comes.
        Type buttonListType = new TypeToken<List<ButtonDto>>() {}.getType();
        GsonBuilder gsonBuilder = new GsonBuilder().registerTypeAdapter(buttonListType, new ButtonTypeAdapter());
        return gsonBuilder.create();
    }
}
