package com.adjust.gps {
import flash.events.EventDispatcher;
import flash.events.StatusEvent;
import flash.external.ExtensionContext;

public class AdjustGPS extends EventDispatcher {
    private static var errorMessage:String = "adjust: GPS not started. Start it manually using the 'start' method";

    private static var KEY_ADVERTISING_ID:String 			= "KEY_ADVERTISING_ID";
    private static var KEY_IS_AD_TRACKING_ENABLED:String 	= "KEY_IS_AD_TRACKING_ENABLED";

    private static var callbackHandler:IAdjustGPS;
    private static var extensionContext:ExtensionContext;

    public static function start(pCallbackHandler:IAdjustGPS):void {
        if (extensionContext) {
            trace("adjust GPS warning: GPS already started");
        }

        try {
            extensionContext = ExtensionContext.createExtensionContext("com.adjust.gps", null);
        } catch (exception) {
            trace(exception.toString());
            return;
        }

        if (!extensionContext) {
            trace("adjust error: cannot open ANE 'com.adjust.gps' for this platform");
            return;
        }

        callbackHandler = pCallbackHandler;
        extensionContext.addEventListener(StatusEvent.STATUS, adjustGPSResponseDelegate);

        extensionContext.call("initialize");
    }

    public static function getAdvertisingId():void {
        if (!extensionContext) {
            trace(errorMessage);
            return;
        }

        extensionContext.call("getAdvertisingId");
    }

    public static function isLimitAdTrackingEnabled():void {
        if (!extensionContext) {
            trace(errorMessage);
            return;
        }

        extensionContext.call("isLimitAdTrackingEnabled");
    }

    private static function adjustGPSResponseDelegate(statusEvent:StatusEvent):void {
        if (statusEvent.code != "adjust_gps_data") {
            return;
        }

        var response:String = statusEvent.level;
        var responsePart:Array = response.split("=");

        if (responsePart.length != 2) {
            trace("Malformed response");
            return;
        }

        var key:String = responsePart[0];
        var value:String = responsePart[1];

        if (key == KEY_ADVERTISING_ID) {
            callbackHandler.cbGetAdvertisingId(value);
        } else if (key == KEY_IS_AD_TRACKING_ENABLED) {
            callbackHandler.cbIsLimitAdTrackingEnabled(value == "true" ? true : false);
        } else {
            trace("Unknown key!");
        }
    }
}
}
