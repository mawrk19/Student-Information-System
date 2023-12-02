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

    public int getId() {
        return id;
    }

    public String getSubCode() {
        return subCode;
    }

    public int getUnits() {
        return units;
    }

    public String getSubject() {
        return subject;
    }
}