package com.braxisltd.dsl

import com.braxisltd.domain.Attribute
import org.hamcrest.core.Is
import org.junit.Before
import org.junit.Test
import static org.junit.Assert.assertThat

class AttributeWrapperTest {

    private AttributeWrapper attributeWrapper;

    @Before
    def void setUp() throws Exception {
        Attribute attribute = new Attribute("name", "value");
        attributeWrapper = new AttributeWrapper(attribute);
    }

    @Test
    def void shouldRetrieveValue() throws Exception {
        assertThat(attributeWrapper.val(), Is.is("value"));
    }

    @Test
    def void shouldUpdateValue() throws Exception {
        attributeWrapper.val("updated");
        assertThat(attributeWrapper.val(), Is.is("updated"));
    }
}
