package com.malinskiy.marathon.report

import com.malinskiy.marathon.analytics.internal.sub.DeviceConnectedEvent
import com.malinskiy.marathon.analytics.internal.sub.ExecutionReport
import com.malinskiy.marathon.device.DevicePoolId
import com.malinskiy.marathon.execution.TestStatus
import com.malinskiy.marathon.report.ReportTestUtils.createTestEvent
import com.malinskiy.marathon.report.ReportTestUtils.getTestConfiguration
import com.malinskiy.marathon.report.ReportTestUtils.getTestDevice
import com.malinskiy.marathon.report.junit.JUnitReporter
import com.malinskiy.marathon.report.junit.JUnitWriter
import com.malinskiy.marathon.test.assert.shouldBeEqualToAsXML
import org.junit.jupiter.api.Test
import java.io.File
import java.time.Instant

class JUnitReporterTest {

    @Test
    fun `only Passed Tests and without retries should generate correct report`() {
        val device = getTestDevice()
        val report = ExecutionReport(
            deviceProviderPreparingEvent = emptyList(),
            devicePreparingEvents = emptyList(),
            deviceConnectedEvents = listOf(
                DeviceConnectedEvent(Instant.now(), DevicePoolId("myPool"), device)
            ),
            testEvents = listOf(
                createTestEvent(device, "test1", TestStatus.PASSED),
                createTestEvent(device, "test2", TestStatus.PASSED),
                createTestEvent(device, "test3", TestStatus.PASSED)
            )
        )
        val configuration = getTestConfiguration()
        println(configuration.outputDir)
        val jUnitWriter = JUnitWriter(configuration.outputDir)
        val junitReport = JUnitReporter(jUnitWriter)
        junitReport.generate(report)
        File(configuration.outputDir.absolutePath + "/tests/myPool/xxyyzz/marathon_junit_report.xml")
            .shouldBeEqualToAsXML(File(javaClass.getResource("/output/tests/myPool/xxyyzz/marathon_junit_report_passed_tests.xml").file))
    }

    @Test
    fun `only Failed Tests and without retries should generate correct report`() {
        val device = getTestDevice()
        val report = ExecutionReport(
            deviceProviderPreparingEvent = emptyList(),
            devicePreparingEvents = emptyList(),
            deviceConnectedEvents = listOf(
                DeviceConnectedEvent(Instant.now(), DevicePoolId("myPool"), device)
            ),
            testEvents = listOf(
                createTestEvent(device, "test1", TestStatus.FAILURE),
                createTestEvent(device, "test2", TestStatus.INCOMPLETE),
                createTestEvent(device, "test3", TestStatus.FAILURE)
            )
        )
        val configuration = getTestConfiguration()
        println(configuration.outputDir)
        val jUnitWriter = JUnitWriter(configuration.outputDir)
        val junitReport = JUnitReporter(jUnitWriter)
        junitReport.generate(report)
        File(configuration.outputDir.absolutePath + "/tests/myPool/xxyyzz/marathon_junit_report.xml")
            .shouldBeEqualToAsXML(File(javaClass.getResource("/output/tests/myPool/xxyyzz/marathon_junit_report_failed_tests.xml").file))
    }

