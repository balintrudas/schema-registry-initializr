package com.github.balintrudas.schema.initializr.service;

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

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.github.balintrudas.schema.initializr.config.Config;
import com.github.balintrudas.schema.initializr.model.RegisterSchemaRegistry;
import com.github.balintrudas.schema.initializr.model.XmlConfig;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class SchemaRegister {

    /**
     * Read config xml(s) and register schemas with confluent schema registry maven implementation
     */
    @SneakyThrows
    public void register() {
        List<XmlConfig> xmlConfigs = readXmlConfigs();
        if (xmlConfigs.size() > 0 && checkRegistryConnection(xmlConfigs.get(0).getSchemaRegistryUrls().getParams())) {
            xmlConfigs.forEach(xmlConfig -> {
                RegisterSchemaRegistry schemaReg = new RegisterSchemaRegistry(xmlConfig);
                try {
                    schemaReg.execute();
                } catch (MojoExecutionException | MojoFailureException e) {
                    log.error("Schema registration failed: ", e);
                } catch (IllegalStateException e) {
                    log.error("One or more exceptions were encountered but the registers can be successful nonetheless: ", e);
                }
            });
        }
    }

    /**
     * Read xml config(s)
     *
     * @return If config merge strategy is MERGE or the config path is not a directory returns a singleton list
     * otherwise all parsed configs.
     * @throws IOException exception
     */
    private List<XmlConfig> readXmlConfigs() throws IOException {
        File root = new File(Config.getConfigPath());
        if (root.isDirectory()) {
            File[] configs = root.listFiles((dir, name) -> name.endsWith(".xml"));
            List<XmlConfig> xmlConfigs = convertFileToXmlConfigs(configs);
            return applyConfigMergeStrategy(xmlConfigs);
        } else {
            return Collections.singletonList(new XmlMapper().readValue(root, XmlConfig.class));
        }
    }

    private List<XmlConfig> applyConfigMergeStrategy(List<XmlConfig> xmlConfigs) {
        switch (Config.getConfigMergeStrategy()) {
            case MERGE:
                return xmlConfigs.stream()
                        .reduce(XmlConfig::merge)
                        .stream()
                        .collect(Collectors.toList());
            case SEPARATE:
                return xmlConfigs;
            default:
                return Collections.emptyList();
        }
    }

    /**
     * Convert files to xml configs
     *
     * @param configFiles config xml files
     * @return Converted xml configs
     */
    private List<XmlConfig> convertFileToXmlConfigs(File[] configFiles) {
        if (configFiles != null) {
            XmlMapper xmlMapper = new XmlMapper();
            return Arrays.stream(configFiles)
                    .sorted(Comparator.comparing(File::getName))
                    .peek(c -> log.info("Config: {}", c.getAbsolutePath()))
                    .map(conf -> {
                        try {
                            return xmlMapper.readValue(conf, XmlConfig.class);
                        } catch (IOException e) {
                            log.error("Unable to process config:" + conf.getAbsolutePath(), e);
                        }
                        return null;
                    })
                    .filter(Objects::nonNull).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    /**
     * Check all url availability
     *
     * @param urls schema registry urls
     * @return return true if all schema registry reachable
     */
    private boolean checkRegistryConnection(List<String> urls) {
        return urls.stream().allMatch(this::checkRegistryConnection);
    }

    private boolean checkRegistryConnection(String url) {
        log.info("Wait for connection to: {} ({} sec)", url, Config.getConnectTimeout());
        try {
            waitForResponse(url, Config.getConnectTimeout() * 1000);
            log.info("Connection successfully established with: {}", url);
            return true;
        } catch (Exception e) {
            log.info("Connection could not be established with: {} after {} sec", url, Config.getConnectTimeout());
            log.error("Connection error: ", e);
            return false;
        }
    }

    private void waitForResponse(String url, int timeoutMS) throws Exception {
        long startTS = System.currentTimeMillis();
        Exception lastException = null;
        while (System.currentTimeMillis() - startTS < timeoutMS) {
            try {
                HttpURLConnection.setFollowRedirects(false);
                HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
                con.setRequestMethod("HEAD");
                con.getResponseCode();
                break;
            } catch (Exception e) {
                lastException = e;
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
        }
        if (lastException != null) {
            throw lastException;
        }
    }

}
