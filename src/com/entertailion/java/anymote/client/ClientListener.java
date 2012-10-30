/*
 * Copyright (C) 2012 Google Inc. All rights reserved.
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

/**
 * All client applications should implement this listener. It provides
 * callbacks when the state of connection to the Anymote service running on
 * Google TV device changes.
 */
public interface ClientListener {
    /**
     * This callback method is called when connection to Anymote service has
     * been established.
     * 
     * @param anymoteSender The proxy to send Anymote messages.
     */
    public void onConnected(AnymoteSender anymoteSender);

    /**
     * This callback method is called when connection to Anymote service is
     * lost.
     */
    public void onDisconnected();

    /**
     * This callback method is called when there was a error in establishing
     * connection to the Anymote service.
     */
    public void onConnectionFailed();

}