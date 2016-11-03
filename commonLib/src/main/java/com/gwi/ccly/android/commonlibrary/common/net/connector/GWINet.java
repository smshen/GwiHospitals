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

/**
 *GWI network单实例
 */
public class GWINet {
    static GWINet singleton = null;

    public synchronized static GWINet init(GWINetConfig config) {
        if (singleton == null) {
            singleton = new GWINet(config);
        }
        return singleton;
    }

    public synchronized static GWINet connect() {
        if (singleton == null) {
            throw new IllegalArgumentException("GWINet not instance yet.");
        }
        return singleton;
    }

    private final GWINetConfig config;

    public GWINet(GWINetConfig config) {
        this.config = config;
    }

    public void cancelRequest(String tag) {
        NetworkHelper.getInstance(config.getContext()).cancelPendingRequests(tag);
    }

    public NetworkCreator createRequest() {
        if (config == null) {
            throw new IllegalArgumentException("Config must not be null.");
        }

        return new NetworkCreator(config.getContext(), config.getBaseUrl());
    }
}
