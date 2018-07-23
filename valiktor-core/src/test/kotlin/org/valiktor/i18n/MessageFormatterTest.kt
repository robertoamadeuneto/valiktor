package org.valiktor.i18n

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.valiktor.i18n.FormattersFixture.TestFormatter
import org.valiktor.i18n.FormattersFixture.TestObject
import org.valiktor.i18n.FormattersFixture.TestParentL
import org.valiktor.i18n.FormattersFixture.TestParentLFormatter
import org.valiktor.i18n.FormattersFixture.TestParentParentL
import org.valiktor.i18n.FormattersFixture.TestParentParentLFormatter
import org.valiktor.i18n.FormattersFixture.TestParentParentParent
import org.valiktor.i18n.FormattersFixture.TestParentParentParentFormatter
import org.valiktor.i18n.FormattersFixture.TestParentParentR
import org.valiktor.i18n.FormattersFixture.TestParentParentRFormatter
import org.valiktor.i18n.FormattersFixture.TestParentR
import org.valiktor.i18n.FormattersFixture.TestParentRFormatter
import org.valiktor.i18n.formatters.*
import java.util.*

private object FormattersFixture {

    interface TestParentParentParent
    interface TestParentParentL : TestParentParentParent
    interface TestParentParentR : TestParentParentParent
    interface TestParentL : TestParentParentL, TestParentParentR
    interface TestParentR : TestParentParentL, TestParentParentR

    object TestObject : TestParentL, TestParentR {
        override fun toString(): String = "TestObject"
    }

    object TestFormatter : Formatter<TestObject> {
        override fun format(value: TestObject, messageBundle: MessageBundle): String = value.toString()
    }

    object TestParentLFormatter : Formatter<TestParentL> {
        override fun format(value: TestParentL, messageBundle: MessageBundle): String = value.toString()
    }

    object TestParentRFormatter : Formatter<TestParentR> {
        override fun format(value: TestParentR, messageBundle: MessageBundle): String = value.toString()
    }

    object TestParentParentLFormatter : Formatter<TestParentParentL> {
        override fun format(value: TestParentParentL, messageBundle: MessageBundle): String = value.toString()
    }

    object TestParentParentRFormatter : Formatter<TestParentParentR> {
        override fun format(value: TestParentParentR, messageBundle: MessageBundle): String = value.toString()
    }

    object TestParentParentParentFormatter : Formatter<TestParentParentParent> {
        override fun format(value: TestParentParentParent, messageBundle: MessageBundle): String = value.toString()
    }
}

class FormattersTest {

    @BeforeEach
    fun `remove formatters`() {
        Formatters -= TestObject::class
        Formatters -= TestParentL::class
        Formatters -= TestParentR::class
        Formatters -= TestParentParentL::class
        Formatters -= TestParentParentR::class
        Formatters -= TestParentParentParent::class
    }

    @Test
    fun `should get default formatters`() {
        assertAll(
                { assertEquals(Formatters[Any::class], AnyFormatter) },
                { assertEquals(Formatters[Number::class], NumberFormatter) },
                { assertEquals(Formatters[Date::class], DateFormatter) },
                { assertEquals(Formatters[Calendar::class], CalendarFormatter) },
                { assertEquals(Formatters[Iterable::class], IterableFormatter) },
                { assertEquals(Formatters[Array<Any>::class], ArrayFormatter) }
        )
    }

    @Test
    fun `should get any formatter`() {
        assertEquals(Formatters[TestObject::class], AnyFormatter)
    }

    @Test
    fun `should get object formatter`() {
        Formatters[TestObject::class] = TestFormatter
        Formatters[TestParentL::class] = TestParentLFormatter
        Formatters[TestParentR::class] = TestParentRFormatter

        assertEquals(Formatters[TestObject::class], TestFormatter)
    }

    @Test
    fun `should get left parent formatter`() {
        Formatters[TestParentL::class] = TestParentLFormatter
        Formatters[TestParentR::class] = TestParentRFormatter

        assertEquals(Formatters[TestObject::class], TestParentLFormatter)
    }

    @Test
    fun `should get right parent formatter`() {
        Formatters[TestParentR::class] = TestParentRFormatter
        Formatters[TestParentParentL::class] = TestParentParentLFormatter

        assertEquals(Formatters[TestObject::class], TestParentRFormatter)
    }

    @Test
    fun `should get left parent parent formatter`() {
        Formatters[TestParentParentL::class] = TestParentParentLFormatter
        Formatters[TestParentParentR::class] = TestParentParentRFormatter

        assertEquals(Formatters[TestObject::class], TestParentParentLFormatter)
    }

    @Test
    fun `should get right parent parent formatter`() {
        Formatters[TestParentParentR::class] = TestParentParentRFormatter
        Formatters[TestParentParentParent::class] = TestParentParentParentFormatter

        assertEquals(Formatters[TestObject::class], TestParentParentRFormatter)
    }

    @Test
    fun `should get parent parent parent formatter`() {
        Formatters[TestParentParentParent::class] = TestParentParentParentFormatter

        assertEquals(Formatters[TestObject::class], TestParentParentParentFormatter)
    }
}