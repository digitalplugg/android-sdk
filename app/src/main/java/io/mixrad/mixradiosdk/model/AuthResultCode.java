package io.mixrad.mixradiosdk.model;

/**
 * Created by RichardW on 04/03/15.
 */
public enum AuthResultCode {

    /** Completed Successfully. */
    SUCCESS,
    /** Cancelled by user. */
    CANCELLED,
    /** Access denied by user. */
    ACCESS_DENIED,
    /** The client id was not valid. */
    UNAUTHORIZED_CLIENT,
    /** An invalid scope was specified. */
    INVALID_SCOPE,
    /** A server error occurred. */
    SERVER_ERROR,
    /** An error occurred trying to refresh an existing token. */
    FAILED_TO_REFRESH,
    /** A silent refresh from an existing token was not possible as there is no cached token. */
    NO_CACHED_TOKEN,
    /** The authorise process is in progress (Windows Phone 8.1 only). */
    IN_PROGRESS,

}
