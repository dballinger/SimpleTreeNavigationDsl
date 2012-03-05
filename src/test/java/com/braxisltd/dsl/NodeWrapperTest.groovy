package com.braxisltd.dsl

import com.braxisltd.domain.Attribute
import com.braxisltd.domain.Node
import org.hamcrest.core.Is
import org.junit.Before
import org.junit.Test
import static com.braxisltd.dsl.AttributeWrapper.nameEquals
import static com.braxisltd.dsl.NodeWrapper.hasAttribute
import static org.hamcrest.core.IsCollectionContaining.hasItems
import static org.junit.Assert.assertThat

class NodeWrapperTest {

    NodeWrapper nodeWrapper
    Node child1
    Node child2
    Node child3
    Node grandchild1
    Node grandchild2
    Node greatGrandchild1
    Node greatGrandchild2

    @Before
    def void before() {
        child1 = new Node("Child 1").addAttribute("att", "1")
        child2 = new Node("Child 2").addAttribute("att", "2")
        child3 = new Node("Child 3").addAttribute("att", "1")
        grandchild1 = new Node("Grandchild 1").addAttribute("att", "1")
        grandchild2 = new Node("Grandchild 2").addAttribute("att", "2")
        greatGrandchild1 = new Node("Great Grandchild 1").addAttribute("att", "1")
        greatGrandchild2 = new Node("Great Grandchild 2").addAttribute("att", "2")
        nodeWrapper = new NodeWrapper(
                new Node("Root")
                        .addAttribute("a", "1")
                        .addAttribute("b", "2")
                        .addAttribute("c", "3")
                        .addChild(child1)
                        .addChild(child2.addChild(grandchild1).addChild(grandchild2.addChild(greatGrandchild1).addChild(greatGrandchild2)))
                        .addChild(child3))
    }

    @Test
    def void shouldFindAttributeValue() {
        assertThat(nodeWrapper.a(nameEquals("a")).val(), Is.is("1"))
    }

    @Test
    def void shouldFindSingleChild() {
        assertThat(nodeWrapper.c(hasAttribute("att", "2")).name(), Is.is("Child 2"))
    }

    @Test
    def void shouldFindMultipleChildren() {
        def nodes = nodeWrapper.c(hasAttribute("att", "1"))
        assertThat(nodes, hasItems(
                new NodeWrapper(child1),
                new NodeWrapper(child3)))
    }

    @Test
    def void shouldFindGrandchildInMultipleChildren() {
        assertThat(nodeWrapper.c().c(hasAttribute("att", "1")).name(), Is.is("Grandchild 1"))
    }

    @Test
    def void shouldFindGreatGrandchildInMultipleBranches() {
        assertThat(nodeWrapper.c().c().c(hasAttribute("att", "2")).name(), Is.is("Great Grandchild 2"))
    }

    @Test
    def void shouldFindSingleChildWithTwoConditions() {
        assertThat(nodeWrapper.c(hasAttribute("att", "2"), {node -> node.name().equals("Child 2")}).name(), Is.is("Child 2"))
    }

    @Test
    def void shouldNotFindSingleChildWithTwoConditions() {
        assertThat(nodeWrapper.c(hasAttribute("att", "2"), {node -> node.name().equals("Child Not Present")}).size(), Is.is(0))
    }

    @Test(expected = DslException.class)
    def void shouldThrowIfNameCalledOnNodeWrapperCollection() {
        nodeWrapper.child().name();
    }

    @Test(expected = DslException.class)
    def void shouldThrowIfHavingCalledOnNodeWrapper() {
        nodeWrapper.having();
    }

    @Test
    def void shouldFindAllChildrenWithWith() {
        def predicate = {true}
        assertThat(nodeWrapper.child().having(predicate), hasItems(
                new NodeWrapper(child1),
                new NodeWrapper(child3)));
    }

    @Test
    def void shouldFilterChildrenWithWith() {
        def predicate = {
            nodeWrapper ->
            return nodeWrapper.node.attributes.find {
                Attribute attribute -> return attribute.name.equals("att")
            }.value.equals("1")
        }
        assertThat(nodeWrapper.child().having(predicate), hasItems(
                new NodeWrapper(child1),
                new NodeWrapper(child3)));
    }

    @Test(expected = DslException.class)
    def void shouldThrowWhenHavingDiscriminatorDoesntMatch() {
        nodeWrapper.child().having("somethingElse", name: "att", value: "2")
    }

    @Test
    def void shouldFindSingleChildUsingWithSyntax() {
        assertThat(nodeWrapper.child().having("attribute", name: "att", value: "2").name(), Is.is("Child 2"));
    }
}
