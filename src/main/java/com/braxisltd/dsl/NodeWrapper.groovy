package com.braxisltd.dsl

import com.braxisltd.domain.Node
import static com.braxisltd.dsl.AttributeWrapper.attributeWrapperTransformer
import static com.braxisltd.dsl.AttributeWrapper.nameEquals

class NodeWrapper {

    private Node node

    NodeWrapper(Node node) {
        this.node = node
    }

    def name() {
        node.name
    }

    @Deprecated
    def listC = {Closure... predicates = [{true}] ->
        def allChildren = []
        delegate.each {
            def filtered = it.node.children.collect(nodeWrapperTransformer())
            for (Closure predicate: predicates) {
                filtered = filtered.findAll(predicate)
            }
            allChildren += filtered
        }
        oneOrOther_dep(allChildren)
    }

    @Deprecated
    def c(Closure... finders = [{true}]) {
        def found = node.children.collect(nodeWrapperTransformer())
        for (Closure f: finders) {
            found = found.findAll(f)
        }
        oneOrOther_dep(found)
    }

    @Deprecated
    private oneOrOther_dep(Collection<NodeWrapper> found) {
        if (found.size() == 1) {
            found.toList().get(0)
        } else {
            found.metaClass.c = listC
            found.metaClass.c.delegate = found;
            found
        }
    }

    @Deprecated
    def a(Closure finder) {
        node.attributes.collect(attributeWrapperTransformer()).find(finder)
    }

    def static Closure<NodeWrapper> nodeWrapperTransformer() {
        {pi -> new NodeWrapper(pi)}
    }

    @Deprecated
    def static Closure<Boolean> hasAttribute(String name, String value) {
        {NodeWrapper n -> n.a(nameEquals(name)).val().equals(value)}
    }

    def having() {
        throw new DslException("Unable to call having() on a NodeWrapper. Must only be called on a collection.")
    }

    def child() {
        def children = node.children.collect(nodeWrapperTransformer())
        attachCollectionMethods(children)
        children
    }

    def attachCollectionMethods(List<NodeWrapper> nodeWrappers) {
        attachHavingToChildCollection(nodeWrappers)
        attachNameToChildCollection(nodeWrappers)
    }

    def attachHavingToChildCollection(List<NodeWrapper> nodeWrappers) {
        nodeWrappers.metaClass.having = {opts = [:], predicateDiscriminator ->
            def predicate
            if (predicateDiscriminator instanceof Closure) {
                predicate = predicateDiscriminator
            } else if ("attribute".equals(predicateDiscriminator)) {
                predicate = AttributeWrapper.nodePredicate(opts)
            } else {
                throw new DslException("Discriminator ${predicateDiscriminator} is not recognised.")
            }
            def filtered = delegate.findAll(predicate)
            oneOrMany(filtered)
        }
    }

    private oneOrMany(Collection filtered) {
        if (filtered.size() != 1) {
            attachCollectionMethods(filtered)
            filtered
        } else {
            filtered[0]
        }
    }

    def attachNameToChildCollection(List<NodeWrapper> nodeWrappers) {
        nodeWrappers.metaClass.name = {
            throw new DslException("Unable to call name() on a NodeWrapper collection")
        }
    }

    boolean equals(o) {
        if (o instanceof NodeWrapper) {
            return node.equals(o.node)
        }
        return false
    }

    int hashCode() {
        node.hashCode()
    }

    @Override
    public String toString() {
        return node.toString();
    }
}
