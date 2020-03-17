package com.malinskiy.marathon.report.allure

import com.malinskiy.marathon.execution.TestStatus
import com.malinskiy.marathon.report.ReportTestUtils.createTestEvent
import com.malinskiy.marathon.report.ReportTestUtils.getTestConfiguration
import com.malinskiy.marathon.report.ReportTestUtils.getTestDevice
import org.junit.jupiter.api.Test
import java.util.*

class AllureReporterTest {
    @Test
    fun `allure reporter fullname should have package`() {
        val device = getTestDevice()
        val testEvent = createTestEvent(device, "foo", TestStatus.FAILURE)
        val configuration = getTestConfiguration()
        val allureReporter = AllureReporter(configuration, configuration.outputDir)
        val uuid = UUID.randomUUID().toString()
        val allureTestResult = allureReporter.createTestResult(uuid, device, testEvent.testResult)
    }

}
