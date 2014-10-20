package fr.opensides.flume.injector;

/**
 * User: khanh
 * To change this template use File | Settings | File Templates.
 */
public enum Type {
    FILE("file"),
    INJECT("inject");

    String type;

    private Type(String type) {
        this.type = type;
    }



}
