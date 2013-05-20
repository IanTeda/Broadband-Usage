package au.id.teda.broadband.dev.util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ViewFlipper;

/**
 * Works around Android Bug 6191 by catching IllegalArgumentException after
 * detached from the window.
 * 
 * @author Eric Burke (eric@squareup.com)
 */
public class SafeViewFlipper extends ViewFlipper {

	//private static final String INFO_TAG = SafeViewFlipper.class.getSimpleName();

	public SafeViewFlipper(Context context) {
		super(context);
	}

	public SafeViewFlipper(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * Workaround for Android Bug 6191:
	 * http://code.google.com/p/android/issues/detail?id=6191
	 * <p/>
	 * ViewFlipper occasionally throws an IllegalArgumentException after screen
	 * rotations.
	 */
	@Override
	protected void onDetachedFromWindow() {
		try {
			super.onDetachedFromWindow();
		} catch (IllegalArgumentException e) {
			//Log.d(INFO_TAG, "SafeViewFlipper ignoring IllegalArgumentException");

			// Call stopFlipping() in order to kick off updateRunning()
			stopFlipping();
		}
	}
}

/**
 * Typical stack trace from the Android bug:
 * 
 * Uncaught handler: thread main exiting due to uncaught exception
 * java.lang.IllegalArgumentException: Receiver not registered:
 * android.widget.ViewFlipper$1@447dc5d0 at
 * android.app.ActivityThread$PackageInfo
 * .forgetReceiverDispatcher(ActivityThread.java:667) at
 * android.app.ApplicationContext
 * .unregisterReceiver(ApplicationContext.java:747) at
 * android.content.ContextWrapper.unregisterReceiver(ContextWrapper.java:321) at
 * android.widget.ViewFlipper.onDetachedFromWindow(ViewFlipper.java:104) at
 * android.view.View.dispatchDetachedFromWindow(View.java:5835) at
 * android.view.ViewGroup.dispatchDetachedFromWindow(ViewGroup.java:1076) at
 * android.view.ViewGroup.dispatchDetachedFromWindow(ViewGroup.java:1074) at
 * android.view.ViewGroup.dispatchDetachedFromWindow(ViewGroup.java:1074) at
 * android.view.ViewGroup.dispatchDetachedFromWindow(ViewGroup.java:1074) at
 * android.view.ViewGroup.dispatchDetachedFromWindow(ViewGroup.java:1074) at
 * android.view.ViewGroup.dispatchDetachedFromWindow(ViewGroup.java:1074) at
 * android.view.ViewGroup.dispatchDetachedFromWindow(ViewGroup.java:1074) at
 * android.view.ViewGroup.dispatchDetachedFromWindow(ViewGroup.java:1074) at
 * android.view.ViewRoot.dispatchDetachedFromWindow(ViewRoot.java:1570) at
 * android.view.ViewRoot.doDie(ViewRoot.java:2556) at
 * android.view.ViewRoot.die(ViewRoot.java:2526) at
 * android.view.WindowManagerImpl
 * .removeViewImmediate(WindowManagerImpl.java:218) at
 * android.view.Window$LocalWindowManager.removeViewImmediate(Window.java:436)
 * at android.app.ActivityThread.handleDestroyActivity(ActivityThread.java:3498)
 * at
 * android.app.ActivityThread.handleRelaunchActivity(ActivityThread.java:3599)
 * at android.app.ActivityThread.access$2300(ActivityThread.java:119) at
 * android.app.ActivityThread$H.handleMessage(ActivityThread.java:1867) at
 * android.os.Handler.dispatchMessage(Handler.java:99) at
 * android.os.Looper.loop(Looper.java:123) at
 * android.app.ActivityThread.main(ActivityThread.java:4363) at
 * java.lang.reflect.Method.invokeNative(Native Method) at
 * java.lang.reflect.Method.invoke(Method.java:521) at
 * com.android.internal.os.ZygoteInit$MethodAndArgsCaller
 * .run(ZygoteInit.java:860) at
 * com.android.internal.os.ZygoteInit.main(ZygoteInit.java:618) at
 * dalvik.system.NativeStart.main(Native Method)
 **/
