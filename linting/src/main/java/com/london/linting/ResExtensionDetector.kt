package com.london.linting

import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.android.tools.lint.detector.api.SourceCodeScanner
import com.intellij.psi.PsiElement
import org.jetbrains.uast.ULiteralExpression
import org.jetbrains.uast.UQualifiedReferenceExpression
import org.jetbrains.uast.UReferenceExpression
import java.util.EnumSet


class ResExtensionDetector : Detector(), SourceCodeScanner {

    override fun getApplicableReferenceNames(): List<String> = trackedExtensions.toList()

    override fun visitReference(
        context: JavaContext,
        reference: UReferenceExpression,
        referenced: PsiElement
    ) {
        val call = reference.uastParent as? UQualifiedReferenceExpression ?: return
        val receiver = call.receiver

        // 1. If it's a raw int literal like `1.string`
        if (receiver is ULiteralExpression && receiver.value is Int) {
            context.report(
                ISSUE,
                receiver,
                context.getLocation(call),
                "Avoid using raw Int literals with `.string` or `.painter`. Use a valid resource ID like R.string.app_name."
            )
            return
        }

        // 2. If it's not a valid R.string or R.drawable constant
        if (receiver is UQualifiedReferenceExpression) {
            val receiverText = receiver.asRenderString()
            val isValid = allowedPackages.any { receiverText.startsWith(it) }
            if (!isValid)
                context.report(
                    ISSUE,
                    receiver,
                    context.getLocation(call),
                    "Only use constants from R.string or R.drawable with `.string` or `.painter`."
                )
        }
    }

    companion object {
        val ISSUE = Issue.create(
            id = "UnsafeResourceExtensionUsage",
            briefDescription = "Raw int used with @StringRes or @DrawableRes extension",
            explanation = """
                Avoid using raw Ints with `.string` or `.painter`. 
                Only use valid R.string or R.drawable resource IDs.
            """.trimIndent(),
            category = Category.CORRECTNESS,
            priority = 6,
            severity = Severity.ERROR,
            implementation = Implementation(
                ResExtensionDetector::class.java,
                EnumSet.of(Scope.JAVA_FILE, Scope.TEST_SOURCES)
            )
        )

        private val trackedExtensions = setOf("string", "painter")
        private val allowedPackages = setOf("R.string", "R.drawable")
    }
}
