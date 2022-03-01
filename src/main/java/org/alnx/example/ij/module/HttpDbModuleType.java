package org.alnx.example.ij.module;

import com.intellij.icons.AllIcons;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.module.ModuleTypeManager;
import com.intellij.openapi.roots.ui.configuration.ModulesProvider;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class HttpDbModuleType extends ModuleType<HttpDbModuleBuilder> {
    public static final String ID = "DEMO_HTTPDB_MOD_TYPE";

    public HttpDbModuleType() {
        super(ID);
    }

    @Override
    public @NotNull HttpDbModuleBuilder createModuleBuilder() {
        return new HttpDbModuleBuilder();
    }

    public static HttpDbModuleType getInstance() {
        return (HttpDbModuleType) ModuleTypeManager.getInstance().findByID(ID);
    }

    @Override
    public @NotNull @Nls(capitalization = Nls.Capitalization.Title) String getName() {
        return "HttpDb Module";
    }

    @Override
    public @NotNull @Nls(capitalization = Nls.Capitalization.Sentence) String getDescription() {
        return "HttpDb Module, i.e. a module implemented in a remote db accessible over http.";
    }

    @Override
    public @NotNull Icon getNodeIcon(boolean isOpened) {
        //var url = getClass().getResource("META-INF/pluginIcon.svg");
        //var url = getClass().getResource("/images/moduleIcon.PNG");
        //ImageIcon ii = new ImageIcon(url, "module icon");
        //TODO: icon doesn't appear, needs fixing
        //new ImageIcon("images/moduleIconWhite.PNG");
        return AllIcons.General.ArrowUp;
    }

    @Override
    public ModuleWizardStep @NotNull [] createWizardSteps(@NotNull WizardContext wizardContext, @NotNull HttpDbModuleBuilder moduleBuilder, @NotNull ModulesProvider modulesProvider) {
        return super.createWizardSteps(wizardContext, moduleBuilder, modulesProvider);
    }
}
