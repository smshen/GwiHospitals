/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
package com.gwi.ccly.android.commonlibrary.common.net.connector;

import java.util.HashMap;

/**
 * HTTP HEADER.
 */
public final class NetHeader {
    private final HashMap<String, String> headers;

    public NetHeader(Builder builder) {
        this.headers = builder.headers;
    }

    HashMap<String, String> getHeaders() {
        return headers;
    }

    public static class Builder {
        private String key;
        private String value;
        private HashMap<String, String> headers = new HashMap<String, String>();

        public Builder add(String key, String value) {
            headers.put(key, value);
            return this;
        }

        public NetHeader build() {
            return new NetHeader(this);
        }
    }

}
