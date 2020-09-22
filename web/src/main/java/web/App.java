package web;

public class App {
    public String getGreeting() {
        return "Hello WEB.";
    }

    public static void main(String[] args) {
        System.out.println(new App().getGreeting());
    }
}
