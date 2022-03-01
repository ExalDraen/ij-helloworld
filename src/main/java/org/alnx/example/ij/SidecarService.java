package org.alnx.example.ij;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.diagnostic.Logger;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@Service
public final class SidecarService implements Disposable {
    private final Project proj;
    private static final String DB_SETTING_NAME = "org.alnx.example.ij.db";
    private static final Logger LOG = Logger.getInstance(MethodHandles.lookup().getClass());

    private final AtomicReference<Process> myProc = new AtomicReference<>(null);

    public SidecarService(Project proj) {
        this.proj = proj;
    }

    /**
     * Start the sidecar sub-process
     *
     * @return the port number of the subprocess that was started
     */
    public int start() throws IOException {
        //TODO: don't start if already started, just use existing instance
        var db = PropertiesComponent.getInstance(proj).getValue(DB_SETTING_NAME);
        // start the subprocess
        String[] cmd = {"jvm", "--db", db};
        // should actually use processbuilder here
        try {
            var p = Runtime.getRuntime().exec(cmd);
            myProc.set(p);
        } catch (IOException e) {
            // TODO: more contextual info; or don't log
            LOG.error("Couldn't start subproc", e);
            throw e;
        }
        //TODO: get actual port from subprocess
        return 123;
    }

    /**
     * Stop the sidecar and return its exit status
     * @return exit status of subprocess
     */
    private int stop() throws InterruptedException {
        var p = myProc.get();
        p.destroy();
        // TODO: handle timeout -> destroy forcibly?
        p.waitFor(60, TimeUnit.SECONDS);
        return p.exitValue();
    }

    @Override
    public void dispose() {
        try {
            stop();
        } catch (InterruptedException ignored) {
            // TODO: some last-resort attempt to clean up?
        }
    }

    // TODO: need to dispose() and call stop() when this service is unloaded
}
