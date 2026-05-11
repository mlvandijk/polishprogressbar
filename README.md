# Polish Progress Bar

This progress bar was originally vibe coded as a joke.
* Find the original code [here](https://github.com/mlvandijk/progress-bar-pl).
* Find the related blogpost [here](https://maritvandijk.com/vibe-coding-an-intellij-idea-plugin/).

Since that version no longer builds, this version was updated based on a new plugin template from the [IntelliJ Platform Plugin Generator](https://plugins.jetbrains.com/generator), with the help of [Jakub Chrzanowski](https://www.linkedin.com/in/chrzanowski/), who also did some code cleanup.

# Features
* Custom progress bar with Polish flag colors (white and red)
* Polish smile emoji that moves along the progress bar

# How to test
Use the run configuration **Run Plugin**.
This will open a sandbox IDE where you can verify the plugin displays the intended behavior.

# How to build
Run the Gradle task `buildPlugin` to build the plugin.

# How to install
Since this plugin is not published to the [JetBrains Marketplace](https://plugins.jetbrains.com/), you will have to build it yourself (see above) and install it manually.
To do so:
* Open **Settings | Plugins**
* Click the gear icon on the top right and select **Install Plugin from Disk**
* Find the location of the repository on your system, open `polish-progress-bar/build/distributions` and select `polishprogressbar- <version>.zip`
* Click **OK**

# How to uninstall
* Open **Settings | Plugins**
* Make sure you are on the **Installed** tab
* Look for this plugin
* In the dropdown menu **Disable** select **Uninstall**
* When asked to confirm, select **Yes**
* Click **OK**
* If prompted to, **Restart IDE**