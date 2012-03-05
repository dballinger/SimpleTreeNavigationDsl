package com.braxisltd.domain;

import com.google.common.base.Joiner;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class Node {

    private final String name;
    private final List<Node> children = newArrayList();
    private final List<Attribute> attributes = newArrayList();

    public Node(String name) {
        this.name = name;
    }

    public Node addChild(Node child) {
        children.add(child);
        return this;
    }
    
    public Node addAttribute(String name, String value) {
        attributes.add(new Attribute(name, value));
        return this;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public List<Node> getChildren() {
        return children;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        appendDescription("", printWriter);
        return stringWriter.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node)) return false;

        Node node = (Node) o;

        if (attributes != null ? !attributes.equals(node.attributes) : node.attributes != null) return false;
        if (children != null ? !children.equals(node.children) : node.children != null) return false;
        if (name != null ? !name.equals(node.name) : node.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (children != null ? children.hashCode() : 0);
        result = 31 * result + (attributes != null ? attributes.hashCode() : 0);
        return result;
    }

    private void appendDescription(String prefix, PrintWriter printWriter) {
        printWriter.append(prefix).append("|- ").println(name);
        for (Attribute attribute : attributes) {
            attribute.appendDescription(prefix, printWriter);
        }
        for (Node child : children) {
            child.appendDescription(Joiner.on("").join(prefix, "| "), printWriter);
        }
    }
}
