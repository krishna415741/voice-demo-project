package com.sampleproject;
//import static com.twilio.voice.Utils.isAudioPermissionGranted;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
//import com.google.firebase.iid.FirebaseInstanceId;
import com.facebook.react.modules.core.DeviceEventManagerModule;
//import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.twilio.jwt.accesstoken.AccessToken;
import com.twilio.jwt.accesstoken.VoiceGrant;
import com.twilio.twiml.TwiMLException;
import com.twilio.voice.CallException;
import com.twilio.voice.CallInvite;
import com.twilio.voice.ConnectOptions;
//import com.twilio.voice.LocalAudioTrack;
//import com.twilio.voice.Preconditions;
import com.twilio.voice.RegistrationException;
import com.twilio.voice.RegistrationListener;
import com.twilio.voice.Voice;

import org.json.JSONStringer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.Executor;

public class TwilioModule extends ReactContextBaseJavaModule implements  {
    private static final int MIC_PERMISSION_REQUEST_CODE = 1;
    private com.twilio.voice.Call activeCall;
    private static Context context;
    private AudioManager audioManager;
    private int savedAudioMode = AudioManager.MODE_NORMAL;
    private String SID;
    private String KEY_SID;
    private String SECRET_KEY;
    private String ACCESS_TOKEN;
    private String APPLICATION_SID;
    private ReactContext reactContext;
    RegistrationListener registrationListener = registrationListener();
    private VoiceBroadcastReceiver voiceBroadcastReceiver;
    private boolean isReceiverRegistered = false;
    private CallInvite activeCallInvite;
    private NotificationManager notificationManager;
    private int activeCallNotificationId;

    com.twilio.voice.Call.Listener callListener = callListener();


    private static final String TAG = "TwilioModule";


    TwilioModule(ReactApplicationContext context) {
        super(context);
        this.context = context;
        reactContext = context;
        registerForCallInvites();

        /*
         * Setup the broadcast receiver to be notified of FCM Token updates
         * or incoming call invite in this Activity.
         */
        voiceBroadcastReceiver = new VoiceBroadcastReceiver();
        registerReceiver();

//        handleIncomingCallIntent(getIntent());

    }

