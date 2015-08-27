package {

import com.adjust.gps.AdjustGPS;
import com.adjust.gps.IAdjustGPS;

import flash.display.SimpleButton;
import flash.display.Sprite;
import flash.events.MouseEvent;
import flash.system.Capabilities;
import flash.text.TextField;
import flash.text.TextFieldAutoSize;

public class Main extends Sprite implements IAdjustGPS {
    private static var tfStatus:TextField;

    public function Main() {
        buildButton(-2, "Start Google Play Services", StartGPSClick);
        buildButton(-1, "Is Ad Tracking Enabled?", IsTrackingEnabledClick);
        buildButton(1, "My Ad Tracking ID?", MyAddTrackingIdClick);
        tfStatus = buildButton(2, "", null);
    }

    private function StartGPSClick(Event:MouseEvent):void {
        AdjustGPS.start(this);
        tfStatus.text = "GPS Enabled";
    }

    private function IsTrackingEnabledClick(Event:MouseEvent):void {
        AdjustGPS.isLimitAdTrackingEnabled();
    }

    private function MyAddTrackingIdClick(Event:MouseEvent):void {
        AdjustGPS.getAdvertisingId();
    }

    // Interface override

    public function cbGetAdvertisingId(advertisingId:String):void {
        tfStatus.text = advertisingId;
    }

    public function cbIsLimitAdTrackingEnabled(isLimitAdTrackingEnabled:Boolean):void {
        tfStatus.text = isLimitAdTrackingEnabled == true ? "YES" : "NO";
    }

    // GUI drawing

    private function buildButton(number:int, text:String, clickFunction:Function):TextField {
        var buttonHeight:int = 40;
        var yPosition: int = Capabilities.screenResolutionY * 0.25 +
                (number < 0 ? number * buttonHeight : (number - 1) * buttonHeight) + ((number != 1 && number != -1) ?
                (number > 0 ? 20 * Math.abs(number) : -20 * Math.abs(number)) : number * 10);

        var textField: TextField = new TextField();
        textField.text = text;
        textField.autoSize = TextFieldAutoSize.CENTER;
        textField.mouseEnabled = false;
        textField.x = (Capabilities.screenResolutionX - textField.width) * 0.5;
        textField.y = yPosition + 10;

        var buttonSprite: Sprite = new Sprite();

        if (number == 2) {
            buttonSprite.graphics.beginFill(0xECE0F8);
        } else {
            buttonSprite.graphics.beginFill(0x82F0FF);
        }

        buttonSprite.graphics.drawRect((Capabilities.screenResolutionX - 250) * 0.5, yPosition, 250, buttonHeight);
        buttonSprite.graphics.endFill();
        buttonSprite.addChild(textField);

        var simpleButton:SimpleButton = new SimpleButton();
        simpleButton.downState = buttonSprite;
        simpleButton.upState = buttonSprite;
        simpleButton.overState = buttonSprite;
        simpleButton.hitTestState = buttonSprite;

        if (clickFunction != null) {
            simpleButton.addEventListener(MouseEvent.CLICK, clickFunction);
        }

        addChild(simpleButton);

        return textField;
    }
}
}
