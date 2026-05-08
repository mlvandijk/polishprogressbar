package com.github.mlvandijk.polishprogressbar.startup

import com.github.mlvandijk.polishprogressbar.progressbar.PolishFlagProgressIndicatorProvider
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity

/**
 * Project activity that ensures the Polish Flag progress bar is initialized.
 */
class MyProjectActivity : ProjectActivity {

    override suspend fun execute(project: Project) {
        // Initialize the progress bar provider
        service<PolishFlagProgressIndicatorProvider>()
    }
}
