package studentprof;

public class Subject {
	
	private String subjectCode;
    private String description;
    private int creditUnits;
    private String section;
    private String subject;
    private String day;
    private String time;
    private String professor;
    private String schedule;
    private int slots;

    public Subject(String subjectCode, int creditUnits, String subject, String day, String time, String professor) {
        this.subjectCode = subjectCode;
        this.creditUnits = creditUnits;
        this.subject = subject;
        this.professor = professor;
        this.day = day;
        this.time = time;
    }
    

    public String getSubjectCode() {
        return subjectCode;
    }
    
    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCreditUnits() {
        return creditUnits;
    }

    public void setCreditUnits(int creditUnits) {
        this.creditUnits = creditUnits;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public int getSlots() {
        return slots;
    }

    public void setSlots(int slots) {
        this.slots = slots;
    }
}

