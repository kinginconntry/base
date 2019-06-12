package com.needto.common.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Administrator
 */
public class Cookie {
    private final String name;
    private final String value;
    private final String path;
    private final String domain;
    private final Date expiry;
    private final boolean isSecure;
    private final boolean isHttpOnly;

    public Cookie(String name, String value, String path, Date expiry) {
        this(name, value, null, path, expiry);
    }

    public Cookie(String name, String value, String domain, String path, Date expiry) {
        this(name, value, domain, path, expiry, false);
    }

    public Cookie(String name, String value, String domain, String path, Date expiry, boolean isSecure) {
        this(name, value, domain, path, expiry, isSecure, false);
    }

    public Cookie(String name, String value, String domain, String path, Date expiry, boolean isSecure, boolean isHttpOnly) {
        this.name = name;
        this.value = value;
        this.path = path != null && !"".equals(path) ? path : "/";
        this.domain = stripPort(domain);
        this.isSecure = isSecure;
        this.isHttpOnly = isHttpOnly;
        if (expiry != null) {
            this.expiry = new Date(expiry.getTime() / 1000L * 1000L);
        } else {
            this.expiry = null;
        }

    }

    public Cookie(String name, String value) {
        this(name, value, "/", (Date)null);
    }

    public Cookie(String name, String value, String path) {
        this(name, value, path, (Date)null);
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }

    public String getDomain() {
        return this.domain;
    }

    public String getPath() {
        return this.path;
    }

    public boolean isSecure() {
        return this.isSecure;
    }

    public boolean isHttpOnly() {
        return this.isHttpOnly;
    }

    public Date getExpiry() {
        return this.expiry;
    }

    private static String stripPort(String domain) {
        return domain == null ? null : domain.split(":")[0];
    }

    public void validate() {
        if (this.name != null && !"".equals(this.name) && this.value != null && this.path != null) {
            if (this.name.indexOf(59) != -1) {
                throw new IllegalArgumentException("SimulateCookie names cannot contain a ';': " + this.name);
            } else if (this.domain != null && this.domain.contains(":")) {
                throw new IllegalArgumentException("Domain should not contain a port: " + this.domain);
            }
        } else {
            throw new IllegalArgumentException("Required attributes are not set or any non-null attribute set to null");
        }
    }

    @Override
    public String toString() {
        return this.name + "=" + this.value + (this.expiry == null ? "" : "; expires=" + (new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss z")).format(this.expiry)) + ("".equals(this.path) ? "" : "; path=" + this.path) + (this.domain == null ? "" : "; domain=" + this.domain) + (this.isSecure ? ";secure;" : "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof Cookie)) {
            return false;
        } else {
            Cookie cookie = (Cookie)o;
            if (!this.name.equals(cookie.name)) {
                return false;
            } else {
                boolean var10000;
                label39: {
                    if (this.value != null) {
                        if (this.value.equals(cookie.value)) {
                            break label39;
                        }
                    } else if (cookie.value == null) {
                        break label39;
                    }

                    var10000 = false;
                    return var10000;
                }

                var10000 = true;
                return var10000;
            }
        }
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    public static class Builder {
        private final String name;
        private final String value;
        private String path;
        private String domain;
        private Date expiry;
        private boolean secure;
        private boolean httpOnly;

        public Builder(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public Cookie.Builder domain(String host) {
            this.domain = Cookie.stripPort(host);
            return this;
        }

        public Cookie.Builder path(String path) {
            this.path = path;
            return this;
        }

        public Cookie.Builder expiresOn(Date expiry) {
            this.expiry = expiry;
            return this;
        }

        public Cookie.Builder isSecure(boolean secure) {
            this.secure = secure;
            return this;
        }

        public Cookie.Builder isHttpOnly(boolean httpOnly) {
            this.httpOnly = httpOnly;
            return this;
        }

        public Cookie build() {
            return new Cookie(this.name, this.value, this.domain, this.path, this.expiry, this.secure, this.httpOnly);
        }
    }
}
