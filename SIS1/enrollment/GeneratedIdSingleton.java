package enrollment;

public class GeneratedIdSingleton {
    private static GeneratedIdSingleton instance;
    private int generatedId;

    private GeneratedIdSingleton() {
        // Private constructor to prevent instantiation
    }

    public static GeneratedIdSingleton getInstance() {
        if (instance == null) {
            instance = new GeneratedIdSingleton();
        }
        return instance;
    }

    public int getGeneratedId() {
        return generatedId;
    }

    public void setGeneratedId(int generatedId) {
        this.generatedId = generatedId;
    }
}
