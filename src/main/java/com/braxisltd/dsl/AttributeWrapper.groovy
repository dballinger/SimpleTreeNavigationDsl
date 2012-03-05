package com.braxisltd.dsl

import com.braxisltd.domain.Attribute

class AttributeWrapper {

    private Attribute attribute

    AttributeWrapper(Attribute attribute) {
        this.attribute = attribute
    }

    static def nodePredicate(opts) {
        {NodeWrapper node ->
            def match = true;
            if (opts.name) {
                def attribute = node.node.attributes.find {it.name.equals(opts.name)}
                match = match && attribute
                if (opts.value) {
                    match = match && attribute.value.equals(opts.value)
                }
            }
            match
        }
    }

    def String name() {
        attribute.name;
    }

    def String val() {
        return attribute.value
    }

    def val(String value) {
        attribute.setValue(value)
    }

    def static Closure<AttributeWrapper> attributeWrapperTransformer() {
        {Attribute a -> new AttributeWrapper(a)}
    }

    def static Closure<Boolean> nameEquals(name) {
        {att -> att.name().equals(name)}
    }
}
