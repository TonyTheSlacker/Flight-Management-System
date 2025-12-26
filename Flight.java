import java.util.Date;

public class Flight {
    private int AvailSeat, Duration;
    private String FlightNumber, DepartTime, DepartCity, DestiCity, ArriveTime;
    private Date DateFlight;
    public Flight() {}

    public Flight(String flightNumber, String departCity, String destiCity, String departTime, String arriveTime, int duration, int availSeat, Date dateFlight) {
        AvailSeat = availSeat;
        FlightNumber = flightNumber;
        DepartTime = departTime;
        DepartCity = departCity;
        DestiCity = destiCity;
        ArriveTime = arriveTime;
        Duration = duration;
        DateFlight = dateFlight;
    }

    public int getAvailSeat() {
        return AvailSeat;
    }

    public void setAvailSeat(int availSeat) {
        AvailSeat = availSeat;
    }

    public String getFlightNumber() {
        return FlightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        FlightNumber = flightNumber;
    }

    public String getDepartTime() {
        return DepartTime;
    }

    public void setDepartTime(String departTime) {
        DepartTime = departTime;
    }

    public String getDepartCity() {
        return DepartCity;
    }

    public void setDepartCity(String departCity) {
        DepartCity = departCity;
    }

    public String getDestiCity() {
        return DestiCity;
    }

    public void setDestiCity(String destiCity) {
        DestiCity = destiCity;
    }

    public String getArriveTime() {
        return ArriveTime;
    }

    public void setArriveTime(String arriveTime) {
        ArriveTime = arriveTime;
    }

    public int getDuration() {
        return Duration;
    }

    public void setDuration(int duration) {
        Duration = duration;
    }    

    public Date getDateFlight() {
        return DateFlight;
    }

    public void setDateFlight(Date dateFlight) {
        this.DateFlight = dateFlight;
    }

    @Override
    public String toString() {
        return "Flight [Available Seat=" + AvailSeat + ", Flight Number=" + FlightNumber 
        + ", Departure Time=" + DepartTime + ", Departure City=" + DepartCity 
        + ", Destination City=" + DestiCity + ", Duration=" + Duration + "(Minutes)" 
        + ", Arrival Time=" + ArriveTime + ", Flight Date= " + DateFlight + "]";
    }
}