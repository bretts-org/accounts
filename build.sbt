val accounts = (
  project in file(".")
    configs UiTest.UiTest
    settings Common.settings
    settings Dependencies.settings
    enablePlugins(Git.plugins: _*)
    settings Git.settings
    settings Run.settings
    settings UiTest.settings
    enablePlugins(Native.plugins: _*)
    settings Native.settings
)
