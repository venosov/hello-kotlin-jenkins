@file:Suppress("SpellCheckingInspection", "SpellCheckingInspection", "SpellCheckingInspection")

package io.jenkins.plugins.sample

import hudson.Extension
import hudson.FilePath
import hudson.Launcher
import hudson.model.AbstractProject
import hudson.model.Run
import hudson.model.TaskListener
import hudson.tasks.BuildStepDescriptor
import hudson.tasks.Builder
import hudson.util.FormValidation
import jenkins.tasks.SimpleBuildStep
import org.jenkinsci.Symbol
import org.kohsuke.stapler.DataBoundConstructor
import org.kohsuke.stapler.DataBoundSetter
import org.kohsuke.stapler.QueryParameter
import java.io.IOException
import javax.annotation.Nonnull
import javax.servlet.ServletException

class HelloWorldBuilder @DataBoundConstructor constructor(val name: String) : Builder(), SimpleBuildStep {
    @set:DataBoundSetter
    var isUseFrench = false

    @Throws(InterruptedException::class, IOException::class)
    override fun perform(@Nonnull p0: Run<*, *>, @Nonnull workspace: FilePath, @Nonnull launcher: Launcher, @Nonnull listener: TaskListener) {
        if (isUseFrench) {
            listener.logger.println("Bonjour, $name!")
        } else {
            listener.logger.println("Hello, $name!")
        }
    }

    @Symbol("greet")
    @Extension
    class DescriptorImpl : BuildStepDescriptor<Builder?>() {
        @Throws(IOException::class, ServletException::class)
        fun doCheckName(@QueryParameter value: String, @QueryParameter useFrench: Boolean): FormValidation {
            if (value.isEmpty()) return FormValidation.error(Messages.HelloWorldBuilder_DescriptorImpl_errors_missingName())
            if (value.length < 4) return FormValidation.warning(Messages.HelloWorldBuilder_DescriptorImpl_warnings_tooShort())
            return if (!useFrench && value.matches(".*[éáàç].*".toRegex())) {
                FormValidation.warning(Messages.HelloWorldBuilder_DescriptorImpl_warnings_reallyFrench())
            } else FormValidation.ok()
        }

        override fun isApplicable(aClass: Class<out AbstractProject<*, *>?>?): Boolean {
            return true
        }

        override fun getDisplayName(): String {
            return Messages.HelloWorldBuilder_DescriptorImpl_DisplayName()
        }
    }

}