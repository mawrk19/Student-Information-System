package schedule;

public class Schedule {
    private String subcodeCLMN;
    private String credunitCLMN;
    private String descriptionCLMN; 
    private int sdidCLMN;

    public Schedule(String subcodeCLMN, String credunitCLMN, String descriptionCLMN, int sdidCLMN) {
        this.subcodeCLMN = subcodeCLMN;
        this.credunitCLMN = credunitCLMN;
        this.descriptionCLMN = descriptionCLMN;
        this.sdidCLMN = sdidCLMN;
    }

    public String getsubcodeCLMN() {
        return subcodeCLMN;
    }

    public void setSubcodeCLMN(String subcodeCLMN) {
        this.subcodeCLMN = subcodeCLMN;
    }
    
    public String getCredunitCLMN() {
        return credunitCLMN;
    }

    public void setCredunitCLMNCLMN(String credunitCLMN) {
        this.credunitCLMN = credunitCLMN;
    }
    
    public String getDescriptionCLMN() {
        return descriptionCLMN;
    }
    
    public void setDescriptionCLMN(String descriptionCLMN) {
        this.descriptionCLMN = descriptionCLMN;
    
    public int getSdidCLMN() {
        return sdidCLMN;
    }
}