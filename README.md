**Run Unit Tests and Build APK File**

Create and startup an emulator for a Nexus 6P w/ API Level 23.  **(This must happen before running connectedAndroidTests).**
Please run the tests with the emulator in Portrait mode.  I have not written tests that account for landscape mode and therefore some views
may be scrolled off screen and fail the test.  In a production app, I would spend a lot more time cross testing across various device sizes
and orientations.

Tests aside, this app should work fairly well on different physical devices and orientations.

Once you have your emulator up and running, you should be able to run the following:

```
./gradlew clean test connectedAndroidTest build
```

**Expected Results**

* Unit Tests will be run.
* Instrumentation tests, including Esspresso UI Driver tests, will run.
* APK will be built and placed into:

```
./app/build/outputs/apk/app-debug.apk
```

**Screenshots**

<img width="300" height="605" src="Promotion%20List.png" alt="Promotion List Example"/> <img width="300" height="605" src="Promotion%20Detail.png" alt="Promotion Detail Example"/> <img width="300" height="605" src="Initial%20Offline.png" alt="Initial Offline Screen Example"/>

<img width="729" height="545" src="Nexus%209%20Landscape.png" alt="Nexus 9 Landscape Example"/>


**Other Projects/Work I want to note are**

**Projects**

**Android**
* Path of lowest cost finder - https://github.com/ericmaxwell2003/PolcAndroid/
* Realm/RoboGuice Espresso Test Sample - https://github.com/ericmaxwell2003/roboguiceRealmNaeNae/tree/feature/roboguiceIntegration/app/src/androidTest/java/software/credible/naenaelistapp
* Donor Discounts - https://play.google.com/store/apps/developer?id=Kustom+Software&hl=en
* Event Client - https://github.com/ericmaxwell2003/androidEventClientApp

**iOS**
* Reddit Client - https://github.com/ericmaxwell2003/redditClient
* Event Client - https://github.com/ericmaxwell2003/iosEventClientApp
* FliteDeck - Will talk about this one

**Talks**
* About Realm & RoboGuice on Android - http://www.slideshare.net/EricMaxwell1/better-data-persistence-on-android
* About Security & Sync Patterns in Mobile App - http://www.slideshare.net/EricMaxwell1/data-synchronization-patterns-in-mobile-application-design
