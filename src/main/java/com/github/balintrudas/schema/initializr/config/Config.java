package com.github.balintrudas.schema.initializr.config;

import com.github.balintrudas.schema.initializr.model.ConfigMergeStrategy;

/*-
 * The MIT License
 * Copyright © 2021 Balint Rudas
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
public class Config {
    /**
     * Path to the configuration file or directory.
     * Default: config.xml
     */
    public static final String CONFIG_PATH = "CONFIG_PATH";

    /**
     * Configuration files merging strategy
     * Default: SEPARATE
     */
    public static final String CONFIG_MERGE_STRATEGY = "CONFIG_MERGE_STRATEGY";

    /**
     * Enable or disable the automatic schema registry on the startup.
     * Default: true
     */
    public static final String INITIALIZE_ON_START = "INITIALIZE_ON_START";

    /**
     * The maximum time (second) to wait for schema registry url connection.
     * Default: 30
     */
    public static final String CONNECT_TIMEOUT = "CONNECT_TIMEOUT";

    /**
     * Embed server port.
     * Default: 8092
     */
    public static final String PORT = "PORT";

    /**
     * Enable or disable the embed api server
     * Default: true
     */
    public static final String ENABLE_API = "ENABLE_API";

    public static boolean initializeOnStart() {
        String onStartEnvValue = System.getenv(Config.INITIALIZE_ON_START);
        return onStartEnvValue == null || Boolean.parseBoolean(onStartEnvValue);
    }

    public static String getConfigPath() {
        String configPath = System.getenv(Config.CONFIG_PATH);
        return configPath == null ? "config.xml" : configPath;
    }

    public static ConfigMergeStrategy getConfigMergeStrategy() {
        String strategy = System.getenv(Config.CONFIG_MERGE_STRATEGY);
        return strategy == null ? ConfigMergeStrategy.SEPARATE : ConfigMergeStrategy.valueOf(strategy.toUpperCase());
    }

    public static Integer getConnectTimeout() {
        String configConnectionTimeout = System.getenv(Config.CONNECT_TIMEOUT);
        return configConnectionTimeout == null ? 30 : Integer.parseInt(configConnectionTimeout);
    }

    public static Integer getPort() {
        String configPort = System.getenv(Config.PORT);
        return configPort == null ? 8092 : Integer.parseInt(configPort);
    }

    public static boolean enableApi() {
        String enableApi = System.getenv(Config.ENABLE_API);
        return enableApi == null || Boolean.parseBoolean(enableApi);
    }

    public static String getState() {
        return "Config{" +
                "CONFIG_PATH='" + getConfigPath() + '\'' +
                ", CONFIG_MERGE_STRATEGY='" + getConfigMergeStrategy() + '\'' +
                ", INITIALIZE_ON_START=" + initializeOnStart() + '\'' +
                ", CONNECT_TIMEOUT=" + getConnectTimeout() + '\'' +
                ", PORT=" + getPort() + '\'' +
                ", ENABLE_API=" + enableApi() +
                '}';
    }
}
