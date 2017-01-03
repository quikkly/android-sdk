![Quikkly SDK for Android BETA](https://github.com/quikkly/android-sdk/blob/master/banner.png?raw=true)

![Build Passing](https://img.shields.io/badge/build-passing-brightgreen.svg)
![Platforms Android](https://img.shields.io/badge/android-sdk%2017%2B-blue.svg)
![Gradle Compatible](https://img.shields.io/badge/gradle-compatible-green.svg)
[![Website](https://img.shields.io/badge/quikkly.io-developers-5cb8a7.svg)](https://developers.quikkly.io)

# Quikkly Sample Apps - Social Sharing (Quikkly Local)
One of the many use cases for Quikkly's Smart Scannables lies within the sharing of social media content / account information, similar to Snapchat's Snapcodes. This Android Studio project contains a simple demo app showing how similar funcitonality can be achieved using the Quikkly SDK.

The basic structure of the project is as follows:
* A simple contacts activity showing a dummy list of 'friends'.
* A simple 'Profile' Activity showing the 'Friend' details headed with a custom Scannable generated using the SDK locally. The scannable is generated using a custom skin and the Friend's user ID. This Activity is accessed by selecting one of the friends in the list OR by selecting the''Profile' button/icon in the top left of the navigations bar.
* A simple, vanilla Activity wrapping the integration of the Quikkly scanner. This activity demonstrates the instantiation, registration and handling of scan results.

Links to the various features:
- [Configuration](#configuration)
- [Scannables](#scannables)
- [Scanning](#scanning)

## Configuration
In order to use this Sample App, a Quikkly app key is required. Visit [here](https://developers.quikkly.io) for more information. You must also supply the API key within the manifest as detailed in the overall documentation.

### Gradle

Note that Quikkly's public repositories have been added to the build.gradle file:
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
The following 3 core components (Snapshot only available at this time) have been added.

```gradle
dependencies {
    compile('net.quikkly.android:scanning-sdk:0.9.0-SNAPSHOT@aar') {
        transitive = true;
    }
    compile('net.quikkly.android:render-lib:0.9.0-SNAPSHOT@aar')
    compile('net.quikkly.android:scan-lib:0.9.0-SNAPSHOT@aar')
    compile('net.quikkly.android:scanning-sdk-ui:0.9.0-SNAPSHOT@aar')
}
```
At this time Quikkly's SDK is reliant on OkHttp and Caverock's SVG libraries. The following dependancies should be added to the build.gradle file. 

```
compile 'com.caverock:androidsvg:1.2.1'
compile 'com.squareup.okhttp:okhttp:2.4.0'
```

## Scannables

In order to generate a scannable Quikkly's SDK requires 2 things: A numeric value which can be squashed down to 8 digit Hex and a Skin object for the visual representation. ProfileActivity.java contains a snippet of code to display a custom skin.

First we instantiate an instance of the [Scanner](http://www.scanner.com) class and generate a Code pattern which will be applied to our scannable.
```
// instantiate the scanner, set the layout and generate toe code pattern.
Scanner mScanner = new Scanner();
Scanner.CodeLayout codeLayout = Scanner.CodeLayout.Horizontal;
String codePattern = mScanner.generateV3CodePattern(codeLayout, userNum);       
```

Then we need to instantiate a [Skin](http://docs.quikkly.io/android/0.9.0/render-lib/net/quikkly/android/render/Skin.html) object
```
// Instantiate a skin object and SVG viewbox
Skin.ViewBox viewBox = new Skin.ViewBox();
viewBox.setWidth(358.0D);
viewBox.setHeight(358.0D);
viewBox.setX(80.0D);
viewBox.setY(65.0D);

mSkin = new Skin();
mSkin.setCodePattern(codePattern);
mSkin.setLayout(codeLayout.ordinal() + 1);
mSkin.setVersion(3);
.
. // set other properties here.
.
mSkin.setLogoViewBox(viewBox);
mSkin.setLogoUri(user.getProfilePic());
```

Once we have the Skin and Code we can instantiate a [ScannableImageView](http://docs.quikkly.io/android/0.9.0/render-lib/net/quikkly/android/render/ScannableImageView.html) to display the Scannable.
```
// Grab the reference to the ScannableImageView
mScannableImageView = (ScannableImageView) super.findViewById(R.id.scannable_view);

// Grab the getViewTreeObserver and setup a handler for Layout changes for re-rendering
mScannableImageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
@Override
public void onGlobalLayout() {

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
        // noinspection deprecation
        mScannableImageView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
    }
    else {
        mScannableImageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }

    mScannableImageView.render(mSkin, true, R.drawable.default_user);
}
});
```

## Scanning

In order to be able to scan the Friend's profile Scannable we need to instantiate the scanner and handle the detection results. In this example we have our own ScanActivity which wraps Quikkly's ScanFragment.
```
// Create a scan fragment, add to the root activity.
mScanFragment = new ScanFragment();
mScanFragment.setScanResultListener(this);
mScanFragment.setCameraStateListener(this);

FragmentManager fragmentManager = super.getFragmentManager();
FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
fragmentTransaction.add(R.id.quikkly_scan_activity_root, mScanFragment);
fragmentTransaction.commit();
```

This is enough to initialise and start the scannning, but an extra step is needed to be able to handle the results. Note how SocialScannerActivity implements CameraStateListener and ScanResultListener. This allows the consuming activity to listen for camera errors / state changes and handle detected frames.
```
@Override
public @Nullable Symbol onScanResult(@Nullable ScanResult scanResult) {
if (scanResult == null) {
    return null;
}
else {
    for (Symbol symbol : scanResult.getSymbols()) {
        if (symbol.isValid()) {
            String scannableNumber = symbol.getData();
            if(scannableNumber.isEmpty() == false) {
                // Success - do something with the number
            } else {
                // Failure - show an error?
            }
        }
    }

    return null;
}
}
```

Please dig into and around the code, and also feel free to use this as a base project for your own solution.
