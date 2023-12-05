package students;

import java.sql.Blob;

public class Students {

    private String firstName;
    private String middleName;
    private String lastName;
    private String course;
    private String year;
    private String section;
    private String location;
    private String sem;
    private int scode;
    private String date;
    private int sid;
    private String gender;
    private Blob studentImage;
    private int start;
    private int end;
    
    // Students( firstName,  middleName,  lastName,  course,  year,  section, location,  scode,  date,  sid,  gender,  studentImage,  start, end)

    public Students(String firstName, String middleName, String lastName, String course, String year, String section,
            String location, int scode, String date, int sid, String gender, Blob studentImage, int start, int end, String sem) {

        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.course = course;
        this.year = year;
        this.section = section;
        this.location = location;
        this.scode = scode;
        this.date = date;
        this.sid = sid;
        this.gender = gender;
        this.studentImage = studentImage;
        this.start = start;
        this.end = end;
        this.sem = sem;
    }

    public Blob getStudentImage() {
        return studentImage;
    }

    public void setStudentImage(Blob studentImage) {
        this.studentImage = studentImage;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getScode() {
        return scode;
    }

    public void setScode(int scode) {
        this.scode = scode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
    
    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }
    
    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

	public String getSem() {
		return sem;	
	}
	
	public void setSem(String sem) {
        this.sem = sem;
    }
}
