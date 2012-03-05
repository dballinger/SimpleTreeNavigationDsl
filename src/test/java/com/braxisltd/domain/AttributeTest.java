package com.braxisltd.domain;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class AttributeTest {

    private Attribute attribute;

    @Before
    public void setUp() throws Exception {
        attribute = new Attribute("name", "value");
    }

    @Test
    public void shouldConstructProperly() throws Exception {
        assertThat(attribute.getName(), is("name"));
        assertThat(attribute.getValue(), is("value"));
    }

    @Test
    public void shouldUpdateValue() throws Exception {
        attribute.setValue("updated");
        assertThat(attribute.getValue(), is("updated"));
    }
}
