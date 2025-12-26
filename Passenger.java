import java.util.Date;

public class Passenger {
    private String Pname;
    private int ID;
    private Date birthdate;

    public Passenger() {}

    public Passenger(String pname, int iD, Date birthdate) {
        Pname = pname;
        ID = iD;
        this.birthdate = birthdate;
    }

    public String getPname() {
        return Pname;
    }

    public void setPname(String pname) {
        Pname = pname;
    }

    public int getID() {
        return ID;
    }

    public void setID(int iD) {
        ID = iD;
    }

    public Date getBirthdate() {
        return birthdate;
    }
    
    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    @Override
    public String toString() {
        return "Passenger [Name: " + Pname + ", ID: " + ID + ", Birthday: " + birthdate + "]";
    }    
}