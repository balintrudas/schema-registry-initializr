package com.github.balintrudas.schema.initializr;

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
import com.github.balintrudas.schema.initializr.config.Config;
import com.github.balintrudas.schema.initializr.service.SchemaRegister;
import com.github.balintrudas.schema.initializr.service.SchemaRegisterApi;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class Application {

    public static void main(String[] args) throws IOException {
        log.info("Configuration: {}", Config.getState());
        if (Config.initializeOnStart()) {
            SchemaRegister schemaRegister = new SchemaRegister();
            schemaRegister.register();
        }
        if (Config.enableApi()) {
            new SchemaRegisterApi();
            log.info("Schema registry initializr started");
        } else {
            log.info("Schema registry initializr finished");
        }
    }

}
