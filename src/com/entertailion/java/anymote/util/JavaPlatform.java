package com.entertailion.java.anymote.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Iterator;

public class JavaPlatform implements Platform {
	
	public JavaPlatform() {
		
	}

	/**
	 * Open a file for output
	 * @param name
	 * @param mode
	 * @return
	 * @throws FileNotFoundException
	 */
	public FileOutputStream openFileOutput(String name, int mode) throws FileNotFoundException { 
		// TODO support mode parameter
		return new FileOutputStream(name);
	}
	
	/**
	 * Open a file for input
	 * @param name
	 * @return
	 * @throws FileNotFoundException
	 */
	public FileInputStream openFileInput(String name) throws FileNotFoundException {
		return new FileInputStream(name);
	}
	
	/**
	 * Get the network broadcast address. Used to listen for multi-cast messages to discover Google TV devices
	 * @return
	 */
	public Inet4Address getBroadcastAddress() {
		Inet4Address selectedInetAddress = null;
		try {
        	Enumeration<NetworkInterface> list = NetworkInterface.getNetworkInterfaces();

            while(list.hasMoreElements()) {
                NetworkInterface iface = list.nextElement();
                if(iface == null) continue;

                if(!iface.isLoopback() && iface.isUp()) {
                    Iterator<InterfaceAddress> it = iface.getInterfaceAddresses().iterator();
                    while (it.hasNext()) {
                        InterfaceAddress interfaceAddress = it.next();
                        if(interfaceAddress == null) continue;
                        InetAddress address = interfaceAddress.getAddress();
                        if (address instanceof Inet4Address) {
                        	if (address.getHostAddress().toString().charAt(0)!='0') {
                        		InetAddress broadcast = interfaceAddress.getBroadcast();
                        		if (selectedInetAddress==null) {
                        			selectedInetAddress = (Inet4Address)broadcast;
                        		} else if (iface.getName().startsWith("wlan") || iface.getName().startsWith("en")) {  // prefer wlan interface
                        			selectedInetAddress = (Inet4Address)broadcast;
                        		}
                        	}
                        }
                    }
                }
            }
        } catch (Exception ex) {
        }

        return selectedInetAddress;
	}
	
	/**
     * Get the platform version code
     * @return versionCode
     */
	public int getVersionCode() {
		return 1;
	}
	
	/**
	 * Get platform strings
	 * @param id
	 * @return
	 */
	public String getString(int id) {
		switch (id) {
			case NAME:
				return "Java";
			case CERTIFICATE_NAME: 
				return "java";
			case UNIQUE_ID: 
				return "emulator";  // needs to be unique per app so that multiple Anymote clients can run on the same device
			case NETWORK_NAME: 
				return "wired";  // (Wifi would be SSID)
			default:
				return null;
		}
	}
}
