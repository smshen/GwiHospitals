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
 * HTTP key-value post.
 */
public class NetBody {
    private final HashMap<String, Object> body;

    public NetBody(Builder builder) {
        this.body = builder.body;
    }

    HashMap<String, Object> getBody() {
        return body;
    }

    public static class Builder {
        private String key;
        private String value;
        private HashMap<String, Object> body = new HashMap<String, Object>();

        public Builder add(String key, Object value) {
            body.put(key, value);
            return this;
        }

        public NetBody build() {
            return new NetBody(this);
        }
    }
}
