package io.mixrad.mixradiosdk;

import retrofit.RequestInterceptor;

/**
 * Created by RichardW on 05/03/15.
 */
public class MixRadioUserInterceptor implements RequestInterceptor {

    private String userid;

    @Override public void intercept(RequestFacade request) {

        request.addEncodedPathParam("userid",userid);

    }

    public void setUserId(String userid) {
        this.userid = userid;
    }

}
