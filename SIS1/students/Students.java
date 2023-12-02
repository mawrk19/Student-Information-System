package students;

public class Students {

	private String fullname;
	private String course;
	private String year;
	private String section;
	private String location;
	private int scode;
	private String date;
	private int sid;
	private String gender;

	public Students(String fullname, String course, String year, String section, String location, int scode,
			String date, int sid, String gender) {
		this.fullname = fullname;
		this.course = course;
		this.year = year;
		this.section = section;
		this.location = location;
		this.scode = scode;
		this.date = date;
		this.sid = sid;
		this.gender = gender;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		// Allow up to 4 first names
		String[] names = fullname.split("\\s+");
		StringBuilder truncatedName = new StringBuilder();
		for (int i = 0; i < Math.min(names.length, 4); i++) {
			truncatedName.append(names[i]);
			if (i < 3) {
				truncatedName.append(" ");
			}
		}
		this.fullname = truncatedName.toString();
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
}
