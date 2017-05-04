![Quikkly SDK for Android BETA](https://github.com/quikkly/android-sdk/blob/master/banner.png?raw=true)

![Build Passing](https://img.shields.io/badge/build-passing-brightgreen.svg)
![Platforms Android](https://img.shields.io/badge/android-sdk%2017%2B-blue.svg)
![Gradle Compatible](https://img.shields.io/badge/gradle-compatible-green.svg)
[![Website](https://img.shields.io/badge/quikkly.io-developers-5cb8a7.svg)](https://developers.quikkly.io)

Quikkly is the easiest way to implement smart scannables within your app.

- [Features](#features)
- [Requirements](#requirements)
- [Installation](#installation)
- [Usage](#usage)
  - [Setup](#setup)
  - [Scanning](#scannable)
  - [Generating](#generating)
  - [Default Scanning](#default-scanning)

## Features

- Built in detector for Quikkly Supported Scannables
- Support for generating Scannables using Quikkly's cloud platform
- Support for generating Scannables using your own platform
- Custom actions for your own app. Use your own API's, URIs et.c

## Requirements

- Android SDK level 19+

## Installation

In order to use this SDK, a Quikkly app key is required. Visit [here](https://developers.quikkly.io/home/get_started/) for more information.

### Gradle

To install the SDK simply add our repositories to your build.gradle file:
```gradle
Release
repositories {
   maven { url 'http://developers.quikkly.io/nexus/repository/maven-releases/' }
}
```
```gradle
Snapshot
repositories {
   maven { url 'http://developers.quikkly.io/nexus/repository/maven-snapshots/' }
}
```

#### Quikkly's Scanner and Code Generator
The simplest integration of Quikkly requires just 2 core dependencies to provide in-app generation and detection:
```gradle
dependencies {
   compile 'net.quikkly.android:quikklycore-lib:1.0.1@aar'
   compile 'net.quikkly.android:quikkly-lib:1.0.1@aar'
}
```

The above libraries provide you with all you need to allow your app to detect smart codes, handle the detection result and generate in-app representations of a code (offline generation)

#### Quikkly's APIs
Alternatively / Additionally you may wish to use Quikkly's CLoud Services. TO support this you should add the following core components:

```gradle
dependencies {
   compile('net.quikkly.android:scan-lib:1.0.1@aar') {
       transitive = true;
   }

   compile('net.quikkly.android:scanning-sdk:1.0.1@aar') {
       transitive = true;
   }

   compile('net.quikkly.android:render-lib:1.0.1@aar') {
       transitive = true;
   }
}
```

The above libraries provide you with wrapper to be able to register Custom Actions, detect and handle the results from calls to our APIs as well as use stock, pre-packaged wrappers to initialise the Scanner. These set of libraries are a little legacy and if used require you to add the app ey to your manifest within the Application block:
```xml
<meta-data android:name="quikkly_api_key" android:value="YOUR_API_KEY"/>
```

#### Additional optional libraries
Some parts of the SDK require additional libraries that are either not available via online repositories or we cannot distribute directly to you. Below is the list of optional libraries that you must provide yourself.

YouTube Player API (v1.2.2) - If this library is not provided then the 'YouTube to Web action' (See Actions#WATCH_ON_YOUTUBE will be disabled.

## Usage

### Setup

In order to use our SDK there are a few pre-requisite steps required when setting up your project.

1. Create a 'Blueprint' for your scannable on the [Quikkly Developer Portal](https://developers.quikkly.io/home/create-scannable/). The blueprint needs adding to your project's Assets.

2. Set the Quikkly API key in your main Application or the Main Activity of your app. The Value for the key will be your App key obtained from Quikkly ([here](https://developers.quikkly.io/my-quikkly/my-apps/)). The api key has to be valid, otherwise certain features of the SDK will not work.
```java
new QuikklyBuilder()
           .setApiKey("1GUVj1rMEgAutuphM39aPw6lzvXV6SpDqttlNsq981uIqNRX8LnDo6H334EgZIsjM7")
           .loadBlueprintFromAssets(this, "custom_blueprint.json")
           .build()
           .setAsDefault();
```

With these steps in place you are now ready to launch the basic integration of Quikkly's SDK.

#### Scanning

Create a new Activity and add the net.quikkly.android.ui package. This Activity needs to extend the ScanActivity class, from which you can override the onScanResult method. With this option you are also free to customise the overall UI to fit in within your own UX. For a fully packaged alternative approach, please see Default Scanning, below.

```java
public class MainActivity extends ScanActivity {

   @Override
   protected void onCreate(@Nullable Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       super.setContentView(R.layout.quikkly_scan_activity);
   }

   /**
    * Override in subclass to handle.
    *
    * Warning: will be called from background threads. Use handlers to post back to UI thread.
    * result.tags is an array of all found Scannable Codes within a frame. You can iterate through them, calling
    * getData() on each to obtain their encoded numeric value.
    */
   public void onScanResult(@Nullable ScanResult result) {
       Log.e(TAG, "ScanResult " + result.tags[0].getData());
   }
}
```

#### Generating

Scannables can be generated for use within your own app and also passed back to you if you wish to store them within your own back-end solution. Instantiating them requires an numeric value for the code and a Skin for visual representation. You will also need to know which 'Template' you wish to use. Template identifiers can be found within the Blueprint you have created on our [Developer Portal](https://developers.quikkly.io/home/create-scannable/).

Scannables can be displayed by using the RenderTagView, a snippet is shown below.

```java
String template = "template0001style1";  // The template you want to render, as per Blueprint.
BigInteger data = 123456789; // the number to encode.
Skin skin = new Skin();
skin.backgroundColor = #FFFFFF;
skin.borderColor = "#000000";
skin.maskColor = "#FFFFFF";
skin.overlayColor = "#FFFFFF";
skin.dataColor = "#000000";
skin.imageFit = 1;
skin.imageUrl = "http://server.com/image_ref.png";

renderView.setAll(template, data, skin);
```

## Default Scanning

### Default Scanner with Action Handling

For a simple and hassle free integration a pre-packaged activity which wraps up both Scanning, Detection and Action request/processing there is a ScanActivity which can be extended within the net.quikkly.android.scanning.scanner package.

```java
public class MainActivity extends ScanActivity {

    // Activity based code in here.

}
```

Then further down in your code override 'onResume()' method add the following to instantiate the wrapper QuikklyUI, which launches the scanner:

```java
public static final String INJECT_UI_KEY = "inject_ui";

private QuikklyUi quikklyUI = new QuikklyUi();

@Override
protected void onResume() {
   Intent intent = super.getIntent();
   if (intent.getBooleanExtra(ScanActivity.INJECT_UI_KEY, true)) {
       quikklyUI.onResume(this); // Important that this is executed before super.onResume()!
   }
   super.onResume();
}
```

You can optionally implement the QuikklyUI.Listener interface to override the handling and processing of Actions when the SDK completes the detection and requests the Actionable data from our services.
A great use case for this is overriding the action data that would come back from a 'Visit Website' based code. You can grab the url and process internally yourself.

```java
public class MainActivity extends ScanActivity implements QuikklyUi.Listener {

       @Override
       public void onActionResult(Action action, @NonNull Scannable<?> scannable, @NonNull ActionListener.ActionStatus status, final String message) {
           if(action != null) {
               int actionId = action.getActionId();
               if (actionId == Actions.VISIT_WEBSITE) {
                   String url = scannable.getActionData();
                   // Do something with the url
               }
           }
           super.onActionResult(action, scannable, status, message);
       }
}
```
