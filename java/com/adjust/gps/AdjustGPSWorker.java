package com.adjust.gps;

import java.lang.ref.WeakReference;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

public class AdjustGPSWorker extends HandlerThread {
	private static final String TAG = "AdjustGPSWorker";
	
	private Context context;
	private final InternalHandler internalHandler;
	
	private static final class InternalHandler extends Handler {
        private static final int GET_AD_TRACKING_ID = 1;
        private static final int IS_AD_TRACKING_ENABLED = 2;

        private final WeakReference<AdjustGPSWorker> adjustGPSWorkerReference;

        protected InternalHandler(Looper looper, AdjustGPSWorker adjustGPSWorker) {
            super(looper);
            this.adjustGPSWorkerReference = new WeakReference<AdjustGPSWorker>(adjustGPSWorker);
        }

        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);

            AdjustGPSWorker adjustGPSWorker = adjustGPSWorkerReference.get();
            
            if (adjustGPSWorker == null) {
                return;
            }

            switch (message.arg1) {
                case GET_AD_TRACKING_ID:
                    adjustGPSWorker.execGetAdvertisingId();
                    break;
                case IS_AD_TRACKING_ENABLED:
                    adjustGPSWorker.execIsLimitAdTrackingEnabled();
                    break;
                default:
                	break;
            }
        }
    }
	
	public AdjustGPSWorker(Context context) {
		super(TAG, MIN_PRIORITY);
		
		setDaemon(true);
		start();
		
		this.context = context;
		this.internalHandler = new InternalHandler(getLooper(), this);
	}
	
	// Adjust Google Play Services available methods
	
	public void getAdvertisingId() {
		Message message = Message.obtain();
        message.arg1 = InternalHandler.GET_AD_TRACKING_ID;
        internalHandler.sendMessage(message);
	}
	
	public void isLimitAdTrackingEnabled() {
		Message message = Message.obtain();
        message.arg1 = InternalHandler.IS_AD_TRACKING_ENABLED;
        internalHandler.sendMessage(message);
	}
	
	// Adjust Google Play Services available methods implementation
	
	private void execGetAdvertisingId() {
		String advertisingId = AdjustGPSAds.GetAdvertisingId(this.context);
		AdjustGPSFactory.getAdjustGPSCallbacksHandler().cbGetAdvertisingId(advertisingId);
	}
	
	private void execIsLimitAdTrackingEnabled() {
		boolean isLimitAdTrackingEnabled = AdjustGPSAds.IsLimitAdTrackingEnabled(this.context);
		AdjustGPSFactory.getAdjustGPSCallbacksHandler().cbIsLimitAdTrackingEnabled(isLimitAdTrackingEnabled);
	}
}
