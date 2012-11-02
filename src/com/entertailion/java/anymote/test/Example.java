/*
 * Copyright (C) 2012 ENTERTAILION, LLC. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.entertailion.java.anymote.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import com.entertailion.java.anymote.client.AnymoteClientService;
import com.entertailion.java.anymote.client.AnymoteSender;
import com.entertailion.java.anymote.client.ClientListener;
import com.entertailion.java.anymote.client.DeviceSelectListener;
import com.entertailion.java.anymote.client.InputListener;
import com.entertailion.java.anymote.client.PinListener;
import com.entertailion.java.anymote.connection.TvDevice;
import com.entertailion.java.anymote.util.Constants;
import com.entertailion.java.anymote.util.JavaPlatform;
import com.google.anymote.Key.Code;

/**
 * Example Anymote client with a command-line interface
 * See https://github.com/entertailion/Anymote-for-Java
 * 
 * @author leon_nicholls
 *
 */
public class Example implements ClientListener, InputListener {
	private AnymoteClientService anymoteClientService;
	private AnymoteSender anymoteSender;
	
	/**
	 * Start the Anymote client
	 * @param args
	 */
	public static void main(String[] args) {
		Example example = new Example();
	}
	
	/**
	 * Connect to the Anymote service and start the device selection
	 */
	public Example() {
		anymoteClientService = AnymoteClientService.getInstance(new JavaPlatform());
		anymoteClientService.attachClientListener(this);  // client service callback
		anymoteClientService.attachInputListener(this);  // user interaction callback
		
		// Find Google TV devices to connect to
		anymoteClientService.selectDevice();
		
		// OR connect to a specific device
//		try {
//            Inet4Address address = (Inet4Address) InetAddress.getByName("192.168.0.51");
//            anymoteClientService.connectDevice(new TvDevice(Constants.string.manual_ip_default_box_name, address));
//        } catch (UnknownHostException e) {
//        }
	}
	
	/** 
	 * ClientListener callback when Anymote is conneced to a Google TV device
	 * @see com.entertailion.java.anymote.client.ClientListener#onConnected(com.entertailion.java.anymote.client.AnymoteSender)
	 */
	public void onConnected(final AnymoteSender anymoteSender) {
	    if (anymoteSender != null) {
	        // Send events to Google TV using anymoteSender.
	        // save handle to the anymoteSender instance.
	        this.anymoteSender = anymoteSender;
	        
	        doCommands();
	    } else {
	    	System.out.println("Connection failed");
	    }
	}

	/**
	 * ClientListener callback when the Anymote service is disconnected from the Google TV device
	 * @see com.entertailion.java.anymote.client.ClientListener#onDisconnected()
	 */
	public void onDisconnected() {
		System.out.println("Disconnected");
	    anymoteSender = null;
	    
	    System.exit(1);
	}

	/**
	 * ClientListener callback when the connection to the Google TV device failed
	 * @see com.entertailion.java.anymote.client.ClientListener#onConnectionFailed()
	 */
	public void onConnectionFailed() {
		System.out.println("Connection failed");

	    anymoteSender = null;
	    
	    System.exit(1);
	}
	
	/**
	 * Cleanup
	 */
	private void destroy() {
        if (anymoteClientService != null) {
        	anymoteClientService.detachClientListener(this);
        	anymoteClientService.detachInputListener(this);
        	anymoteSender = null;
        }
    }
	
