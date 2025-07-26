package com.london.linting

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.detector.api.CURRENT_API

class ResExtensionIssueRegistry : IssueRegistry() {
    override val issues = listOf(ResExtensionDetector.ISSUE)
    override val api = CURRENT_API
}
