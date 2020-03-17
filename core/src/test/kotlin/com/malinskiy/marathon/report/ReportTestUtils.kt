package com.malinskiy.marathon.report

import com.malinskiy.marathon.analytics.internal.sub.TestEvent
import com.malinskiy.marathon.device.DeviceFeature
import com.malinskiy.marathon.device.DeviceInfo
import com.malinskiy.marathon.device.DevicePoolId
import com.malinskiy.marathon.device.DeviceProvider
import com.malinskiy.marathon.device.NetworkState
import com.malinskiy.marathon.device.OperatingSystem
import com.malinskiy.marathon.execution.AnalyticsConfiguration
import com.malinskiy.marathon.execution.Configuration
import com.malinskiy.marathon.execution.TestParser
import com.malinskiy.marathon.execution.TestResult
import com.malinskiy.marathon.execution.TestStatus
import com.malinskiy.marathon.log.MarathonLogConfigurator
import com.malinskiy.marathon.test.Test
import com.malinskiy.marathon.vendor.VendorConfiguration
import java.nio.file.Files
import java.time.Instant

internal object ReportTestUtils{
    fun createTestEvent(
        deviceInfo: DeviceInfo,
        methodName: String,
        status: TestStatus,
        stackTrace: String? = null,
        final: Boolean = true
    ): TestEvent {
        return TestEvent(
            Instant.now(),
            DevicePoolId("myPool"),
            deviceInfo,
            TestResult(
                test = Test(pkg = "com", clazz = "SimpleTest", method = methodName, metaProperties = emptyList()),
                device = deviceInfo,
                status = status,
                startTime = 0,
                stacktrace = stackTrace,
                endTime = 100
            ),
            final
        )
    }
    fun getTestDevice() =
        DeviceInfo(
            operatingSystem = OperatingSystem("23"),
            serialNumber = "xxyyzz",
            model = "Android SDK built for x86",
            manufacturer = "unknown",
            networkState = NetworkState.CONNECTED,
            deviceFeatures = listOf(DeviceFeature.SCREENSHOT, DeviceFeature.VIDEO),
            healthy = true
        )
    fun getTestConfiguration() =
        Configuration(
            name = "",
            outputDir = Files.createTempDirectory("test-run").toFile(),
            analyticsConfiguration = AnalyticsConfiguration.DisabledAnalytics,
            poolingStrategy = null,
            shardingStrategy = null,
            sortingStrategy = null,
            batchingStrategy = null,
            flakinessStrategy = null,
            retryStrategy = null,
            filteringConfiguration = null,
            ignoreFailures = null,
            isCodeCoverageEnabled = null,
            fallbackToScreenshots = null,
            strictMode = null,
            uncompletedTestRetryQuota = null,
            testClassRegexes = null,
            includeSerialRegexes = null,
            excludeSerialRegexes = null,
            testBatchTimeoutMillis = null,
            testOutputTimeoutMillis = null,
            debug = null,
            screenRecordingPolicy = null,
            vendorConfiguration = object : VendorConfiguration {
                override fun testParser(): TestParser? = null
                override fun deviceProvider(): DeviceProvider? = null
                override fun logConfigurator(): MarathonLogConfigurator? = null
                override fun preferableRecorderType(): DeviceFeature? = null
            },
            analyticsTracking = false
        )

}
