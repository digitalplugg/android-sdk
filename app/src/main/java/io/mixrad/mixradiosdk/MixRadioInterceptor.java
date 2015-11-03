package io.mixrad.mixradiosdk;

import retrofit.RequestInterceptor;

/**
 * Created by RichardW on 05/03/15.
 */
public class MixRadioInterceptor implements RequestInterceptor {

    private String apiKey;
    private String countryCode;

    @Override public void intercept(RequestFacade request) {

        request.addQueryParam("client_id", apiKey);

        request.addQueryParam("domain", "music");

        request.addEncodedPathParam("countrycode",countryCode);

    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

}
