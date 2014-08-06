package com.waelawada.genericreporter.domain;

import java.util.Map;

/**
 * Created by waelawada on 8/5/14.
 */
public class Procedure {

    private String name;
    private String database;
    private Map<String, String> parameters;

    public Procedure() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }
}
