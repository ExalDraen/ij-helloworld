package org.alnx.example.ij.services;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * A service which provides access to data / files over http, using
 * a locally hosted http server managed by {@link SidecarService}
 */
@Service
public class HttpBridgeService {
    private static final String GET_PROJECTS_ENDPOINT = "getProjects";
    private final Project proj;
    private final HttpClient client = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(20))
            .build();
    private static final Logger LOG = Logger.getInstance(MethodHandles.lookup().getClass());


    public HttpBridgeService(Project proj) {
        this.proj = proj;
    }

    /**
     * Send request to local http server and provide response
     *
     * @return response body as a string
     */
    private String sendRequest(String path) throws IOException, IllegalArgumentException, InterruptedException {
        var sidecar = proj.getService(SidecarService.class);
        var port = sidecar.start();
        var host = "localhost:" + port;
        try {
            var uri = new URI("http", host, path, "");
            var req = HttpRequest.newBuilder()
                    .GET()
                    .uri(uri)
                    .build();
            var response = client.send(req, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (URISyntaxException e) {
            LOG.error("Bad path {} provided", path);
            throw new IllegalArgumentException("Bad path provided: " + path);
        } catch (InterruptedException e) {
            LOG.error("Interrupted during http request sending");
            throw e;
        }
    }

    public String[] getProjects() throws InterruptedException, IllegalArgumentException, IOException {
        var resp = sendRequest(GET_PROJECTS_ENDPOINT);

        //TODO: parse the response as json, not as comma separated values ...
        return resp.split(",");
    }
}
