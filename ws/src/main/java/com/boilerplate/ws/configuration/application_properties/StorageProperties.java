package com.boilerplate.ws.configuration.application_properties;

public class StorageProperties {
    private String root;
    private String profile;

    public StorageProperties() {
    }

    public StorageProperties(String root, String profile) {
        this.root = root;
        this.profile = profile;
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
