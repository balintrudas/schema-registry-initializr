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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.text.StringSubstitutor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Reference {
    private String name;
    private String subject;
    private Integer version;

    @SneakyThrows
    public io.confluent.kafka.schemaregistry.maven.Reference getAsConfluentReference() {
        io.confluent.kafka.schemaregistry.maven.Reference reference = new io.confluent.kafka.schemaregistry.maven.Reference();
        FieldUtils.writeField(reference, "name", getName(), true);
        FieldUtils.writeField(reference, "subject", getSubject(), true);
        FieldUtils.writeField(reference, "version", getVersion(), true);
        return reference;
    }

    public void setName(String name) {
        this.name = StringSubstitutor.createInterpolator().replace(name);
    }

    public void setSubject(String subject) {
        this.subject = StringSubstitutor.createInterpolator().replace(subject);
    }

    @Override
    public String toString() {
        return "Reference{" +
                "name='" + name + '\'' +
                ", subject='" + subject + '\'' +
                ", version=" + version +
                '}';
    }
}
