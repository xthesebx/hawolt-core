package com.hawolt.impl.capsolver;

import com.hawolt.ICaptchaProvider;
import com.hawolt.ICaptchaType;
import com.hawolt.impl.anticaptcha.AntiCaptchaType;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created: 23/08/2023 09:11
 * Author: Twitter @hawolt
 **/

public class CapSolverBuilder {

    public static class HCaptchaTaskProxyless implements ICaptchaProvider {
        private final Builder builder;

        private HCaptchaTaskProxyless(Builder builder) {
            this.builder = builder;
        }

        @Override
        public Map<String, Object> get() {
            Map<String, Object> map = new HashMap<>();
            if (builder.object != null) map.put("enterprisePayload", builder.object);
            if (builder.userAgent != null) map.put("userAgent", builder.userAgent);
            if (builder.object != null) map.put("isEnterprise", true);
            map.put("isInvisible", builder.isInvisible);
            map.put("websiteURL", builder.websiteURL);
            map.put("websiteKey", builder.websiteKey);
            map.put("type", builder.type);
            return map;
        }

        public static class Builder {
            private String websiteURL, websiteKey, userAgent;
            private final ICaptchaType type;
            private boolean isInvisible;
            private JSONObject object;

            public Builder() {
                this.type = AntiCaptchaType.HCaptchaTaskProxyless;
            }

            public HCaptchaTaskProxyless build() {
                return new HCaptchaTaskProxyless(this);
            }

            public Builder setEnterpriseData(JSONObject object) {
                this.object = object;
                return this;
            }

            public Builder setUserAgent(String userAgent) {
                this.userAgent = userAgent;
                return this;
            }

            public Builder setWebsiteURL(String websiteURL) {
                this.websiteURL = websiteURL;
                return this;
            }

            public Builder setWebsiteKey(String websiteKey) {
                this.websiteKey = websiteKey;
                return this;
            }

            public Builder setInvisible(boolean invisible) {
                isInvisible = invisible;
                return this;
            }
        }
    }
}