    private void registerReceiver() {
        if (!isReceiverRegistered) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Constants.ACTION_INCOMING_CALL);
            intentFilter.addAction(Constants.ACTION_CANCEL_CALL);
            intentFilter.addAction(Constants.ACTION_FCM_TOKEN);
            LocalBroadcastManager.getInstance(context).registerReceiver(
                    voiceBroadcastReceiver, intentFilter);
            isReceiverRegistered = true;
        }
    }

    private void unregisterReceiver() {
        if (isReceiverRegistered) {
            LocalBroadcastManager.getInstance(context).unregisterReceiver(voiceBroadcastReceiver);
            isReceiverRegistered = false;
        }
    }


    private void sendEvent(ReactContext reactContext,
                           String eventName,
                           @Nullable String params) {
        reactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }
    @ReactMethod
    public void addListener(String eventName) {
        sendEvent(reactContext, "EventReminder", "good");
        // Set up any upstream listeners or background tasks as necessary
    }

    @ReactMethod
    public void removeListeners(Integer count) {
        // Remove upstream listeners, stop unnecessary background tasks
    }

    private class VoiceBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null && (action.equals(Constants.ACTION_INCOMING_CALL) || action.equals(Constants.ACTION_CANCEL_CALL))) {
                /*
                 * Handle the incoming or cancelled call invite
                 */
                handleIncomingCallIntent(intent);
//                answer();
//                sendEvent(reactContext, "CallEnded", intent.toString());

            }
        }
    }

    private void handleIncomingCallIntent(Intent intent) {
        if (intent != null && intent.getAction() != null) {
            String action = intent.getAction();
            activeCallInvite = intent.getParcelableExtra(Constants.INCOMING_CALL_INVITE);
            activeCallNotificationId = intent.getIntExtra(Constants.INCOMING_CALL_NOTIFICATION_ID, 0);

            switch (action) {
//                case Constants.ACTION_INCOMING_CALL:
//                    handleIncomingCall();
//                    break;
                case Constants.ACTION_INCOMING_CALL_NOTIFICATION:
//                    showIncomingCallDialog();
                    break;
                case Constants.ACTION_CANCEL_CALL:
//                    handleCancel();
                    sendEvent(reactContext, "CallEnded", "Call has been ended");
//                    answer();
                    break;
                case Constants.ACTION_FCM_TOKEN:
                    registerForCallInvites();
                    break;
                case Constants.ACTION_ACCEPT:
                    answer();
                    break;
                default:
                    break;
            }
        }
    }


    @ReactMethod
    public void generateAccessToken(String accountSid, String keySid, String secret, String applicationSid) {
        try {
            SID = accountSid;
            KEY_SID = keySid;
            SECRET_KEY = secret;
            APPLICATION_SID = applicationSid;
            ACCESS_TOKEN = getAccessToken("alice");
            Log.d("Auth Token generation", "Created accessToken successfully : " + ACCESS_TOKEN);

        } catch (Exception e) {
            Log.d("Auth Token generation", "Error generating Access Token : " + e);
        }
    }

    @ReactMethod
    private void muteCalls() {
        if (activeCall != null) {
            boolean mute = !activeCall.isMuted();
            activeCall.mute(mute);
        }
    }


    @ReactMethod
    public void holdCalls() {
            if (activeCall != null) {
                boolean hold = !activeCall.isOnHold();
                activeCall.hold(hold);
                Log.d("Call Status", "Call on hold successfully");
            }
    }

    private String getAccessToken(final String identity) {
        VoiceGrant grant = new VoiceGrant();
        grant.setOutgoingApplicationSid(APPLICATION_SID);
        AccessToken token = new AccessToken.Builder(
                SID,
                KEY_SID,
                SECRET_KEY
        ).identity(identity).grant(grant).build();
        System.out.println(token.toJwt());
        ACCESS_TOKEN = token.toJwt();
        return token.toJwt();
    }

    private void registerForCallInvites() {
//        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(context, instanceIdResult -> {
//            String fcmToken = instanceIdResult.getToken();
            Log.i(TAG, "Registering with FCM");
//            FirebaseMessaging fcmToken =  FirebaseMessaging.getInstance();
//        fcmToken.getToken().addOnSuccessListener(context, instanceResult -> {
//            Log.i(TAG, instanceResult);
//        });
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
//                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG, token);
//                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                        String accountSid = "AC00651d84b23073f39fa767deee83ebb2";
                        String keySid = "SKf7c9b9db85953694f613c0f977ad4bcc";
                        String secret = "y6QKBAl4lxZggzOojQjIKRmfvR4mdBur";
                        String applicationSid = "APcbbbbc534c9fe8b089e1f4d397aa253a";
                        generateAccessToken(accountSid, keySid, secret, applicationSid);
//                        String accessToken2 = getAccessToken("alice");
                        String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCIsImN0eSI6InR3aWxpby1mcGE7dj0xIn0.eyJqdGkiOiJTS2Y3YzliOWRiODU5NTM2OTRmNjEzYzBmOTc3YWQ0YmNjLTE2NDQzMjYwNDIiLCJncmFudHMiOnsiaWRlbnRpdHkiOiJhbGljZSIsInZvaWNlIjp7ImluY29taW5nIjp7ImFsbG93Ijp0cnVlfSwib3V0Z29pbmciOnsiYXBwbGljYXRpb25fc2lkIjoiQVBjYmJiYmM1MzRjOWZlOGIwODllMWY0ZDM5N2FhMjUzYSJ9LCJwdXNoX2NyZWRlbnRpYWxfc2lkIjoiQ1IwODUzMDU0ZjdjMzVhZjZhMGU3OTRiNjBlODYwYjk3NCJ9fSwiaWF0IjoxNjQ0MzI2MDQyLCJleHAiOjE2NDQzMjk2NDIsImlzcyI6IlNLZjdjOWI5ZGI4NTk1MzY5NGY2MTNjMGY5NzdhZDRiY2MiLCJzdWIiOiJBQzAwNjUxZDg0YjIzMDczZjM5ZmE3NjdkZWVlODNlYmIyIn0.GaPeOvyi7EDV5-j6K6ypAaq3P1W9JOHlpD-3crljCSA";
                        Voice.register(accessToken, Voice.RegistrationChannel.FCM, token, registrationListener);

                    }
                });
//                .addOnSuccessListener(this, instanceIdResult -> {
//                String token = instanceIdResult;
//            });
            /* String accessToken1 = getAccessToken("alice"); */

