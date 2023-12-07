package enrollment;

public class Subject {
    private int id;
    private String subCode;
    private int units;
    private String subject;

    public Subject(int id, String subCode, int units, String subject) {
        this.id = id;
        this.subCode = subCode;
        this.units = units;
        this.subject = subject;
    }

    // Assuming you have a constructor like this in your code
    public Subject(String string, int int1, String string2, String string3, String string4, String string5) {
        // TODO: Implement this constructor based on your needs
    }

    // Getter for id
    public int getId() {
        return id;
    }

    // Getter for subCode
    public String getSubCode() {
        return subCode;
    }

    // Getter for units
    public int getUnits() {
        return units;
    }

    // Getter for subject
    public String getSubject() {
        return subject;
    }

    // Setter for subCode
    public void setSubCode(String subCode) {
        this.subCode = subCode;
    }

    public void setID(int id) {
        this.id = id;
    }
    
    public void setUnits(int units) {
        this.units = units;
    }
    
    public void setSubject(String subject) {
        this.subject = subject;
    }
    
    // You can implement setters for other properties (id, units, subject) similarly if needed
}
