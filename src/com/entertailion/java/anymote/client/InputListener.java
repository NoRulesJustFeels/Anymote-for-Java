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
package com.entertailion.java.anymote.client;

import java.util.List;

import com.entertailion.java.anymote.connection.TvDevice;

/**
 * The Listener for user interactions
 */
public interface InputListener {
	
	/**
	 * Called before Google TV devices are discovered
	 */
	public void onDiscoveringDevices();
	
	/**
	 * Called when a device has to be selected from the list of discovered Google TV devices
	 * @param devices
	 * @param listener
	 */
	public void onSelectDevice(List<TvDevice> devices, DeviceSelectListener listener);
	
	/**
	 * Called when a PIN is required to pair with a Google TV device
	 * @param listener
	 */
	public void onPinRequired(PinListener listener);

}