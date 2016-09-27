package com.mobi.movilbox.movilbox.Networking;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Generador de servicios para la comunicacion  con la API Rest.
 */
public class Service {

    public static final String BASE_URL = "http://localhost:8491/";
    public Retrofit retrofit;
    HttpLoggingInterceptor logging = new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY);

    /**
     * Crea el servicio de retrofit para la comunicación con la red
     *
     * @param serviceClass: Contenedor de las interfaces
     * @param token         : Token del usuario
     * @return Retrofit service
     */
    public <S> S createService(Class<S> serviceClass, final String token) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create());

        if (token != null) {
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Interceptor.Chain chain)
                        throws IOException {

                    Request original = chain.request();
                    Request.Builder requestBuilder = original.newBuilder()
                            .method(original.method(), original.body())
                            .addHeader("Authorization", "Bearer " + token);
                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });
        }
        httpClient.addInterceptor(logging);
        OkHttpClient client = httpClient.build();
        retrofit = builder.client(client).build();
        return retrofit.create(serviceClass);
    }
}
