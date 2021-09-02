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

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringSubstitutor;

import java.util.HashSet;

/**
 * Store the xml configuration's parsed values
 */
@JacksonXmlRootElement
@Data
@NoArgsConstructor
@Slf4j
public class XmlConfig {

    @JacksonXmlProperty(localName = "userInfoConfig")
    private String userInfoConfig;

    @JacksonXmlProperty(localName = "subjects")
    private Subjects subjects = new Subjects();

    @JacksonXmlProperty(localName = "schemaRegistryUrls")
    private SchemaRegistryUrls schemaRegistryUrls = new SchemaRegistryUrls();

    @JacksonXmlProperty(localName = "schemaTypes")
    private SchemaTypes schemaTypes = new SchemaTypes();

    @JacksonXmlProperty(localName = "references")
    private References references = new References();

    public void setUserInfoConfig(String userInfoConfig) {
        this.userInfoConfig = StringSubstitutor.createInterpolator().replace(userInfoConfig);
    }

    public XmlConfig merge(XmlConfig xmlConfig) {
        HashSet<String> commonSubjects = new HashSet<>(subjects.getValues().keySet());
        commonSubjects.retainAll(xmlConfig.getSubjects().getValues().keySet());
        if (!commonSubjects.isEmpty()) {
            log.warn("There are duplicated subjects: {}", commonSubjects);
        }
        subjects.getValues().putAll(xmlConfig.getSubjects().getValues());
        schemaRegistryUrls.getParams().addAll(xmlConfig.getSchemaRegistryUrls().getParams());
        schemaTypes.getValues().putAll(xmlConfig.getSchemaTypes().getValues());
        references.getValues().putAll(xmlConfig.getReferences().getValues());
        return this;
    }
}
