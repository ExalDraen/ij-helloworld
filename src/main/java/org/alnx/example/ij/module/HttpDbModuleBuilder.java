package org.alnx.example.ij.module;

import com.intellij.ide.util.projectWizard.ModuleBuilder;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.roots.ModifiableRootModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HttpDbModuleBuilder extends ModuleBuilder {

    String db;

    @Override
    public void setupRootModel(@NotNull ModifiableRootModel modifiableRootModel) throws ConfigurationException {
        // this is where we set up the source , test folders, sdk, etc.
        // instantiate vfs here
    }

    @Override
    public HttpDbModuleType getModuleType() {
        return HttpDbModuleType.getInstance();
    }

    @Override
    public @Nullable ModuleWizardStep getCustomOptionsStep(WizardContext context, Disposable parentDisposable) {
        return new HttpDbWizardStep(this);
    }
}
