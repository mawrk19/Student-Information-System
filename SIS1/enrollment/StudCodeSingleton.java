package enrollment;

public class StudCodeSingleton {
    private static StudCodeSingleton instance;
    private String studCode;

    private StudCodeSingleton() {
        // Private constructor to prevent instantiation
    }

    public static StudCodeSingleton getInstance() {
        if (instance == null) {
            instance = new StudCodeSingleton();
        }
        return instance;
    }

    public String getStudCode() {
        return studCode;
    }

    public void setStudCode(String studCode) {
        this.studCode = studCode;
    }
}
