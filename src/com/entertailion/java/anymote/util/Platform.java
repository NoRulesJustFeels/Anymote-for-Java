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
package com.entertailion.java.anymote.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.Inet4Address;

/**
 * Platform-specific capabilities
 */
public interface Platform {
	public static final int NAME = 0;
	public static final int CERTIFICATE_NAME = 1;
	public static final int UNIQUE_ID = 2;
	public static final int NETWORK_NAME = 3;
	public static final int MODE_PRIVATE = 0;

	/**
	 * Open a file for output
	 * @param name
	 * @param mode
	 * @return
	 * @throws FileNotFoundException
	 */
	public FileOutputStream openFileOutput(String name, int mode) throws FileNotFoundException;
	
	/**
	 * Open a file for input
	 * @param name
	 * @return
	 * @throws FileNotFoundException
	 */
	public FileInputStream openFileInput(String name) throws FileNotFoundException;
	
	/**
	 * Get the network broadcast address. Used to listen for multi-cast messages to discover Google TV devices
	 * @return
	 */
	public Inet4Address getBroadcastAddress();
	
	/**
     * Get the platform version code
     * @return versionCode
     */
	public int getVersionCode();
	
	/**
	 * Get platform strings
	 * @param id
	 * @return
	 */
	public String getString(int id) ;
}
