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

## Useage

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

The QuikklyUI also handles the action processing.

#### Scanner with Scan Fragment camera feed - Local handling of scan result.

For a more flexible implementation there is a ScanFragment class. You can wrap up this ScanFragment within your own activity. The scanning and detection is handled for you but you will need to resume and pause the scanning yourself at the most approriate point within your app, i.e on detection of a valid tag you should pause scanning and when you have finished with the resulting object you should resume scanning.

A more detailed example of how you can use the ScanFragment can be seen within our [Local Social Sharing](https://github.com/quikkly/android-sdk/tree/master/LocalSocialSharing) sample app.

#### Generation Without Quikkly back-end

Scannables can be generated for use within your own app and also passed back to you if you wish to store them within your own back-end solution. Instantiating them requires an numeric value for the code and a Skin for visual representation within a [ScannableImageView](http://docs.quikkly.io/android/0.9.0/render-lib/net/quikkly/android/render/ScannableImageView.html).

Again, our [Local Social Sharing](https://github.com/quikkly/android-sdk/tree/master/LocalSocialSharing) sample app details how you can achieve this.

### Displaying Scannables

Simply set the scannable property of a ScannableImageView instance.

```java
ScannableImageView.render(new Skin(someSkinJson), false, "http://some.default.image/url.png");
```
Again, our [Local Social Sharing](https://github.com/quikkly/android-sdk/tree/master/LocalSocialSharing) sample app details how you can achieve this.

