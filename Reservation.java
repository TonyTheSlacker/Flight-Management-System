import java.util.ArrayList;

public class Reservation {
    private int reservationId, GateNum;
    private Flight flight;
    ArrayList<Passenger> passenger;

    public Reservation() {}

    public Reservation(int reservationId, int gateNum, Flight flight, ArrayList<Passenger> passenger) {
        this.reservationId = reservationId;
        GateNum = gateNum;
        this.flight = flight;
        this.passenger = passenger;
    }
    
    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public int getGateNum() {
        return GateNum;
    }

    public void setGateNum(int gateNum) {
        GateNum = gateNum;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public ArrayList<Passenger> getPassenger() {
        return passenger;
    }

    public void setPassenger(ArrayList<Passenger> passenger) {
        this.passenger = passenger;
    }

    @Override
    public String toString() {
        return "Reservation [reservationId=" + reservationId + ", GateNum=" + GateNum + ", flight=" + flight
                + ", passenger=" + passenger + "]";
    }
}