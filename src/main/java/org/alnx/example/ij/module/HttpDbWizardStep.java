package org.alnx.example.ij.module;

import com.intellij.ide.util.projectWizard.ModuleWizardStep;

import javax.swing.*;

public class HttpDbWizardStep extends ModuleWizardStep {
    @Override
    public JComponent getComponent() {
        return new JLabel("Something something something.");
    }

    @Override
    public void updateDataModel() {
        // do something useful.
    }
}
