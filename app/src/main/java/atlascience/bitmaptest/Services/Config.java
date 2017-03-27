package atlascience.bitmaptest.Services;

public class Config {

    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global";

    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";
    public static final String PUSH_QUESTION = "pushQuestion";
    public static final String PUSH_CAPTURED_ZONE = "captured_zone";

    public static final String SHARED_PREF = "ah_firebase";
}