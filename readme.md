RunTeam Add-On
- Исполнение Groovy-скриптов
- Исполнение jpql-запросов
- Внешние экраны

= RunTeam Add-on

= RunTeam Add-on for Jmix

:jbake-type: page
:jbake-toc:
:toc: macro
:toc-title:
:toclevels: 2
:icons: font
:experimental:
:source-language: java

== Overview

RunTeam is a powerful add-on for Jmix applications that provides an integrated console for executing Groovy scripts, JPQL queries, and scriptable screens. This tool is designed to simplify development, debugging, and maintenance tasks by providing a flexible environment for running custom logic directly within your application.

[NOTE]
====
This add-on requires Jmix version 2.4 and Java 17.
====

== Features

* Execute Groovy scripts with full access to the application context
* Run JPQL queries and inspect results
* Create and manage scriptable screens
* Convenient web-based interface
* Seamless integration into Jmix applications

== Installation

To install the RunTeam add-on in your Jmix project, add the following dependency to your `build.gradle` file:

[source,groovy]
----
dependencies {
implementation 'com.runteam:jmix-runteam:<version>'
}
----

[WARNING]
====
Replace `<version>` with the actual version number of the RunTeam add-on.
====

After adding the dependency, perform a Gradle refresh to download and include the add-on in your project.

== Usage

=== Console Interface

The RunTeam console can be accessed via the Jmix admin menu. It provides a user-friendly interface for:

* Writing and executing Groovy scripts
* Running JPQL queries and viewing results
* Managing scriptable screens

=== Scriptable Screens

Scriptable screens allow you to define custom UI components and logic using Groovy scripts. This feature is particularly useful for creating temporary or experimental interfaces without modifying the main application code.

== Configuration

By default, the RunTeam add-on is configured to work out-of-the-box. However, advanced users may customize its behavior by modifying the following properties in the `application.properties` file:

[source,properties]
----
# Enable/disable the console interface
runteam.console.enabled=true

# Define allowed IP addresses (comma-separated list)
runteam.security.allowed-ips=127.0.0.1

# Set maximum execution time for scripts (in milliseconds)
runteam.execution.timeout=30000
----

== Security Considerations

Since the RunTeam add-on provides powerful capabilities for executing arbitrary code, it's important to restrict access to trusted users only. By default, access to the console is limited to users with the `ADMIN` role. You can further customize security settings according to your specific requirements.

== Support and Contributions

If you encounter any issues or have suggestions for improvement, please open an issue on our GitHub repository: https://github.com/bizblocks/runteam

Contributions are welcome! Please follow these guidelines when submitting pull requests:

* Include tests for new features
* Maintain existing code style
* Update documentation as needed

== License

RunTeam is released under the MIT License