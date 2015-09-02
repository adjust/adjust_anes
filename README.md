## Summary

This is Google Play Services ANE Builder of adjust™. This tool is designed to generate Google Play Services ANE
file which can later be used by [adjust Adobe AIR SDK][adjust_adobe_air_sdk] as part of Flash mobile app or by
developers in general to get access to Google Play Services library functionality from ActionScript.

## Example app

There is an example app inside the [`SampleApp` directory][example]. You can open the `IntelliJ IDEA` project to see
an example of how couple of Google Play Services library functionalities are being used by Flash Mobile apps.

## Prerequisites

In order to be able to build Google Play Services ANE, you should have following tools:

* `Adobe AIR SDK` - You can download the latest version [here][adobe_air_sdk].
* `Java Development Kit` - For this project, JDK 1.6.0 was used.
* `Android SDK` - You can download it from Android Studio and Eclipse, or directly from [here][android_sdk_tools].

## Using the Google Play Services ANE Builder

#### 1. Setting environment variables in Makefile

Before you run [Makefile][makefile], please read comments you will find on top of it. It is very important that 
you set all relevant environment variables properly in order to be able to build `AdjustGPS.jar` and 
`AdjustGPS-1.0.0.ane`.

#### 2. Run Makefile

Everything you need to do is just to run Makefile and `AdjustGPS-1.0.0.ane` will be generated for you in root
of the cloned repository folder. After this, you can use this ANE file in your Flash Mobile app.

## Customize your Google Play Services ANE

#### 1. Intro

Purpose of this Google Play Services ANE Builder is to allow users to make their custom ANE versions depening on
their needs. Initial version of this repository creates ANE which contains just `Mobile Ads` part of Google Play
Services library because that's the only part needed by `adjust SDK`. More about Google Play Services library 
content can be read in [here in Table 1][google_play_services_lib_parts].

#### 2. Choose your own google-play-services.jar

You can choose **google-play-services.jar** of your own which you want to integrate in ANE file. It is located in
`<REPO_ROOT_FOLDER>/build/android` folder.

Original **google-play-services.jar** is part of your Android SDK folder. You can find it in `ANDROID_SDK_FOLDER/extras/google/google_play_services/libproject/google_play_services_lib/libs` folder.

![][google_play_services_jar]

This jar contains full version of Google Play Services library with all its containing parts. By integrating this
jar file in your ANE, you will have access to all features of the library and it will cause increasing in size of
your resulting ANE. You can choose (like we did) to use just subset of library functionalities. In order to make
this happen, you should "strip" your **google-play-services.jar** and remove unnecessary parts of it.

In order to do that, you can use script made by `dextorer` which can be found in [here][google_play_services_strip].
You should be careful and make sure that you have functional jar with selected subset of functionalities after jar
stripping. `strip.conf` file used for our stripped version which contains `Mobile Ads` functionality looks like this:

```
actions=false
ads=true
analytics=false
appindexing=false
appstate=false
auth=false
cast=false
common=true
drive=false
dynamic=false
games=false
gcm=false
identity=false
internal=true
location=false
maps=false
panorama=false
plus=false
security=false
tagmanager=false
wallet=false
wearable=false
```

#### 3. Copy Google Play Services resouces folder

As part of your Android SDK folder you will also find Google Play  Services library resources folder which should
also be copied to `<REPO_ROOT_FOLDER>/build/android` folder under name `google-play-services-res`. You can find 
this folder called `res` in 
`ANDROID_SDK_FOLDER/extras/google/google_play_services/libproject/google_play_services_lib` folder.

![][google_play_services_res]

This folder is important for later integration in app, because app's `AndroidManifest.xml` should define version
number  of Google Play Services library being used. This is done by adding following lines of code as part of
`<application>` tag body in `AndroidManifest.xml`:

```xml
<meta-data
  android:name="com.google.android.gms.version"
  android:value="@integer/google_play_services_version"/>
```

