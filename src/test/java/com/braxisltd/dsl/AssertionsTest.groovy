package com.braxisltd.dsl

import org.hamcrest.core.Is
import org.junit.Test
import static org.junit.Assert.assertThat

class AssertionsTest {

    ArrayList<String> list = ["a", "b", "c"]

    @Test
    def void shouldMakeNewMethodAvailableToCollection() {
        list.metaClass.doCoolStuff = {
            def rtn = ""
            delegate.each { el ->
                rtn += el
            }
            rtn
        }
        assertThat(list.doCoolStuff(), Is.is("abc"));
    }

    @Test
    def void shouldMakeNewMethodWithParamsAvailableToCollection() {
        list.metaClass.doCoolStuff = {suffix ->
            def rtn = ""
            delegate.each { el ->
                rtn += el
            }
            rtn + suffix
        }
        assertThat(list.doCoolStuff("d"), Is.is("abcd"));
    }
    
    @Test
    def void shouldUseOptionalClosureParam() {
        def clos = {param = "a" -> param}
        assertThat(clos(), Is.is("a"))
        assertThat(clos("b"), Is.is("b"))
    }
}
