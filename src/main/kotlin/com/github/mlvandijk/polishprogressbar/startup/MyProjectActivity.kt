package com.github.mlvandijk.polishprogressbar.startup

import com.github.mlvandijk.polishprogressbar.progressbar.PolishProgressIndicatorProvider
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity

/**
 * Project activity that ensures the Polish progress bar is initialized.
 */
class MyProjectActivity : ProjectActivity {

    override suspend fun execute(project: Project) {
        // Initialize the progress bar provider
        service<PolishProgressIndicatorProvider>()
    }
}
