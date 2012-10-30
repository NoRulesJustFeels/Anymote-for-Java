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

import com.entertailion.java.anymote.connection.TvDevice;

/**
 * Device select listener
 */
public interface DeviceSelectListener {
    /**
     * Called whenever user picks up a new device to connect to.
     * 
     * @param device - interface to remote device
     */
    void onDeviceSelected(TvDevice device);

    /**
     * Called when device select dialog is dismissed
     */
    void onDeviceSelectCancelled();
}