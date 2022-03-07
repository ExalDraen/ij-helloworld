package org.alnx.example.ij.module;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;

import javax.swing.*;

import static org.alnx.example.ij.services.SidecarService.DB_SETTING_NAME;

public class HttpDbWizardStep extends ModuleWizardStep {
    private final HttpDbModuleBuilder builder;

    public HttpDbWizardStep(HttpDbModuleBuilder builder) {
        this.builder = builder;
    }

    @Override
    public JComponent getComponent() {
        return new JLabel("Something something something.");
    }

    @Override
    public void updateDataModel() {
        // update the data model in the UI, e.g. names and directories and settings type
        builder.db = "foo bar baz";  // TODO: actually prompt for db name and provide it here
    }
}
