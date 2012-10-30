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

// Android R-style constants

public final class Constants {
    public static final class integer {
        public static int broadcast_timeout=3000;
        public static int manual_default_port=9551;  // see https://developers.google.com/tv/remote/docs/communication?hl=en
    }
    public static final class string {
        public static String app_name="anymote";
        public static String manual_ip_default_box_name="GTV device";
    }
}
