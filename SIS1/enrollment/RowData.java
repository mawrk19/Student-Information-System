package enrollment;

public class RowData {
	private String category;
    private String price;

    public RowData(String category, String price) {
        this.category = category;
        this.price = price;
    }

    public String getCategory () {
    	return category;
    }
    
    public void setCategory(String category) {
    	this.category = category;
    }
    
    public String getPrice () {
    	return price;
    }
    
    public void setPrice (String price) {
    	this.price = price;
    }

}