//        });

    }

    private void answer() {
        setAudioFocus(true);
        sendEvent(reactContext, "CallEnded", "Call has been started");

//        SoundPoolManager.getInstance(context).stopRinging();
//        activeCallInvite.accept(context, callListener);
//        notificationManager.cancel(activeCallNotificationId);
//        setCallUI();
//        if (alertDialog != null && alertDialog.isShowing()) {
//            alertDialog.dismiss();
//        }
    }



    private void setAudioFocus(boolean setFocus) {
        if (audioManager != null) {
            if (setFocus) {
                savedAudioMode = audioManager.getMode();
                // Request audio focus before making any device switch.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    AudioAttributes playbackAttributes = new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_VOICE_COMMUNICATION)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                            .build();
                    AudioFocusRequest focusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT)
                            .setAudioAttributes(playbackAttributes)
                            .setAcceptsDelayedFocusGain(true)
                            .setOnAudioFocusChangeListener(i -> {
                            })
                            .build();
                    audioManager.requestAudioFocus(focusRequest);
                } else {
                    audioManager.requestAudioFocus(
                            focusChange -> {
                            },
                            AudioManager.STREAM_VOICE_CALL,
                            AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                }
                /*
                 * Start by setting MODE_IN_COMMUNICATION as default audio mode. It is
                 * required to be in this mode when playout and/or recording starts for
                 * best possible VoIP performance. Some devices have difficulties with speaker mode
                 * if this is not set.
                 */
                audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
            } else {
                audioManager.setMode(savedAudioMode);
                audioManager.abandonAudioFocus(null);
            }
        }
    };

    public TwilioModule getData(){
        return this;
    }

    @SuppressLint("WrongConstant")
    @ReactMethod
    public void createCallEvent(String number, Callback callback) throws IOException {
        HashMap<String, String> params = new HashMap<>();
        params.put("to", number);
//        String accessToken = getAccessToken("alice");
        ConnectOptions connectOptions = new ConnectOptions.Builder(ACCESS_TOKEN)
                .params(params)
                .build();
        CallData cl = new CallData();
        cl.setAccessToken(ACCESS_TOKEN);
        cl.setActiveCall(null);
        cl.setActiveCallInvite(null);
        cl.setParam(params);
        cl.setReceiverRegistered(false);
        cl.setAudioPermissionGranted(true);
        audioManager = (AudioManager) getCurrentActivity().getSystemService("audio");
        audioManager.setSpeakerphoneOn(true);
        cl.setAudioManager(audioManager);
        cl.setCallListener(callListener);

//        Call call = Call.creator(
//                new com.twilio.type.PhoneNumber("+15878442715"),
//                new com.twilio.type.PhoneNumber("+918309677743"),
//                newUri)
//                .create();
        TwilioModule.context =cl;
        activeCall = Voice.connect( TwilioModule.context, connectOptions, callListener);
        System.out.println(activeCall);
        callback.invoke("Call placed Successfully");
        Log.d("Call Status", "Call placed Successfully : ");

    }


    @ReactMethod
    public void rejectCalls(){
        try {
            activeCall.disconnect();
        } catch (TwiMLException e) {
            e.printStackTrace();
        }
    }


    private com.twilio.voice.Call.Listener callListener() {
        return new com.twilio.voice.Call.Listener() {
            @Override
            public void onConnectFailure(@NonNull com.twilio.voice.Call call, @NonNull CallException callException) {
                setAudioFocus(false);

            }

            @Override
            public void onRinging(@NonNull com.twilio.voice.Call call) {
                setAudioFocus(true);
            }

            @Override
            public void onConnected(@NonNull com.twilio.voice.Call call) {
                setAudioFocus(true);

            }

            @Override
            public void onReconnecting(@NonNull com.twilio.voice.Call call, @NonNull CallException callException) {

            }

            @Override
            public void onReconnected(@NonNull com.twilio.voice.Call call) {
                setAudioFocus(true);
            }

            @Override
            public void onDisconnected(@NonNull com.twilio.voice.Call call, @Nullable CallException callException) {
                setAudioFocus(false);
                sendEvent(reactContext, "CallEnded", "Call has been ended");
            }

        };
    }

    private RegistrationListener registrationListener() {
        return new RegistrationListener() {
            @Override
            public void onRegistered(@NonNull String accessToken, @NonNull String fcmToken) {
                Log.d(TAG, "Successfully registered FCM " + fcmToken);
            }

            @Override
            public void onError(@NonNull RegistrationException error,
                                @NonNull String accessToken,
                                @NonNull String fcmToken) {
                String message = String.format(
                        Locale.US,
                        "Registration Error: %d, %s",
                        error.getErrorCode(),
                        error.getMessage());
                Log.e(TAG, message);
            }
        };
    }

    @NonNull
    @Override
    public String getName() {
        return "TwilioModule";
    }
}


