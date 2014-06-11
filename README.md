Cordova Spectralink Plugin
===================

This is a Cordova/Phonegap plugin to interact with the Spectralink ruggedized devices' Barcode Scanners.  It has been tested on an 8753.

=============

This plugin is compatible with plugman.  To install, run the following from your project command line: <br/>
```$ cordova plugin add https://github.com/BlueFletch/spectralink-cordova.git```


==============

<h3>To Use:</h3>
You'll register a callback which will be called when a successful "scan" event occurs. 

NOTE: this plugin uses reflection to read the added Spectralink APIs to core Android at runtime.  This means you do NOT need to compile your project against a different version of Android.

```
function readBarcode(barcode) {
	console.log("barcode read : " + barcode);
	//TODO: handle barcode read
}
document.addEventListener("deviceready", function(){ 

	if (window.spectralinkBarcode) {
		console.log("initializing barcode scanner");

		spectralinkBarcode.register(readBarcode, function (argument) {
			console.log("failed to register barcode scanner");
		});
	}
}, false);
```

==============
Copyright 2014 BlueFletch Mobile

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
