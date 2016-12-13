![Quikkly SDK for Android BETA](https://github.com/quikkly/android-sdk/blob/master/banner.png?raw=true)

![Build Passing](https://img.shields.io/badge/build-passing-brightgreen.svg)
![Platforms Android](https://img.shields.io/badge/android-sdk%2017%2B-blue.svg)
![Gradle Compatible](https://img.shields.io/badge/gradle-compatible-green.svg)
[![Website](https://img.shields.io/badge/quikkly.io-developers-5cb8a7.svg)](https://developers.quikkly.io)

Quikkly is the easiest way to implement smart scannables within your app

- [Features](#features)
- [Requirements](#requirements)
- [Installation](#installation)
- [Usage](#usage)

## Features

- Built in detector for Quikkly Supported Scannables
- Support for generating Scannables using Quikkly's cloud platform
- Support for generating Scannables using your own platform
- Custom actions for your own app. Use your own API's, URIs et.c

## Requirements

- Android SDK level 17+

## Installation

In order to use this SDK, a Quikkly app key is required. Visit [here](https://developers.quikkly.io) for more information.

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
Then add the following 3 core components (Snapshot only available at this time) to start using Quikkly:

```gradle
dependencies {
    compile('net.quikkly.android:scanning-sdk:0.9.0-SNAPSHOT@aar') {
        transitive = true; // This is required
    }
    compile('net.quikkly.android:render-lib:0.9.0-SNAPSHOT@aar')
    compile('net.quikkly.android:scan-lib:0.9.0-SNAPSHOT@aar')
}
```

Additionally you may choose to use our ready packaged Scanning View, in which case simply add the folloing dependancy to your build.gradle.

```gradle
compile('net.quikkly.android:scanning-sdk-ui:0.9.0-SNAPSHOT@aar')
```

Some parts of the SDK require additional libraries that are either not available via online repositories or we cannot distribute directly to you. Below is the list of optional libraries that you must provide yourself.

YouTube Player API (v1.2.2) - If this library is not provided then the 'YouTube to Web action' (See Actions#WATCH_ON_YOUTUBE will be disabled.

### Scanning/Detection

#### Scanner with default UI

For a simple and hassle free integration a pre-packaged activity handling the detection of Quikkly based Scannables is provided.
It's as simple as this.

```java
public static final String INJECT_UI_KEY = "inject_ui";

private QuikklyUi mQuikklyUi = new QuikklyUi();

@Override
protected void onResume() {
    Intent intent = super.getIntent();
    if (intent.getBooleanExtra(ScanActivity.INJECT_UI_KEY, true)) {
        mQuikklyUi.onResume(this); // Important that this is executed before super.onResume()!
    }
    super.onResume();
}
```

The QuikklyUI also handles the action processing, however you can override the aciton handling with your own custom implemntation. Please see the [Custom Action](https://developers.quikkly.io/placeholder) example for more details and some sample code.

#### Scanner with Scan Fragment camera feed

For a more flexible implementation there is a ScanView class.
It will notify it's ScanResultListener object about detected scannables. The snippet below shows an example of the overidden method used to pass back the scannable, but please see the [Custom Scan View](https://developers.quikkly.io/placeholder) example for more details and some sample code.

```java
@Override
public @Nullable Symbol onScanResult(@Nullable ScanResult scanResult) {
    if (scanResult == null) {
        return null;
    } else {
        for (Symbol symbol : scanResult.getSymbols()) {
            if (symbol.isValid()) {
                // do something here, maybe display?
            }
        }
        return null;
    }
}
```

Note that this does not automatically handle the action as well. The detected Scannable object needs to be passed to an ActionHandler instance, or handled internally by your application. Please see the [Custom Scan Handling](https://developers.quikkly.io/placeholder) example for more details and some sample code.

### Generating Scannables

#### With actions via Quikkly back-end

To create a scannable on the Quikkly back-end, a few properties are needed, such as the type of action you wish to create, the data you want associated with the action and optionaly the display data in the form of a Skin.
For instance:

```java
Skin mySkin = new Skin();
```

The Skin object has properties which can be set - Border, Code, Background colour and also the URL of the image you wish to place in the centre. Please see the [Skin](http://docs.quikkly.io/android/0.9.0/render-lib/net/quikkly/android/render/Skin.html) object's Javadocs for more details.

Then it can be passed as a parameter of the generateActionTag method in the NewActionTagRequest interface. The skin can be nil as it will then use the default [Skin](http://docs.quikkly.io/android/0.9.0/render-lib/net/quikkly/android/render/Skin.html) provided by the Quikkly platform, but you are able to use the NewActionTagRequest().generateActionTag() method (example for Facebook shown below) to generate an action.

```java
Long actionID = 0L; // like on Facebook
String actionValue = "Quikkly"; // page_or_user_id
String scannableName = "Like Quikkly on Facebook";
Skin mySkin = new Skin();

// Now create the scannable. The context should be the Application Context and the Skin listener is any class which implements the SkinListener interface.
Symbol scannable = NewActionTagRequest().generateActionTag(context, actionID, actionValue, scannableName, mySkin, accessToken, skinListener);
```

#### Without Quikkly back-end

Scannables can be generated for use on your own back-end. Instantiating them requires an numeric value for the code and a Skin for visual representation with a [ScannableImageView](http://docs.quikkly.io/android/0.9.0/render-lib/net/quikkly/android/render/ScannableImageView.html)

### Displaying Scannables

Simply set the scannable property of a ScannableImageView instance.

```java
ScannableImageView.render(new Skin(someSkinJson), false, "http://some.default.image/url.png");
```
