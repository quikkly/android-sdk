![Quikkly SDK for Android BETA](https://github.com/quikkly/android-sdk/blob/master/banner.png?raw=true)

![Build Passing](https://img.shields.io/badge/build-passing-brightgreen.svg)
![Latest Version](https://img.shields.io/badge/version-1.2.2-green.svg)
![Gradle Compatible](https://img.shields.io/badge/gradle-compatible-green.svg)
[![Website](https://img.shields.io/badge/quikkly.io-developers-5cb8a7.svg)](https://developers.quikkly.io)

Quikkly is the easiest way to implement smart scannables within your app.

- [Features](#features)
- [Requirements](#requirements)
- [Usage](#usage)
  - [Setup](#setup)
  - [Scanning](#scannable)
  - [Generating](#generating)

## Features

- Built in detector for Quikkly Supported Scannables
- Support for generating Scannables using Quikkly's Android SDK

## Requirements

- Android SDK level 28+

## Usage 

### Gradle

To faciliate dependency resolution of our SDK you must configure your repositories in your build.gradle fil:
```gradle
repositories {
    maven {
        url 'https://maven.pkg.github.com/quikkly/quikkly-android-sdk'
        credentials {
            username = githubUser
            password = githubPackagesToken
        }
    }
}
```
See [build.gradle](SampleApp/build.gradle) in the SampleApp for a working example. For more information on resolving components from
github packages we refer you to their [documentation](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-gradle-registry).

**N.B.**: Github Packages require authentication the ***githubUser*** and ***githubPackagesToken*** in the previous example are resolved from ~/.gradle/gradle.properties

```gradle
dependencies {
    :
    :
    implementation 'net.quikkly.core:quikklycore-lib:3.4.18'
    implementation 'net.quikkly.android:quikkly-lib:3.4.18'
}
```

The above libraries provide you with all you need to allow your app to detect smart codes, handle the detection result and generate in-app representations of a code (offline generation)


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

Scannables can be generated for use within your own app and also passed back to you if you wish to store them within your own back-end solution. Instantiating them requires an numeric value for the code and a Skin for visual representation. You will also need to know which 'Template' you wish to use. Template identifiers can be found within the default Blueprint or you custom Blueprint.

Scannables can be displayed by using the RenderTagView, a snippet is shown below.

We refer you to the SampleApp for a comprehensive example on how to generate codes.