By having resources folder as part of ANE, `android:value` can be always set like this and not hard coded, because
resources folder contains file named `version.xml` with definition of this integer and it looks like this:

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <integer name="google_play_services_version">7571000</integer>
</resources>
```

So, with each Google Play Services library update, you don't need to make any changes to `AndroidManifest.xml` in
your app.

#### 4. ActionScript interface

We decided to make the bridge between Google Play Services and Flash Mobile apps by exposing some of the Google
Play Services library methods in ActionScript so that users actually can take advantage of library and use it
directly in their app's ActionScript classes.

Folder named `java` in root folder of this repository is the place where we keep all of our Java files. These
files are used to build `AdjustGPS.jar` which is actually [native library][adjustgps_jar_native_lib] used for ANE.
In oder to build it properly, you will need presence of `google-play-services.jar` and `FlashRuntimeExtensions.jar`.
This is being buit automatically with our Makefile, but like described in Makefile comments, we're actually keeping
these Java files as part of Eclipse project where we edit them with presence of needed jar files thus enabling 
syntax highlighting for easier development. Our project is basically `google-play-services_lib` project from 
Android SDK folder where we just added our Java classes.

![][gps_java_eclipse_project]

After making all changes in here, Makefile copies these files into `java` folder located in root directory of
repository folder before building `AdjustGPS.jar`.

At this moment, we have exposed only two methods from Google Play Services library (Mobile Ads part):

- [getId()][getid_api_ref]
- [isLimitAdTrackingEnabled()][islimitadtrackingenabled_api_ref]

If user wants to use these methods in app, it can be done (for example) like this:

```actionscript
import com.adjust.gps.AdjustGPS;
import com.adjust.gps.IAdjustGPS;

public class Main extends Sprite implements IAdjustGPS {
    public function Main() {
        AdjustGPS.start(this);
        
        AdjustGPS.isLimitAdTrackingEnabled();
        AdjustGPS.getAdvertisingId();
    }

    // Interface override

    public function cbGetAdvertisingId(advertisingId:String):void {
        trace(advertisingId);
    }

    public function cbIsLimitAdTrackingEnabled(isLimitAdTrackingEnabled:Boolean):void {
        var isEnabled:String = isLimitAdTrackingEnabled == true ? "YES" : "NO";
        
        trace(isEnabled);
    }
```

As you noticed in this example, for each call made, you will get response in corresponding callback. This is done
like this because execution of these methods (and probably many other from Google Play Services library) can't be
executed in main app thread. That's why calls to Google Play Services library methods are executed in background thread and answers are sent back to app via corresponding callbacks.

#### 5. Final thoughts

Current state of this repository is adjusted to fit needs of [adjust Adobe AIR SDK][adjust_adobe_air_sdk]. We tried
to make Google Play Services ANE generation process as customizable as possible so that everyone can make ANE which
fits their needs. Also, we wanted to give a starting point for people to see one way of how it's possible to use
Google Play Services library directly from ActionScript. We encourage you to use this repository and improve
ActionScript interface according to your needs and help us grow ActionScript API so that users can use as much of
Google Play Services library functionality as possible in their Flash Mobile apps.

[example]: https://github.com/adjust/gps_ane_builder/tree/development/SampleApp
[adobe_air_sdk]: http://www.adobe.com/devnet/air/air-sdk-download.html
[android_sdk_tools]: https://developer.android.com/tools/sdk/tools-notes.html
[makefile]: https://github.com/adjust/gps_ane_builder/blob/development/Makefile#L1
[google_play_services_lib_parts]: https://developers.google.com/android/guides/setup
[google_play_services_jar]: https://raw.github.com/adjust/adjust_sdk/master/Resources/air/google_play_services_jar.png
[google_play_services_res]: https://raw.github.com/adjust/adjust_sdk/master/Resources/air/google_play_services_res.png
[gps_java_eclipse_project]: https://raw.github.com/adjust/adjust_sdk/master/Resources/air/gps_java_eclipse_project.png
[google_play_services_strip]: https://gist.github.com/dextorer/a32cad7819b7f272239b
[adjustgps_jar_native_lib]: https://github.com/adjust/gps_ane_builder/blob/development/src/extension.xml#L7
[adjust_adobe_air_sdk]: https://github.com/adjust/adobe_air_sdk/
[getid_api_ref]: https://developers.google.com/android/reference/com/google/android/gms/ads/identifier/AdvertisingIdClient.Info.html#getId()
[islimitadtrackingenabled_api_ref]: https://developers.google.com/android/reference/com/google/android/gms/ads/identifier/AdvertisingIdClient.Info.html#isLimitAdTrackingEnabled()

## License

The adjust Google Play Services ANE Builder is licensed under the MIT License.

Copyright (c) 2012-2015 adjust GmbH, http://www.adjust.com

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the "Software"), to deal in
the Software without restriction, including without limitation the rights to
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
of the Software, and to permit persons to whom the Software is furnished to do
so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
