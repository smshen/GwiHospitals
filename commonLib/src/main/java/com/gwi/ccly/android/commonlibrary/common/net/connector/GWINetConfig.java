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

import android.content.Context;

/**
 * 服务器参数配置(url)
 */
public class GWINetConfig {
    private final String baseUrl;
    private final Context context;

    GWINetConfig(Builder builder) {
        this.baseUrl = builder.baseUrl;
        this.context = builder.context;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public Context getContext() {
        return context;
    }

    public static class Builder {
        private String baseUrl;
        private Context context;

        public Builder setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder setContext(Context context) {
            this.context = context;
            return this;
        }

        public GWINetConfig build() {
            return new GWINetConfig(this);
        }
    }

}