	/**
	 * Send Anymote commands to the Google TV device
	 */
	private void doCommands() {
		if (anymoteSender != null) {
			System.out.println("Next Home");
			// Need some time to settle
			sleep(5000);
			
			System.out.println("Home; Next Netflix");
			anymoteSender.sendKeyPress(Code.KEYCODE_HOME);
			sleep(10000);
			
			System.out.println("Netflix; Next Live TV");
			// android.content.Intent string format; see http://developer.android.com/reference/android/content/Intent.html
			anymoteSender.sendUrl("intent:#Intent;action=android.intent.action.MAIN;category=android.intent.category.LAUNCHER;launchFlags=0x10200000;component=com.google.tv.netflix/.NetflixActivity;end");
			sleep(15000);
			
			System.out.println("Live TV; Next URL");
			anymoteSender.sendKeyPress(Code.KEYCODE_LIVE);
			sleep(10000);
			
			System.out.println("URL; Next Live TV");
			anymoteSender.sendUrl("http://ableremote.com");
			sleep(20000);
			
			System.out.println("Live TV; Next Change Channel");
			anymoteSender.sendKeyPress(Code.KEYCODE_LIVE);
			sleep(10000);
			
			System.out.println("Change channel; Next Pause");
			anymoteSender.sendData("2");
			sleep(500);
			anymoteSender.sendData("5");
			sleep(500);
			anymoteSender.sendData("0");
			sleep(10000);
			
			System.out.println("Pause; Next Play");
			anymoteSender.sendKeyPress(Code.KEYCODE_PAUSE);
			sleep(10000);
			
			System.out.println("Play");
			anymoteSender.sendKeyPress(Code.KEYCODE_MEDIA_PLAY);
			
			destroy();
			System.exit(0);
		}
	}
	
	/**
	 * Utility method to make the thread sleep
	 * @param delay
	 */
	private void sleep(int delay) {
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
		}
	}
	
	/** 
	 * InputListener callback for feedback on starting the device discovery process
	 * @see com.entertailion.java.anymote.client.InputListener#onDiscoveringDevices()
	 */
	public void onDiscoveringDevices() {
		System.out.println("Finding devices...");
	}
	
	/** 
	 * InputListener callback when a Google TV device needs to be selected
	 * @see com.entertailion.java.anymote.client.InputListener#onSelectDevice(java.util.List, com.entertailion.java.anymote.client.DeviceSelectListener)
	 */
	public void onSelectDevice(List<TvDevice> trackedDevices, DeviceSelectListener listener) {
		// show a simple menu of options
		int counter = 1;
		for (TvDevice tv : trackedDevices) {
			System.out.println((counter++)+": "+tv.getName()+" - "+tv.getAddress().getHostAddress());
		}
		System.out.println((counter)+": Manual IP address"); // allow the user to enter a manual IP address

		System.out.print("Enter choice: ");
		BufferedReader br = new BufferedReader(new InputStreamReader(
				System.in));
		String choice = null;
		try {
			choice = br.readLine();
			int value = Integer.parseInt(choice);
			if (value==counter) { // manual IP
				System.out.print("Enter IP address: ");
				choice = br.readLine();
				
				String[] ipPort = choice.split(":");
		        int port = Constants.integer.manual_default_port;

		        if (ipPort.length == 2) {
		            try {
		                port = Integer.parseInt(ipPort[1]);
		            } catch (NumberFormatException e) {
		            	listener.onDeviceSelectCancelled();
		            }
		        } else if (ipPort.length != 1) {
		        	listener.onDeviceSelectCancelled();
		        }

		        try {
		            Inet4Address address = (Inet4Address) InetAddress.getByName(ipPort[0]);
		            listener.onDeviceSelected(new TvDevice(Constants.string.manual_ip_default_box_name, address, port));
		        } catch (UnknownHostException e) {
		        	listener.onDeviceSelectCancelled();
		        }
				
			} else {
				listener.onDeviceSelected(trackedDevices.get(value-1));
			}
		} catch (IOException ioe) {
			listener.onDeviceSelectCancelled();
		}
	}
	
	/**
	 * InputListener callback when PIN required to pair with Google TV device
	 * @see com.entertailion.java.anymote.client.InputListener#onPinRequired(com.entertailion.java.anymote.client.PinListener)
	 */
	public void onPinRequired(PinListener listener) {
        System.out.print("Enter PIN: ");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String pin = null;
        try {
           pin = br.readLine();
           listener.onSecretEntered(pin);
        } catch (IOException ioe) {
        	listener.onCancel();
        }
	}

}
