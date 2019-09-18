Sample for Broken AbstractSavedStateViewModelFactory
===================================================

Sample project for bug https://issuetracker.google.com/issues/141225984

Component used: lifecycle-viewmodel-savedstate
Version used: 2.2.0-alpha05
Devices/Android versions reproduced on: Android 10

AbstractSavedStateViewModelFactory throws an IllegalStateException when creating a ViewModel.


```
 java.lang.RuntimeException: Unable to start activity ComponentInfo{com.geekorum.ttrss.free/com.geekorum.ttrss.MainActivity}: java.lang.IllegalStateException: Already attached to lifecycleOwner
        at android.app.ActivityThread.performLaunchActivity(ActivityThread.java:3270)
        at android.app.ActivityThread.handleLaunchActivity(ActivityThread.java:3409)
        at android.app.servertransaction.LaunchActivityItem.execute(LaunchActivityItem.java:83)
        at android.app.servertransaction.TransactionExecutor.executeCallbacks(TransactionExecutor.java:135)
        at android.app.servertransaction.TransactionExecutor.execute(TransactionExecutor.java:95)
        at android.app.ActivityThread$H.handleMessage(ActivityThread.java:2016)
        at android.os.Handler.dispatchMessage(Handler.java:107)
        at android.os.Looper.loop(Looper.java:214)
        at android.app.ActivityThread.main(ActivityThread.java:7356)
        at java.lang.reflect.Method.invoke(Native Method)
        at com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:492)
        at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:930)
     Caused by: java.lang.IllegalStateException: Already attached to lifecycleOwner
        at androidx.lifecycle.SavedStateHandleController.attachToLifecycle(SavedStateHandleController.java:43)
        at androidx.lifecycle.AbstractSavedStateViewModelFactory.create(AbstractSavedStateViewModelFactory.java:67)
        at androidx.lifecycle.ViewModelProvider.get(ViewModelProvider.java:177)
        at androidx.lifecycle.ViewModelProvider.get(ViewModelProvider.java:145)
        at androidx.lifecycle.ViewModelLazy.getValue(ViewModelProvider.kt:54)
        at androidx.lifecycle.ViewModelLazy.getValue(ViewModelProvider.kt:41)
        at com.geekorum.ttrss.BatteryFriendlyActivity.getNightViewModel(Unknown Source:7)
        at com.geekorum.ttrss.BatteryFriendlyActivity.onCreate(BatteryFriendlyActivity.kt:51)
        at com.geekorum.ttrss.InjectableBaseActivity.onCreate(CoreComponents.kt:70)
        at com.geekorum.ttrss.MainActivity.onCreate(MainActivity.kt:36)
        at android.app.Activity.performCreate(Activity.java:7802)
        at android.app.Activity.performCreate(Activity.java:7791)
        at android.app.Instrumentation.callActivityOnCreate(Instrumentation.java:1306)
        at android.app.ActivityThread.performLaunchActivity(ActivityThread.java:3245)
        at android.app.ActivityThread.handleLaunchActivity(ActivityThread.java:3409) 
        at android.app.servertransaction.LaunchActivityItem.execute(LaunchActivityItem.java:83) 
        at android.app.servertransaction.TransactionExecutor.executeCallbacks(TransactionExecutor.java:135) 
        at android.app.servertransaction.TransactionExecutor.execute(TransactionExecutor.java:95) 
        at android.app.ActivityThread$H.handleMessage(ActivityThread.java:2016) 
        at android.os.Handler.dispatchMessage(Handler.java:107) 
        at android.os.Looper.loop(Looper.java:214) 
        at android.app.ActivityThread.main(ActivityThread.java:7356) 
```

This is cause because the controller is attached  to lifecycle twice during the creation.
Once in `SavedStateHandleController .create(SavedStateRegistry registry, Lifecycle lifecycle,  key, Bundle defaultArgs)` when creating the controller. And a second time, in AbstractSavedStateViewModelFactory.create()` just after the controller is created:

``` AbstractSavedStateViewModelFactory.java

 public final <T extends ViewModel> T create(@NonNull String key, @NonNull Class<T> modelClass) {
        SavedStateHandleController controller = SavedStateHandleController.create(
                mSavedStateRegistry, mLifecycle, key, mDefaultArgs);
        controller.attachToLifecycle(mSavedStateRegistry, mLifecycle);
....
```
