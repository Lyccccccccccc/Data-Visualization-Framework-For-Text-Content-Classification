package edu.cmu.cs214.hw6.framework.GUI;

public class DPlugin {
    private final String name;
    private final String link;

    public DPlugin(String name, String link){
        this.name = name;
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }

    @Override
    public String toString() {
        return "{ \"name\": \"" + this.name + "\"," +
                " \"link\": \"" + this.link + "\"}";
    }
}
