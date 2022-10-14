package com.hawolt.impl.anticaptcha;

import com.hawolt.ICaptchaProvider;
import com.hawolt.ICaptchaType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created: 16/08/2022 11:24
 * Author: Twitter @hawolt
 **/

public class AntiCaptchaBuilder {
    public static class RecaptchaV2TaskProxyless implements ICaptchaProvider {
        private final Builder builder;

        private RecaptchaV2TaskProxyless(Builder builder) {
            this.builder = builder;
        }

        @Override
        public Map<String, Object> get() {
            Map<String, Object> map = new HashMap<>();
            map.put("recaptchaDataSValue", builder.recaptchaDataSValue);
            map.put("isInvisible", builder.isInvisible);
            map.put("websiteURL", builder.websiteURL);
            map.put("websiteKey", builder.websiteKey);
            map.put("type", builder.type);
            return map;
        }

        public static class Builder {
            private final ICaptchaType type;
            private String websiteURL, websiteKey, recaptchaDataSValue;
            private boolean isInvisible;

            public Builder() {
                this.type = AntiCaptchaType.RecaptchaV2TaskProxyless;
            }

            public RecaptchaV2TaskProxyless build() {
                return new RecaptchaV2TaskProxyless(this);
            }

            public Builder setWebsiteURL(String websiteURL) {
                this.websiteURL = websiteURL;
                return this;
            }

            public Builder setWebsiteKey(String websiteKey) {
                this.websiteKey = websiteKey;
                return this;
            }

            public Builder setRecaptchaDataSValue(String recaptchaDataSValue) {
                this.recaptchaDataSValue = recaptchaDataSValue;
                return this;
            }

            public Builder setInvisible(boolean invisible) {
                isInvisible = invisible;
                return this;
            }
        }
    }

    public static class HCaptchaTaskProxyless implements ICaptchaProvider {
        private final Builder builder;

        private HCaptchaTaskProxyless(Builder builder) {
            this.builder = builder;
        }

        @Override
        public Map<String, Object> get() {
            Map<String, Object> map = new HashMap<>();
            map.put("userAgent", builder.userAgent);
            map.put("isInvisible", builder.isInvisible);
            map.put("websiteURL", builder.websiteURL);
            map.put("websiteKey", builder.websiteKey);
            map.put("type", builder.type);
            return map;
        }

        public static class Builder {
            private final ICaptchaType type;
            private String websiteURL, websiteKey, userAgent;
            private boolean isInvisible;

            public Builder() {
                this.type = AntiCaptchaType.HCaptchaTaskProxyless;
            }

            public HCaptchaTaskProxyless build() {
                return new HCaptchaTaskProxyless(this);
            }

            public Builder setWebsiteURL(String websiteURL) {
                this.websiteURL = websiteURL;
                return this;
            }

            public Builder setWebsiteKey(String websiteKey) {
                this.websiteKey = websiteKey;
                return this;
            }

            public Builder setUserAgent(String userAgent) {
                this.userAgent = userAgent;
                return this;
            }

            public Builder setInvisible(boolean invisible) {
                isInvisible = invisible;
                return this;
            }
        }
    }
}
