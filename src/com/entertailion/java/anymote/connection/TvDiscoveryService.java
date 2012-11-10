/*
 * Copyright (C) 2012 Google Inc.  All rights reserved.
 * Copyright (C) 2012 ENTERTAILION, LLC. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.entertailion.java.anymote.connection;

import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.List;

import com.entertailion.java.anymote.connection.BroadcastDiscoveryClient.BroadcastAdvertisement;
import com.entertailion.java.anymote.connection.BroadcastDiscoveryClient.DeviceDiscoveredListener;
import com.entertailion.java.anymote.util.Constants;
import com.entertailion.java.anymote.util.Log;
import com.entertailion.java.anymote.util.Message;
import com.entertailion.java.anymote.util.MessageThread;
import com.entertailion.java.anymote.util.Platform;

/**
 * Service which discovers Google TV devices on the local network.
 * see https://developers.google.com/tv/remote/docs/communication?hl=en
 */
public class TvDiscoveryService extends MessageThread { 

    /**
     * Tag for debug logging.
     */
    private static final String LOG_TAG = "TvDiscoveryService";

    /**
     * Anymote service name.
     */
    private static final String SERVICE_TCP = "_anymote._tcp";

    /**
     * The service that handles connection to the TV device and sends events to
     * it.
     */
    private final Platform context;

    /**
     * The Broadcast client that listens for L3 broadcasts for Anymote service
     * on the network.
     */
    private BroadcastDiscoveryClient broadcastClient;

    /**
     * The thread that handles network communications.
     */
    private Thread broadcastThread;

    /**
     * All discovered TVs are stored in this list.
     */
    private List<TvDevice> devices;
    
    private static TvDiscoveryService instance;

    /**
     * Constructor
     * 
     * @param coreService The service that handles connectivity to the TV
     *            device.
     */
    private TvDiscoveryService(Platform context) {
        this.context = context;
        devices = new ArrayList<TvDevice>();
    }
    
    public static synchronized TvDiscoveryService getInstance(Platform context) {
    	if (instance==null) {
    		instance = new TvDiscoveryService(context);
    		instance.start();
    	}
    	return instance;
    }

    /**
     * Enum that declares internal messages.
     */
    private enum RequestType {

        BROADCAST_TIMEOUT,
    }

    /**
     * Sends message to the handler.
     * 
     * @param type
     */
    private void sendMessage(RequestType type) {
        sendMessage(type, null, 0);
    }

    /**
     * Lock object to synchronize threads.
     */
    Object broadCastSync = new Object();

    /**
     * Send messages to the handler with a delay.
     * 
     * @param type
     * @param obj
     * @param timeout
     */
    private void sendMessage(RequestType type, Object obj, long timeout) {
        Message message = obtainMessage(type.ordinal(), obj);
        super.sendMessageDelayed(message, timeout);
    }

    /**
     * The looper for thread that discovers Google TV devices offering Anymote
     * service on the local network.
     */
    DiscoveryLooper looper = new DiscoveryLooper();

    /**
     * Returns a list of Google TV devices offering Anymote service on the local
     * network.
     * 
     * @return list of TV devices
     */
    public List<TvDevice> discoverTvs() {
    	startBroadcast();
        if (getBroadcastAddress() == null) {
            devices = null;
            return devices;
        }
        try {
            synchronized (broadCastSync) {
                broadCastSync.wait();
            }

        } catch (InterruptedException e) {
            Log.e(LOG_TAG, "Interrupted while scanning for tvs");
            // Return empty array list as this.devices might be unsafe
            // because of background looper, which may still be writing to it.
            return new ArrayList<TvDevice>();
        }
        return devices;
    }

    /**
     * Called when network scan for discovering Google TV devices is completed.
     */
    public void onDeviceScanComplete() {
        stopBroadcast();
    }

    /**
     * Called when a Google TV device is found on local network.
     * 
     * @param dev
     */
    public void onDeviceFound(TvDevice dev) {
    	for(TvDevice device:devices) {
    		if (device.getName().equals(dev.getName())) {
    			return; // ignore duplicates
    		}
    	}
        devices.add(dev);
    }

    /**
     * Stops looking for Google TV devices on the network.
     */
    private synchronized void stopBroadcast() {
        if (broadcastClient != null) {
            Log.i(LOG_TAG, "Disabling broadcast");
            broadcastClient.stop();
            broadcastClient = null;
            try {
                broadcastThread.join(1000);
            } catch (InterruptedException e) {
                Log.i(LOG_TAG, "Timeout while waiting for thread execution to complete");
            }
            broadcastThread = null;
            onDeviceScanComplete();
        }
    }

    /**
     * Starts scanning the local network for Google TV devices.
     */
    private synchronized void startBroadcast() {
        Inet4Address broadcastAddress = getBroadcastAddress();
        if (broadcastAddress == null) {
            stopBroadcast();
            return;
        }
        if (broadcastClient == null) {
            Log.i(LOG_TAG, "Enabling broadcast");
            broadcastClient = new BroadcastDiscoveryClient(broadcastAddress, getServiceName());
            broadcastClient.setDeviceDiscoveredListener(new DeviceDiscoveredListener() {
                public void onDeviceDiscovered(BroadcastAdvertisement advert) {
                    TvDevice remoteDevice = getDeviceFromAdvert(advert);
                    Log.i(LOG_TAG, "Found device: " + remoteDevice.getName());
                    onDeviceFound(remoteDevice);
                }
            });

            broadcastThread = new Thread(broadcastClient);
            broadcastThread.start();
            int broadcastTimeout = Constants.integer.broadcast_timeout;
            sendMessage(RequestType.BROADCAST_TIMEOUT, null, broadcastTimeout);
        }
    }

    /**
     * Internal thread that does the discovery
     */
    private class DiscoveryLooper extends Thread {

        @Override
        public void run() {
            startBroadcast();
            if (getBroadcastAddress() == null) {
                devices = null;
                return;
            }

        }
    }

    public void handleMessage(Message msg) {
        RequestType request = RequestType.values()[msg.what];

        if (request == RequestType.BROADCAST_TIMEOUT) {
            stopBroadcast();
            synchronized (broadCastSync) {
                broadCastSync.notifyAll();
            }
        }
    }

    /**
     * Extracts Device definition from network broadcast.
     * 
     * @param adv network broadcast
     * @return TV device instance
     */
    protected TvDevice getDeviceFromAdvert(BroadcastAdvertisement adv) {
        return new TvDevice(adv.getServiceName(), adv.getServiceAddress(), adv.getServicePort());
    }

    /**
     * Retuns network name.
     * 
     * @return network name.
     */
    protected String getNetworkName() {
    	return context.getString(Platform.NETWORK_NAME);
    }

    /**
     * Returns the IP address where network broadcasts are sent.
     * 
     * @return IP address for broadcasts.
     */
    protected Inet4Address getBroadcastAddress() {
        return context.getBroadcastAddress();
    }

    /**
     * Returns Anymote service name.
     * 
     * @return Anymote service name.
     */
    protected String getServiceName() {
        return SERVICE_TCP;
    }
}
