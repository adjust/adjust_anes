## Summary

This is Google Play Services ANE Builder of adjust™. This tool is designed to generate a Google Play Services ANE
file that can later be used by the [adjust Adobe AIR SDK][adjust_adobe_air_sdk] as part of a Flash mobile app, or by
developers in general to access Google Play Services library functionality from ActionScript.

## Example app

There is an example app located within the [`SampleApp` directory][example]. Open the `IntelliJ IDEA` project to see
an example of how some of the Google Play Services library functionalities are being used by Flash Mobile apps.

## Prerequisites

To build Google Play Services ANE, you will need following tools:

* `Adobe AIR SDK` - You can download the latest version [here][adobe_air_sdk].
* `Java Development Kit` - For this project, JDK 1.6.0 was used.
* `Android SDK` - You can download it from Android Studio and Eclipse, or directly from [here][android_sdk_tools].

## Using the Google Play Services ANE Builder

#### 1. Setting environment variables in Makefile

Before you run [Makefile][makefile], please read the top comments. All relevant environment variables must be properly set before you can build `AdjustGPS.jar` and `AdjustGPS-1.0.0.ane`.

#### 2. Run Makefile

Simply run Makefile and `AdjustGPS-1.0.0.ane` will be generated for you in the root of the cloned repository folder. You can then use this ANE file in your Flash Mobile app.

## Customize your Google Play Services ANE

#### 1. Intro

The purpose of the Google Play Services ANE Builder is to allow users to create a custom ANE tailored to
their needs. The initial version of this repository creates an ANE file containing only the `Mobile Ads` part of Google Play
Services library. This is the only part the 'adjust SDK' needs. You can read more about the Google Play Services library 
content in [Table 1][google_play_services_lib_parts].

#### 2. Choose your own google-play-services.jar

You can choose the **google-play-services.jar** you want to integrate with the ANE file. You can find it in the
`<REPO_ROOT_FOLDER>/build/android` folder.

Original **google-play-services.jar** is part of your Android SDK folder. You can find it in `ANDROID_SDK_FOLDER/extras/google/google_play_services/libproject/google_play_services_lib/libs` folder.

![][google_play_services_jar]

This jar contains a full version of the Google Play Services library. By integrating this
jar file in your ANE, you will have access to all the library's features and a much larger file. You can choose (like we did) to use just a subset of library functionalities. To do this, you should "strip" your **google-play-services.jar** and remove any unnecessary parts.

You can use script made by `dextorer`, which can be found [here][google_play_services_strip].
You should be careful and make sure you have a functional jar with a selected subset of functionalities after stripping your jar. The `strip.conf` file used for our stripped version containing the `Mobile Ads` functionality looks like this:

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

#### 3. Copy the Google Play Services resources folder

You will also find a Google Play Services library resources folder within your Android SDK folder. This should also be copied to the `<REPO_ROOT_FOLDER>/build/android` folder under the name `google-play-services-res`. You can find 
this folder (called `res`) in 
`ANDROID_SDK_FOLDER/extras/google/google_play_services/libproject/google_play_services_lib` folder.

![][google_play_services_res]

This folder is important for later app integration. This is because the app's `AndroidManifest.xml` should define the version
number of the Google Play Services library being used. To do this, add the following lines of code as part of the
`<application>` tag body in `AndroidManifest.xml`:

```xml
<meta-data
  android:name="com.google.android.gms.version"
  android:value="@integer/google_play_services_version"/>
```

Having a resources folder as part of the ANE means that the `android:value` can be set instead of hard coded. The resources folder contains a file named `version.xml`, which carries a definition of this integer and looks like this:

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <integer name="google_play_services_version">7571000</integer>
</resources>
```

Because of this, you won't need to make any changes to your app's `AndroidManifest.xml` with each Google Play Services library update.

#### 4. ActionScript interface

We decided to build a bridge between Google Play Services and Flash Mobile apps by exposing some of the Google
Play Services library methods in ActionScrip, enabling users to take advantage of the library by using it
directly in their app's ActionScript classes.

All of our Java files are stored in a folder named `java`, located in the root folder of this repository. These
files are used to build `AdjustGPS.jar`, which is actually the [native library][adjustgps_jar_native_lib] used for ANE.
In order to build it correctly, you will need `google-play-services.jar` and `FlashRuntimeExtensions.jar`.
This will be built automatically with our Makefile, but just as described in the Makefile comments, we will actually be keeping
these Java files as part of Eclipse project. We will edit them with the presence of jar files, which will enable syntax highlighting and allow for easier development. Our project is basically the `google-play-services_lib` project, but from the 
Android SDK folder where our Java classes were just added.

![][gps_java_eclipse_project]

After making all your changes, Makefile will copy these files into the `java` folder located in the root directory of the
repository folder before building `AdjustGPS.jar`.

At this moment, we have exposed only two methods from the Google Play Services library (Mobile Ads part):

- [getId()][getid_api_ref]
- [isLimitAdTrackingEnabled()][islimitadtrackingenabled_api_ref]

If a user wants to use these methods in app, it can be done (for example) like this:

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

As you can see from this example, you will receive a response in the corresponding callback for each call made. This is done because the execution of these methods (and probably many others from the Google Play Services library) can't be
executed in the main app thread. That's why calls to Google Play Services library methods are executed in a background thread, and answers are sent back to the app via corresponding callbacks.

#### 5. Final thoughts

This repository is designed to fit needs of the [adjust Adobe AIR SDK][adjust_adobe_air_sdk]. We tried
to make the Google Play Services ANE generation process as customizable as possible so that everyone can make an ANE that
fits their needs. Also, we wanted to give a starting point for people to see how you can use the Google Play Services library directly from ActionScript. We encourage you to use this repository, and to improve the
ActionScript interface according to your needs. You can help us grow the ActionScript API, enabling users to use as much of the
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
