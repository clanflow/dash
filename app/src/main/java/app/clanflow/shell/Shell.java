package app.clanflow.shell;

public interface Shell {
    static public void PrintDelimiter() {
        System.out.println("==============================================");
        System.out.flush();
    }

    static public void PrintPrompt(String prefix) {
        System.out.print(prefix + "> ");
        System.out.flush();
    }
    public void interact();
    public boolean quit();
}
