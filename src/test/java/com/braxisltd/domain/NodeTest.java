package com.braxisltd.domain;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.junit.internal.matchers.IsCollectionContaining.hasItem;

public class NodeTest {

    private Node root;
    private Node childA;
    private Node childB;
    private Node grandchild;

    @Before
    public void before() {
        root = new Node("Root")
                .addAttribute("parent", "p value");
        childA = new Node("Child1")
                .addAttribute("ca1", "value 1")
                .addAttribute("ca2", "value 2");
        root.addChild(childA);
        childB = new Node("Child2")
                .addAttribute("cb1", "value 1")
                .addAttribute("cb2", "value 2")
                .addAttribute("cb3", "value 3");
        root.addChild(childB);
        grandchild = new Node("Grandchild").addAttribute("g", "value");
        childA.addChild(grandchild);
    }

    @Test
    public void shouldPrintToString() throws Exception {
        System.out.println(root);
    }

    @Test
    public void shouldRetrieveChildren() throws Exception {
        assertThat(root.getChildren(), hasItem(childA));
        assertThat(root.getChildren(), hasItem(childB));
    }

    @Test
    public void shouldRetrieveAttributes() throws Exception {
        assertThat(childA.getAttributes(), hasItem(new Attribute("ca1", "value 1")));
        assertThat(childA.getAttributes(), hasItem(new Attribute("ca2", "value 2")));
    }
}
