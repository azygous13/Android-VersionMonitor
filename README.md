# Android-VersionMonitor

[![Build Status](https://travis-ci.org/azygous13/Android-VersionMonitor.svg?branch=master)](https://travis-ci.org/azygous13/Android-VersionMonitor)

Checking new version available from Google Play Store

![screenshot 2016-05-08 09 43 03](https://cloud.githubusercontent.com/assets/3615979/15095713/2dc498b8-1501-11e6-84a7-f5e4bc00df63.png)

## Installation
To use this library in your android project, just simply add the following dependency into your build.gradle

```
dependencies {
    compile 'com.github.azygous13:version-monitor:0.1.0'
}
```

### Permission
Add this permission in your AndroidManifest.xml
```
<uses-permission android:name="android.permission.INTERNET"/>
```


## Usage
Just call VersionMonitor.init(this).monitor() in your launcher activity's onCreate() method.

## Support
VersionMonitor supports API level 8 and up.

## License

Licensed under the Apache License, Version 2.0 (the "License"); 
you may not use this file except in compliance with the License. 
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, 
software distributed under the License is distributed on an "AS IS" BASIS, 
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
See the License for the specific language governing permissions and limitations under the License.
