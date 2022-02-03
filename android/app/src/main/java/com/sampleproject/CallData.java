package com.sampleproject;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextParams;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.ChangedPackages;
import android.content.pm.FeatureInfo;
import android.content.pm.InstrumentationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.pm.SharedLibraryInfo;
import android.content.pm.VersionedPackage;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.view.Display;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.twilio.voice.Call;
import com.twilio.voice.CallInvite;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CallData extends Context {
    public String accessToken;
    public Call activeCall;
    public CallInvite activeCallInvite;
    public int activeCallNotificationId;
    public AudioManager audioManager;
    public boolean isReceiverRegistered;
    public int savedVolumeControlStream;
    public int savedAudioMode;
    public boolean isAudioPermissionGranted;
    public Call.Listener callListener;


    private static final String TAG = "VoiceActivity";
    private static final int MIC_PERMISSION_REQUEST_CODE = 1;



    private static final int PERMISSIONS_REQUEST_CODE = 100;

    public Map<String, String> params = new HashMap <String, String>();

    public static String getTAG() {
        return TAG;
    }

    public static int getMicPermissionRequestCode() {
        return MIC_PERMISSION_REQUEST_CODE;
    }

    public static int getPermissionsRequestCode() {
        return PERMISSIONS_REQUEST_CODE;
    }

    public void setParam(HashMap<String, String> map)
    {
        this.params = map;
    }
    public Map<String, String> getParam()
    {
        return this.params;
    }

    public Call.Listener getCallListener() {
        return callListener;
    }

    public void setCallListener(Call.Listener callListener) {
        this.callListener = callListener;
    }


    /*
     * Audio device management
     */

    public int getSavedVolumeControlStream() {
        return savedVolumeControlStream;
    }

    public void setSavedVolumeControlStream(int savedVolumeControlStream) {
        this.savedVolumeControlStream = savedVolumeControlStream;
    }

    public boolean isReceiverRegistered() {
        return isReceiverRegistered;
    }



    public AudioManager getAudioManager() {
        return audioManager;
    }

    public void setAudioManager(AudioManager audioManager) {
        this.audioManager = audioManager;
    }

    public boolean isAudioPermissionGranted(boolean b) {
        return isAudioPermissionGranted;
    }

    public void setAudioPermissionGranted(boolean audioPermissionGranted) {
        isAudioPermissionGranted = audioPermissionGranted;
    }


    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Call getActiveCall() {
        return activeCall;
    }

    public void setActiveCall(Call activeCall) {
        this.activeCall = activeCall;
    }

    public CallInvite getActiveCallInvite() {
        return activeCallInvite;
    }

    public void setActiveCallInvite(CallInvite activeCallInvite) {
        this.activeCallInvite = activeCallInvite;
    }

    public int getActiveCallNotificationId() {
        return activeCallNotificationId;
    }

    public void setActiveCallNotificationId(int activeCallNotificationId) {
        this.activeCallNotificationId = activeCallNotificationId;
    }


    public boolean isReceiverRegistered(boolean b) {
        return isReceiverRegistered;
    }

    public void setReceiverRegistered(boolean receiverRegistered) {
        isReceiverRegistered = receiverRegistered;
    }


    public int getSavedAudioMode() {
        return savedAudioMode;
    }

    public void setSavedAudioMode(int savedAudioMode) {
        this.savedAudioMode = savedAudioMode;
    }




    @Override
    public AssetManager getAssets() {
        return null;
    }

    @Override
    public Resources getResources() {
        return null;
    }

    @Override
    public PackageManager getPackageManager() {
        PackageManager s = new PackageManager() {
            @Override
            public PackageInfo getPackageInfo(@NonNull String s, int i) throws NameNotFoundException {
                return null;

            }

            @Override
            public PackageInfo getPackageInfo(@NonNull VersionedPackage versionedPackage, int i) throws NameNotFoundException {
                return null;
            }

            @Override
            public String[] currentToCanonicalPackageNames(@NonNull String[] strings) {
                return new String[0];
            }

            @Override
            public String[] canonicalToCurrentPackageNames(@NonNull String[] strings) {
                return new String[0];
            }

            @Nullable
            @Override
            public Intent getLaunchIntentForPackage(@NonNull String s) {
                return null;
            }

            @Nullable
            @Override
            public Intent getLeanbackLaunchIntentForPackage(@NonNull String s) {
                return null;
            }

            @Override
            public int[] getPackageGids(@NonNull String s) throws NameNotFoundException {
                return new int[0];
            }

            @Override
            public int[] getPackageGids(@NonNull String s, int i) throws NameNotFoundException {
                return new int[0];
            }

            @Override
            public int getPackageUid(@NonNull String s, int i) throws NameNotFoundException {
                return 0;
            }

            @Override
            public PermissionInfo getPermissionInfo(@NonNull String s, int i) throws NameNotFoundException {
                return null;
            }

            @NonNull
            @Override
            public List<PermissionInfo> queryPermissionsByGroup(@Nullable String s, int i) throws NameNotFoundException {
                return null;
            }

            @NonNull
            @Override
            public PermissionGroupInfo getPermissionGroupInfo(@NonNull String s, int i) throws NameNotFoundException {
                return null;
            }

            @NonNull
            @Override
            public List<PermissionGroupInfo> getAllPermissionGroups(int i) {
                return null;
            }

            @NonNull
            @Override
            public ApplicationInfo getApplicationInfo(@NonNull String s, int i) throws NameNotFoundException {
                return null;
            }

            @NonNull
            @Override
            public ActivityInfo getActivityInfo(@NonNull ComponentName componentName, int i) throws NameNotFoundException {
                return null;
            }

            @NonNull
            @Override
            public ActivityInfo getReceiverInfo(@NonNull ComponentName componentName, int i) throws NameNotFoundException {
                return null;
            }

            @NonNull
            @Override
            public ServiceInfo getServiceInfo(@NonNull ComponentName componentName, int i) throws NameNotFoundException {
                return null;
            }

            @NonNull
            @Override
            public ProviderInfo getProviderInfo(@NonNull ComponentName componentName, int i) throws NameNotFoundException {
                return null;
            }

            @NonNull
            @Override
            public List<PackageInfo> getInstalledPackages(int i) {
                return null;
            }

            @NonNull
            @Override
            public List<PackageInfo> getPackagesHoldingPermissions(@NonNull String[] strings, int i) {
                return null;
            }

            @SuppressLint("WrongConstant")
            @Override
            public int checkPermission(@NonNull String s, @NonNull String s1) {
                return 0;
            }

            @Override
            public boolean isPermissionRevokedByPolicy(@NonNull String s, @NonNull String s1) {
                return false;
            }

            @Override
            public boolean addPermission(@NonNull PermissionInfo permissionInfo) {
                return false;
            }

            @Override
            public boolean addPermissionAsync(@NonNull PermissionInfo permissionInfo) {
                return false;
            }

            @Override
            public void removePermission(@NonNull String s) {

            }

            @SuppressLint("WrongConstant")
            @Override
            public int checkSignatures(@NonNull String s, @NonNull String s1) {
                return 0;
            }

            @SuppressLint("WrongConstant")
            @Override
            public int checkSignatures(int i, int i1) {
                return 0;
            }

            @Nullable
            @Override
            public String[] getPackagesForUid(int i) {
                return new String[0];
            }

            @Nullable
            @Override
            public String getNameForUid(int i) {
                return null;
            }

            @NonNull
            @Override
            public List<ApplicationInfo> getInstalledApplications(int i) {
                return null;
            }

            @Override
            public boolean isInstantApp() {
                return false;
            }

            @Override
            public boolean isInstantApp(@NonNull String s) {
                return false;
            }

            @Override
            public int getInstantAppCookieMaxBytes() {
                return 0;
            }

            @NonNull
            @Override
            public byte[] getInstantAppCookie() {
                return new byte[0];
            }

            @Override
            public void clearInstantAppCookie() {

            }

            @Override
            public void updateInstantAppCookie(@Nullable byte[] bytes) {

            }

            @Nullable
            @Override
            public String[] getSystemSharedLibraryNames() {
                return new String[0];
            }

            @NonNull
            @Override
            public List<SharedLibraryInfo> getSharedLibraries(int i) {
                return null;
            }

            @Nullable
            @Override
            public ChangedPackages getChangedPackages(int i) {
                return null;
            }

            @NonNull
            @Override
            public FeatureInfo[] getSystemAvailableFeatures() {
                return new FeatureInfo[0];
            }

            @Override
            public boolean hasSystemFeature(@NonNull String s) {
                return true;
            }

            @Override
            public boolean hasSystemFeature(@NonNull String s, int i) {
                return true;
            }

            @Nullable
            @Override
            public ResolveInfo resolveActivity(@NonNull Intent intent, int i) {
                return null;
            }

            @NonNull
            @Override
            public List<ResolveInfo> queryIntentActivities(@NonNull Intent intent, int i) {
                return null;
            }

            @NonNull
            @Override
            public List<ResolveInfo> queryIntentActivityOptions(@Nullable ComponentName componentName, @Nullable Intent[] intents, @NonNull Intent intent, int i) {
                return null;
            }

            @NonNull
            @Override
            public List<ResolveInfo> queryBroadcastReceivers(@NonNull Intent intent, int i) {
                return null;
            }

            @Nullable
            @Override
            public ResolveInfo resolveService(@NonNull Intent intent, int i) {
                return null;
            }

            @NonNull
            @Override
            public List<ResolveInfo> queryIntentServices(@NonNull Intent intent, int i) {
                return null;
            }

            @NonNull
            @Override
            public List<ResolveInfo> queryIntentContentProviders(@NonNull Intent intent, int i) {
                return null;
            }

            @Nullable
            @Override
            public ProviderInfo resolveContentProvider(@NonNull String s, int i) {
                return null;
            }

            @NonNull
            @Override
            public List<ProviderInfo> queryContentProviders(@Nullable String s, int i, int i1) {
                return null;
            }

            @NonNull
            @Override
            public InstrumentationInfo getInstrumentationInfo(@NonNull ComponentName componentName, int i) throws NameNotFoundException {
                return null;
            }

            @NonNull
            @Override
            public List<InstrumentationInfo> queryInstrumentation(@NonNull String s, int i) {
                return null;
            }

            @Nullable
            @Override
            public Drawable getDrawable(@NonNull String s, int i, @Nullable ApplicationInfo applicationInfo) {
                return null;
            }

            @NonNull
            @Override
            public Drawable getActivityIcon(@NonNull ComponentName componentName) throws NameNotFoundException {
                return null;
            }

            @NonNull
            @Override
            public Drawable getActivityIcon(@NonNull Intent intent) throws NameNotFoundException {
                return null;
            }

            @Nullable
            @Override
            public Drawable getActivityBanner(@NonNull ComponentName componentName) throws NameNotFoundException {
                return null;
            }

            @Nullable
            @Override
            public Drawable getActivityBanner(@NonNull Intent intent) throws NameNotFoundException {
                return null;
            }

            @NonNull
            @Override
            public Drawable getDefaultActivityIcon() {
                return null;
            }

            @NonNull
            @Override
            public Drawable getApplicationIcon(@NonNull ApplicationInfo applicationInfo) {
                return null;
            }

            @NonNull
            @Override
            public Drawable getApplicationIcon(@NonNull String s) throws NameNotFoundException {
                return null;
            }

            @Nullable
            @Override
            public Drawable getApplicationBanner(@NonNull ApplicationInfo applicationInfo) {
                return null;
            }

            @Nullable
            @Override
            public Drawable getApplicationBanner(@NonNull String s) throws NameNotFoundException {
                return null;
            }

            @Nullable
            @Override
            public Drawable getActivityLogo(@NonNull ComponentName componentName) throws NameNotFoundException {
                return null;
            }

            @Nullable
            @Override
            public Drawable getActivityLogo(@NonNull Intent intent) throws NameNotFoundException {
                return null;
            }

            @Nullable
            @Override
            public Drawable getApplicationLogo(@NonNull ApplicationInfo applicationInfo) {
                return null;
            }

            @Nullable
            @Override
            public Drawable getApplicationLogo(@NonNull String s) throws NameNotFoundException {
                return null;
            }

            @NonNull
            @Override
            public Drawable getUserBadgedIcon(@NonNull Drawable drawable, @NonNull UserHandle userHandle) {
                return null;
            }

            @NonNull
            @Override
            public Drawable getUserBadgedDrawableForDensity(@NonNull Drawable drawable, @NonNull UserHandle userHandle, @Nullable Rect rect, int i) {
                return null;
            }

            @NonNull
            @Override
            public CharSequence getUserBadgedLabel(@NonNull CharSequence charSequence, @NonNull UserHandle userHandle) {
                return null;
            }

            @Nullable
            @Override
            public CharSequence getText(@NonNull String s, int i, @Nullable ApplicationInfo applicationInfo) {
                return null;
            }

            @Nullable
            @Override
            public XmlResourceParser getXml(@NonNull String s, int i, @Nullable ApplicationInfo applicationInfo) {
                return null;
            }

            @NonNull
            @Override
            public CharSequence getApplicationLabel(@NonNull ApplicationInfo applicationInfo) {
                return null;
            }

            @NonNull
            @Override
            public Resources getResourcesForActivity(@NonNull ComponentName componentName) throws NameNotFoundException {
                return null;
            }

            @NonNull
            @Override
            public Resources getResourcesForApplication(@NonNull ApplicationInfo applicationInfo) throws NameNotFoundException {
                return null;
            }

            @NonNull
            @Override
            public Resources getResourcesForApplication(@NonNull String s) throws NameNotFoundException {
                return null;
            }

            @Override
            public void verifyPendingInstall(int i, int i1) {

            }

            @Override
            public void extendVerificationTimeout(int i, int i1, long l) {

            }

            @Override
            public void setInstallerPackageName(@NonNull String s, @Nullable String s1) {

            }

            @Nullable
            @Override
            public String getInstallerPackageName(@NonNull String s) {
                return null;
            }

            @Override
            public void addPackageToPreferred(@NonNull String s) {

            }

            @Override
            public void removePackageFromPreferred(@NonNull String s) {

            }

            @NonNull
            @Override
            public List<PackageInfo> getPreferredPackages(int i) {
                return null;
            }

            @Override
            public void addPreferredActivity(@NonNull IntentFilter intentFilter, int i, @Nullable ComponentName[] componentNames, @NonNull ComponentName componentName) {

            }

            @Override
            public void clearPackagePreferredActivities(@NonNull String s) {

            }

            @Override
            public int getPreferredActivities(@NonNull List<IntentFilter> list, @NonNull List<ComponentName> list1, @Nullable String s) {
                return 0;
            }

            @Override
            public void setComponentEnabledSetting(@NonNull ComponentName componentName, int i, int i1) {

            }

            @SuppressLint("WrongConstant")
            @Override
            public int getComponentEnabledSetting(@NonNull ComponentName componentName) {
                return 0;
            }

            @Override
            public void setApplicationEnabledSetting(@NonNull String s, int i, int i1) {

            }

            @SuppressLint("WrongConstant")
            @Override
            public int getApplicationEnabledSetting(@NonNull String s) {
                return 0;
            }

            @Override
            public boolean isSafeMode() {
                return false;
            }

            @Override
            public void setApplicationCategoryHint(@NonNull String s, int i) {

            }

            @NonNull
            @Override
            public PackageInstaller getPackageInstaller() {
                return null;
            }

            @Override
            public boolean canRequestPackageInstalls() {
                return false;
            }
        };
        return s;
    }

    @Override
    public ContentResolver getContentResolver() {
        return null;
    }

    @Override
    public Looper getMainLooper() {
        return null;
    }

    @Override
    public Context getApplicationContext() {
        return this;
    }

    @Override
    public void setTheme(int i) {

    }

    @Override
    public Resources.Theme getTheme() {
        return null;
    }

    @Override
    public ClassLoader getClassLoader() {
        return null;
    }

    @Override
    public String getPackageName() {
        return null;
    }

    @Override
    public ApplicationInfo getApplicationInfo() {
        return null;
    }

    @Override
    public String getPackageResourcePath() {
        return null;
    }

    @Override
    public String getPackageCodePath() {
        return null;
    }

    @Override
    public SharedPreferences getSharedPreferences(String s, int i) {
        return null;
    }

    @Override
    public boolean moveSharedPreferencesFrom(Context context, String s) {
        return false;
    }

    @Override
    public boolean deleteSharedPreferences(String s) {
        return false;
    }

    @Override
    public FileInputStream openFileInput(String s) throws FileNotFoundException {
        return null;
    }

    @Override
    public FileOutputStream openFileOutput(String s, int i) throws FileNotFoundException {
        return null;
    }

    @Override
    public boolean deleteFile(String s) {
        return false;
    }

    @Override
    public File getFileStreamPath(String s) {
        return null;
    }

    @Override
    public File getDataDir() {
        return null;
    }

    @Override
    public File getFilesDir() {
        return null;
    }

    @Override
    public File getNoBackupFilesDir() {
        return null;
    }

    @Nullable
    @Override
    public File getExternalFilesDir(@Nullable String s) {
        return null;
    }

    @Override
    public File[] getExternalFilesDirs(String s) {
        return new File[0];
    }

    @Override
    public File getObbDir() {
        return null;
    }

    @Override
    public File[] getObbDirs() {
        return new File[0];
    }

    @Override
    public File getCacheDir() {
        return null;
    }

    @Override
    public File getCodeCacheDir() {
        return null;
    }

    @Nullable
    @Override
    public File getExternalCacheDir() {
        return null;
    }

    @Override
    public File[] getExternalCacheDirs() {
        return new File[0];
    }

    @Override
    public File[] getExternalMediaDirs() {
        return new File[0];
    }

    @Override
    public String[] fileList() {
        return new String[0];
    }

    @Override
    public File getDir(String s, int i) {
        return null;
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(String s, int i, SQLiteDatabase.CursorFactory cursorFactory) {
        return null;
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(String s, int i, SQLiteDatabase.CursorFactory cursorFactory, @Nullable DatabaseErrorHandler databaseErrorHandler) {
        return null;
    }

    @Override
    public boolean moveDatabaseFrom(Context context, String s) {
        return false;
    }

    @Override
    public boolean deleteDatabase(String s) {
        return false;
    }

    @Override
    public File getDatabasePath(String s) {
        return null;
    }

    @Override
    public String[] databaseList() {
        return new String[0];
    }

    @Override
    public Drawable getWallpaper() {
        return null;
    }

    @Override
    public Drawable peekWallpaper() {
        return null;
    }

    @Override
    public int getWallpaperDesiredMinimumWidth() {
        return 0;
    }

    @Override
    public int getWallpaperDesiredMinimumHeight() {
        return 0;
    }

    @Override
    public void setWallpaper(Bitmap bitmap) throws IOException {

    }

    @Override
    public void setWallpaper(InputStream inputStream) throws IOException {

    }

    @Override
    public void clearWallpaper() throws IOException {

    }

    @Override
    public void startActivity(Intent intent) {

    }

    @Override
    public void startActivity(Intent intent, @Nullable Bundle bundle) {

    }

    @Override
    public void startActivities(Intent[] intents) {

    }

    @Override
    public void startActivities(Intent[] intents, Bundle bundle) {

    }

    @Override
    public void startIntentSender(IntentSender intentSender, @Nullable Intent intent, int i, int i1, int i2) throws IntentSender.SendIntentException {

    }

    @Override
    public void startIntentSender(IntentSender intentSender, @Nullable Intent intent, int i, int i1, int i2, @Nullable Bundle bundle) throws IntentSender.SendIntentException {

    }

    @Override
    public void sendBroadcast(Intent intent) {

    }

    @Override
    public void sendBroadcast(Intent intent, @Nullable String s) {

    }

    @Override
    public void sendOrderedBroadcast(Intent intent, @Nullable String s) {

    }

    @Override
    public void sendOrderedBroadcast(@NonNull Intent intent, @Nullable String s, @Nullable BroadcastReceiver broadcastReceiver, @Nullable Handler handler, int i, @Nullable String s1, @Nullable Bundle bundle) {

    }

    @Override
    public void sendBroadcastAsUser(Intent intent, UserHandle userHandle) {

    }

    @Override
    public void sendBroadcastAsUser(Intent intent, UserHandle userHandle, @Nullable String s) {

    }

    @Override
    public void sendOrderedBroadcastAsUser(Intent intent, UserHandle userHandle, @Nullable String s, BroadcastReceiver broadcastReceiver, @Nullable Handler handler, int i, @Nullable String s1, @Nullable Bundle bundle) {

    }

    @Override
    public void sendStickyBroadcast(Intent intent) {

    }

    @Override
    public void sendStickyOrderedBroadcast(Intent intent, BroadcastReceiver broadcastReceiver, @Nullable Handler handler, int i, @Nullable String s, @Nullable Bundle bundle) {

    }

    @Override
    public void removeStickyBroadcast(Intent intent) {

    }

    @Override
    public void sendStickyBroadcastAsUser(Intent intent, UserHandle userHandle) {

    }

    @Override
    public void sendStickyOrderedBroadcastAsUser(Intent intent, UserHandle userHandle, BroadcastReceiver broadcastReceiver, @Nullable Handler handler, int i, @Nullable String s, @Nullable Bundle bundle) {

    }

    @Override
    public void removeStickyBroadcastAsUser(Intent intent, UserHandle userHandle) {

    }

    @Nullable
    @Override
    public Intent registerReceiver(@Nullable BroadcastReceiver broadcastReceiver, IntentFilter intentFilter) {
        return null;
    }

    @Nullable
    @Override
    public Intent registerReceiver(@Nullable BroadcastReceiver broadcastReceiver, IntentFilter intentFilter, int i) {
        return null;
    }

    @Nullable
    @Override
    public Intent registerReceiver(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter, @Nullable String s, @Nullable Handler handler) {
        return null;
    }

    @Nullable
    @Override
    public Intent registerReceiver(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter, @Nullable String s, @Nullable Handler handler, int i) {
        return null;
    }

    @Override
    public void unregisterReceiver(BroadcastReceiver broadcastReceiver) {

    }

    @Nullable
    @Override
    public ComponentName startService(Intent intent) {
        return null;
    }

    @Nullable
    @Override
    public ComponentName startForegroundService(Intent intent) {
        return null;
    }

    @Override
    public boolean stopService(Intent intent) {
        return false;
    }

    @Override
    public boolean bindService(Intent intent, @NonNull ServiceConnection serviceConnection, int i) {
        return false;
    }

    @Override
    public void unbindService(@NonNull ServiceConnection serviceConnection) {

    }

    @Override
    public boolean startInstrumentation(@NonNull ComponentName componentName, @Nullable String s, @Nullable Bundle bundle) {
        return false;
    }

    @Override
    public Object getSystemService(@NonNull String s) {
        return audioManager;
    }

    @Nullable
    @Override
    public String getSystemServiceName(@NonNull Class<?> aClass) {
        return null;
    }

    @SuppressLint("WrongConstant")
    @Override
    public int checkPermission(@NonNull String s, int i, int i1) {
        return 0;
    }

    @SuppressLint("WrongConstant")
    @Override
    public int checkCallingPermission(@NonNull String s) {
        return 0;
    }

    @SuppressLint("WrongConstant")
    public int checkCallingOrSelfPermission(String var){
        return 0;
    }

    @SuppressLint("WrongConstant")
    @Override
    public int checkSelfPermission(@NonNull String s) {
        return 0;
    }

    @Override
    public void enforcePermission(@NonNull String s, int i, int i1, @Nullable String s1) {

    }

    @Override
    public void enforceCallingPermission(@NonNull String s, @Nullable String s1) {

    }

    @Override
    public void enforceCallingOrSelfPermission(@NonNull String s, @Nullable String s1) {

    }

    @Override
    public void grantUriPermission(String s, Uri uri, int i) {

    }

    @Override
    public void revokeUriPermission(Uri uri, int i) {

    }

    @Override
    public void revokeUriPermission(String s, Uri uri, int i) {

    }

    @SuppressLint("WrongConstant")
    @Override
    public int checkUriPermission(Uri uri, int i, int i1, int i2) {
        return 0;
    }

    @SuppressLint("WrongConstant")
    @Override
    public int checkCallingUriPermission(Uri uri, int i) {
        return 0;
    }

    @SuppressLint("WrongConstant")
    @Override
    public int checkCallingOrSelfUriPermission(Uri uri, int i) {
        return 0;
    }

    @SuppressLint("WrongConstant")
    @Override
    public int checkUriPermission(@Nullable Uri uri, @Nullable String s, @Nullable String s1, int i, int i1, int i2) {
        return 0;
    }

    @Override
    public void enforceUriPermission(Uri uri, int i, int i1, int i2, String s) {

    }

    @Override
    public void enforceCallingUriPermission(Uri uri, int i, String s) {

    }

    @Override
    public void enforceCallingOrSelfUriPermission(Uri uri, int i, String s) {

    }

    @Override
    public void enforceUriPermission(@Nullable Uri uri, @Nullable String s, @Nullable String s1, int i, int i1, int i2, @Nullable String s2) {

    }

    @Override
    public Context createPackageContext(String s, int i) throws PackageManager.NameNotFoundException {
        return null;
    }

    @Override
    public Context createContextForSplit(String s) throws PackageManager.NameNotFoundException {
        return null;
    }

    @Override
    public Context createConfigurationContext(@NonNull Configuration configuration) {
        return null;
    }

    @Override
    public Context createDisplayContext(@NonNull Display display) {
        return null;
    }

    @Override
    public Context createDeviceProtectedStorageContext() {
        return null;
    }

    @Override
    public boolean isDeviceProtectedStorage() {
        return false;
    }


}