    @Test
    fun `with FAILURE, IGNORED, INCOMPLETE and ASSUMPTION_FAILURE Tests with stack trace should generate correct report`() {
        val device = getTestDevice()
        val stackTrace = "Exception in thread \"main\" java.lang.RuntimeException: A test exception\n" +
            "  at com.example.stacktrace.StackTraceExample.methodB(StackTraceExample.java:13)\n" +
            "  at com.example.stacktrace.StackTraceExample.methodA(StackTraceExample.java:9)\n" +
            "  at com.example.stacktrace.StackTraceExample.main(StackTraceExample.java:5)"
        val report = ExecutionReport(
            deviceProviderPreparingEvent = emptyList(),
            devicePreparingEvents = emptyList(),
            deviceConnectedEvents = listOf(
                DeviceConnectedEvent(Instant.now(), DevicePoolId("myPool"), device)
            ),
            testEvents = listOf(
                createTestEvent(device, "test1", TestStatus.FAILURE, stackTrace),
                createTestEvent(device, "test2", TestStatus.IGNORED),
                createTestEvent(device, "test2", TestStatus.INCOMPLETE),
                createTestEvent(device, "test3", TestStatus.ASSUMPTION_FAILURE, stackTrace)
            )
        )
        val configuration = getTestConfiguration()
        println(configuration.outputDir)
        val jUnitWriter = JUnitWriter(configuration.outputDir)
        val junitReport = JUnitReporter(jUnitWriter)
        junitReport.generate(report)
        File(configuration.outputDir.absolutePath + "/tests/myPool/xxyyzz/marathon_junit_report.xml")
            .shouldBeEqualToAsXML(File(javaClass.getResource("/output/tests/myPool/xxyyzz/marathon_junit_report_failed_tests_with_stacktrace.xml").file))
    }

    @Test
    fun `with multiple execution of the same test from FAILURE to PASSED should generate correct report`() {
        val device = getTestDevice()
        val stackTrace = "Exception in thread \"main\" java.lang.RuntimeException: A test exception\n" +
            "  at com.example.stacktrace.StackTraceExample.methodB(StackTraceExample.java:13)\n" +
            "  at com.example.stacktrace.StackTraceExample.methodA(StackTraceExample.java:9)\n" +
            "  at com.example.stacktrace.StackTraceExample.main(StackTraceExample.java:5)"
        val report = ExecutionReport(
            deviceProviderPreparingEvent = emptyList(),
            devicePreparingEvents = emptyList(),
            deviceConnectedEvents = listOf(
                DeviceConnectedEvent(Instant.now(), DevicePoolId("myPool"), device)
            ),
            testEvents = listOf(
                createTestEvent(device, "test1", TestStatus.FAILURE, stackTrace, false),
                createTestEvent(device, "test1", TestStatus.PASSED, final = true)
            )
        )
        val configuration = getTestConfiguration()
        println(configuration.outputDir)
        val jUnitWriter = JUnitWriter(configuration.outputDir)
        val junitReport = JUnitReporter(jUnitWriter)
        junitReport.generate(report)
        File(configuration.outputDir.absolutePath + "/tests/myPool/xxyyzz/marathon_junit_report.xml")
            .shouldBeEqualToAsXML(File(javaClass.getResource("/output/tests/myPool/xxyyzz/marathon_junit_report_failed_to_passed_test.xml").file))
    }

    @Test
    fun `with multiple execution of the same test from PASSED to FAILURE should generate correct report`() {
        val device = getTestDevice()
        val stackTrace = "Exception in thread \"main\" java.lang.RuntimeException: A test exception\n" +
            "  at com.example.stacktrace.StackTraceExample.methodB(StackTraceExample.java:13)\n" +
            "  at com.example.stacktrace.StackTraceExample.methodA(StackTraceExample.java:9)\n" +
            "  at com.example.stacktrace.StackTraceExample.main(StackTraceExample.java:5)"
        val report = ExecutionReport(
            deviceProviderPreparingEvent = emptyList(),
            devicePreparingEvents = emptyList(),
            deviceConnectedEvents = listOf(
                DeviceConnectedEvent(Instant.now(), DevicePoolId("myPool"), device)
            ),
            testEvents = listOf(
                createTestEvent(device, "test1", TestStatus.PASSED, final = false),
                createTestEvent(device, "test1", TestStatus.FAILURE, stackTrace)
            )
        )
        val configuration = getTestConfiguration()
        println(configuration.outputDir)
        val jUnitWriter = JUnitWriter(configuration.outputDir)
        val junitReport = JUnitReporter(jUnitWriter)
        junitReport.generate(report)
        File(configuration.outputDir.absolutePath + "/tests/myPool/xxyyzz/marathon_junit_report.xml")
            .shouldBeEqualToAsXML(File(javaClass.getResource("/output/tests/myPool/xxyyzz/marathon_junit_report_passed_to_failed_test.xml").file))
    }
}
