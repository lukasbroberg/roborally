/**
 * For demonstration purposes and for avoiding some JavaFX warnings, the RoboRally
 * application is now configured as a Java module.
 */
module roborally {

    requires javafx.controls;
    requires org.jetbrains.annotations;
    requires java.sql;
    // requires mysql.connector.j; to avoid warnings not (explicitly) required anymore
    requires com.google.common;
    requires com.google.gson;

    requires bowman.client;
    requires spring.hateoas;
    requires org.javassist;
    requires com.fasterxml.jackson.databind;
    requires spring.web;
    requires spring.core;
    requires java.desktop;

    exports dk.dtu.compute.se.pisd.roborally;

    opens dk.dtu.compute.se.pisd.roborally.gameselection.model;
    opens schemas;
    opens properties;
    opens boards;

    // The following are needed for Gson being able to create the
    // respective Java objects when reading JSON files
    exports dk.dtu.compute.se.pisd.roborally.model;
    exports dk.dtu.compute.se.pisd.roborally.fileaccess.model;

    exports dk.dtu.compute.se.pisd.roborally.gameselection.model;

    // The following are needed only for generating the JavaDocs via maven
    // for the respective packages
    exports dk.dtu.compute.se.pisd.roborally.view;
    exports dk.dtu.compute.se.pisd.roborally.controller;

    exports dk.dtu.compute.se.pisd.roborally.dal;
    exports dk.dtu.compute.se.pisd.roborally.fileaccess;

    exports dk.dtu.compute.se.pisd.designpatterns.observer;
    exports dk.dtu.compute.se.pisd.roborally.gameselection.view;

}