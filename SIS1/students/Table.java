package students;

public class Table {

    private String sub_code;
    private String subject;
    private String professor;
    private String schedule;
    private String day;
    private int units;

    public Table(String sub_code, String subject, String professor, String schedule, String day, int units) {
        this.sub_code = sub_code;
        this.subject = subject;
        this.professor = professor;
        this.schedule = schedule;
        this.day = day;
        this.units = units;
    }

    public String getSub_code() {
        return sub_code;
    }

    public void setSub_code(String sub_code) {
        this.sub_code = sub_code;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }
}