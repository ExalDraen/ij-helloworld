package org.alnx.example.ij.services;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@Service
public final class SidecarService implements Disposable {
    public static final int STOP_TIMEOUT_S = 60;
    private final Project proj;
    public static final String DB_SETTING_NAME = "org.alnx.example.ij.db";
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
        // TODO: don't start if already started, just use existing instance
        // TODO: need to set the db value somewhere!
        // this uses the propertiescomponent service, see https://plugins.jetbrains.com/docs/intellij/persisting-state-of-components.html#using-propertiescomponent-for-simple-non-roamable-persistence
        var db = PropertiesComponent.getInstance(proj).getValue(DB_SETTING_NAME);
        if (db == null) {
            throw new IllegalStateException("Database not set! Can't start sidecar service without it");
        }
        // start the subprocess
        LOG.info("Using db " + db);
        String[] cmd = {"python", "-m", "http.server", "--bind", "127.0.0.1", "--db"};
        LOG.info("Launching command: " + Arrays.toString(cmd));
        try {
            var p = new ProcessBuilder(cmd)
                    .redirectError(ProcessBuilder.Redirect.INHERIT)
                    .redirectOutput(ProcessBuilder.Redirect.INHERIT)
                    .start();
            myProc.set(p);
        } catch (IOException e) {
            // TODO: more contextual info; or don't log
            LOG.error("Couldn't start subprocess", e);
            throw e;
        }
        //TODO: get actual port from subprocess
        return 123;
    }

    /**
     * Stop the sidecar and return its exit status
     *
     * @return exit status of subprocess
     */
    private int stop() throws InterruptedException {
        var p = myProc.get();
        p.destroy();
        if (!p.waitFor(STOP_TIMEOUT_S, TimeUnit.SECONDS)) {
            LOG.warn(String.format("Timed out after %s s waiting for service to shut down", STOP_TIMEOUT_S));
            // TODO: better final cleanup handling
            p.destroyForcibly();
        }
        return p.exitValue();
    }

    @Override
    public void dispose() {
        try {
            int rc = stop();
            LOG.trace("Process exited with" + rc);
        } catch (InterruptedException ignored) {
            // TODO: some last-resort attempt to clean up?
        }
    }

    // TODO: need to dispose() and call stop() when this service is unloaded
}
