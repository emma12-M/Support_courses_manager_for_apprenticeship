package main.java.singleton;

public class AppConfig {

    private static AppConfig instance;

    private String applicationName;

    private AppConfig() {

        applicationName =
                "Support Course Management";
    }

    public static AppConfig getInstance() {

        if(instance == null) {

            instance = new AppConfig();
        }

        return instance;
    }

    public String getApplicationName() {

        return applicationName;
    }

    public void setApplicationName(
            String applicationName) {

        this.applicationName =
                applicationName;
    }
}