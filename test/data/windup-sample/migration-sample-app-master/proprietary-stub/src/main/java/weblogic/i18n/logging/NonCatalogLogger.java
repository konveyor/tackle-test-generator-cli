package weblogic.i18n.logging;

public class NonCatalogLogger {
    private String name;

    public NonCatalogLogger () {
        this.name = "NoName";
    }

    public NonCatalogLogger(String name) {
        this.name = name;
    }

    public void info(String log) {
        System.out.println("LOG[" + this.name + "]: " + log);
    }
}
