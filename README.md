Anymote-for-Java
================

The Anymote-for-Java library implements the Google TV Pairing and Anymote protocols in pure Java.

Google open sourced the implementations of their software to allow mobile devices to communicate with Google TV devices. 
The Google TV Pairing Protocol is used to pair sessions between mobile devices and Google TV.
The Anymote Protocol is a messaging protocol that applications on a remote device use to communicate with Google TV. 
Anymote supports commands that are like those of a physical remote control.

If you want to write an app that communicates with Google TV devices you can either use the source code of the Google TV 
Remote app (https://code.google.com/p/google-tv-remote/) or you can use the Anymote Library 
(https://code.google.com/p/googletv-android-samples/source/browse/#git%2FAnymoteLibrary). Both of these are based on Android.

The Anymote-for-Java library has the following advantages:
1. Not dependent on Android. Any 1.6 JRE on any platform will work.
2. Paired devices are remembered. The Google Anymote library requires the pairing PIN to be entered for each session.
3. The user interface is externalized and replaceable. The Google Anymote library has the Android user interface embedded.

Anymote-for-Java is based on the Google Anymote Library code, but all Android dependencies have been replaced with pure Java logic. 
Platform-specific logic like creating files or getting the network configuration is isolated in the Platform class.


References:
Google TV Pairing Protocol: https://developers.google.com/tv/remote/docs/pairing
Anymote Protocol: https://code.google.com/p/anymote-protocol/
Building Second-screen Applications for Google TV: https://developers.google.com/tv/remote/docs/developing





