package com.github.balintrudas.schema.initializr.model;

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
import io.confluent.kafka.schemaregistry.maven.RegisterSchemaRegistryMojo;
import lombok.SneakyThrows;
import org.apache.commons.lang3.reflect.FieldUtils;

public class RegisterSchemaRegistry extends RegisterSchemaRegistryMojo {

    /**
     * Copy all necessary property into parent which are needed to start the schema registry process
     * @param config Parsed xml config
     */
    @SneakyThrows
    public RegisterSchemaRegistry(XmlConfig config) {
        FieldUtils.writeField(this, "schemaRegistryUrls", config.getSchemaRegistryUrls().getParams(), true);
        FieldUtils.writeField(this, "userInfoConfig", config.getUserInfoConfig(), true);
        FieldUtils.writeField(this, "subjects", config.getSubjects().getValues(), true);
        FieldUtils.writeField(this, "schemaTypes", config.getSchemaTypes().getValues(), true);
        FieldUtils.writeField(this, "references", config.getReferences().getValues(), true);
    }
}
