# react-native-contact-picker

* Support to open contact sheet and select a user and get their emails back.
* Note: will not work on for Android on versions below React Native 40 due to a breaking change in React Native

## Getting started

`npm install doctadre/react-native-contact-picker`

### iOS
1. In XCode, in the project navigator, right click `your project` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-contact-picker` and add `ContactPicker.m`
3. In XCode, open the Info.plist file and add a `Privacy - Contacts Usage Description` key/value
4. Run your project (`Cmd+R`)

### Android

 if you do not have rnpm: `npm install rnpm -g`

 `rnpm link react-native-contact-picker`

 In AndroidManifest.xml

 ```xml
    <!--add contact picker permissions-->
    <uses-permission android:name="android.permission.READ_CONTACTS"/>
 ```


## Usage

Require the module `var ContactPicker = require('NativeModules').ContactPicker;`

```javascript
'use strict';

var React = require('react-native');
var ContactPicker = require('NativeModules').ContactPicker;
```

then invoke:

```javascript
    onButtonPress() {
        ContactPicker.pickContact().then((emails) => {
            if (emails && emails.length) {
                // list of contacts emails as strings
            } else {
             // either user hit cancel or the person they picked has no emails
            }
        });
    }
```

## License

MIT
