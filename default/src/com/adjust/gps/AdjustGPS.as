package com.adjust.gps {
import flash.events.EventDispatcher;
import flash.events.StatusEvent;

public class AdjustGPS extends EventDispatcher {
    public static function start(pCallbackHandler:IAdjustGPS):void {
        trace("adjust GPS: start called");
    }

    public static function getAdvertisingId():void {
        trace("adjust GPS: getAdvertisingId called");
    }

    public static function isLimitAdTrackingEnabled():void {
        trace("adjust GPS: isLimitAdTrackingEnabled called");
    }
}
}
