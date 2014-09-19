package io.lumify.web.clientapi;

import io.lumify.web.clientapi.codegen.*;
import io.lumify.web.clientapi.codegen.model.UserMe;
import io.lumify.web.clientapi.codegen.model.Workspace;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LumifyApi {
    private final UserApi userApi;
    private final ArtifactApiExt artifactApi;
    private final WorkspaceApiExt workspaceApi;
    private final WorkspacesApi workspacesApi;
    private final AdminApiExt adminApi;
    private final VertexApi vertexApi;
    private final OntologyApiExt ontologyApi;
    private final String basePath;
    private Workspace currentWorkspace;

    public LumifyApi(String basePath) {
        this.basePath = basePath;

        userApi = new UserApi();
        userApi.setBasePath(basePath);

        artifactApi = new ArtifactApiExt();
        artifactApi.setBasePath(basePath);

        workspaceApi = new WorkspaceApiExt();
        workspaceApi.setBasePath(basePath);

        workspacesApi = new WorkspacesApi();
        workspacesApi.setBasePath(basePath);

        adminApi = new AdminApiExt();
        adminApi.setBasePath(basePath);

        vertexApi = new VertexApi();
        vertexApi.setBasePath(basePath);

        ontologyApi = new OntologyApiExt();
        ontologyApi.setBasePath(basePath);
    }

    public UserApi getUserApi() {
        return userApi;
    }

    public ArtifactApiExt getArtifactApi() {
        return artifactApi;
    }

    public WorkspaceApiExt getWorkspaceApi() {
        return workspaceApi;
    }

    public WorkspacesApi getWorkspacesApi() {
        return workspacesApi;
    }

    public AdminApiExt getAdminApi() {
        return adminApi;
    }

    public VertexApi getVertexApi() {
        return vertexApi;
    }

    public OntologyApiExt getOntologyApi() {
        return ontologyApi;
    }

    public Workspace getCurrentWorkspace() {
        return currentWorkspace;
    }

    public Workspace loginAndGetCurrentWorkspace() throws ApiException {
        UserMe me = getUserApi().getMe();
        ApiInvoker.getInstance().setCsrfToken(me.getCsrfToken());

        List<Workspace> workspaces = getWorkspacesApi().getAll().getWorkspaces();

        currentWorkspace = null;
        if (me.getCurrentWorkspaceId() != null) {
            for (Workspace workspace : workspaces) {
                if (workspace.getWorkspaceId().equals(me.getCurrentWorkspaceId())) {
                    currentWorkspace = workspace;
                    break;
                }
            }
        }

        if (currentWorkspace == null) {
            if (workspaces.size() == 0) {
                currentWorkspace = getWorkspaceApi().create();
            } else {
                currentWorkspace = workspaces.get(0);
            }
        }

        ApiInvoker.getInstance().setWorkspaceId(currentWorkspace.getWorkspaceId());

        return currentWorkspace;
    }

    public String invokeAPI(String path, String method, Map<String, String> queryParams, Object body, Map<String, String> headerParams, Map<String, String> formParams, String contentType) throws ApiException {
        if (queryParams == null) {
            queryParams = new HashMap<String, String>();
        }
        if (headerParams == null) {
            headerParams = new HashMap<String, String>();
        }
        if (formParams == null) {
            formParams = new HashMap<String, String>();
        }
        return ApiInvoker.getInstance().invokeAPI(this.basePath, path, method, queryParams, body, headerParams, formParams, contentType);
    }
}