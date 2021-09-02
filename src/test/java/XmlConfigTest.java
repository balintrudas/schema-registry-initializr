
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
import com.github.balintrudas.schema.initializr.model.XmlConfig;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsNull.notNullValue;

public class XmlConfigTest {

    @Test
    public void shouldParseXmlConfig() throws IOException {
        File xml = new File("src/test/resources/config.xml");
        XmlMapper xmlMapper = new XmlMapper();
        XmlConfig xmlConfig = xmlMapper.readValue(xml, XmlConfig.class);
        assertThat(xmlConfig, notNullValue());

        assertThat(xmlConfig.getReferences(), notNullValue());
        assertThat(xmlConfig.getReferences().getValues(), notNullValue());
        assertThat(xmlConfig.getReferences().getValues(), hasKey("order"));
        assertThat(xmlConfig.getReferences().getValues().get("order"), hasSize(2));

        assertThat(xmlConfig.getUserInfoConfig(), notNullValue());
        assertThat(xmlConfig.getUserInfoConfig(), is("${schemaRegistryBasicAuthUserInfo}"));

        assertThat(xmlConfig.getSchemaRegistryUrls(), notNullValue());
        assertThat(xmlConfig.getSchemaRegistryUrls().getParams(), hasSize(1));
        assertThat(xmlConfig.getSchemaRegistryUrls().getParams().get(0), is("http://localhost:8081"));

        assertThat(xmlConfig.getSubjects(), notNullValue());
        assertThat(xmlConfig.getSubjects().getValues(), hasKey("process-status"));
        assertThat(xmlConfig.getSubjects().getValues().get("process-status").getPath(),
                is(new File("src/main/resources/avro/process-status.avsc").getPath()));

        assertThat(xmlConfig.getSchemaTypes(), notNullValue());
        assertThat(xmlConfig.getSchemaTypes().getValues(), hasKey("order"));
        assertThat(xmlConfig.getSchemaTypes().getValues(), hasKey("product"));
        assertThat(xmlConfig.getSchemaTypes().getValues(), hasKey("customer"));
    }

}
