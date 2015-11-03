package io.mixrad.mixradiosdk.model;

/**
 * Created by RichardW on 04/03/15.
 */
public class AuthToken {

    /** The current access token. */
    String accessToken;
    /** When the access token expires. */
    String expiresUtc;
    /** The refresh token that can be used to obtain a new AccessToken. */
    String refreshToken;
    /** The user’s country. */
    String territory;
    /** The user ID. */
    String userId;

}
