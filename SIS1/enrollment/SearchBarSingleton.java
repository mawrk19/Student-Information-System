package enrollment;

public class SearchBarSingleton {
    private static SearchBarSingleton instance;
    private String searchbarText;

    private SearchBarSingleton() {
        // Private constructor to prevent instantiation
    }

    public static SearchBarSingleton getInstance() {
        if (instance == null) {
            instance = new SearchBarSingleton();
        }
        return instance;
    }

    public String getSearchbarText() {
        return searchbarText;
    }

    public void setSearchbarText(String searchbarText) {
        this.searchbarText = searchbarText;
        System.out.println("searchbarText: "+ searchbarText);
    }
}
