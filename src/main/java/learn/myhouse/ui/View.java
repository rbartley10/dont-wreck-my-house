package learn.myhouse.ui;

import learn.myhouse.model.Guest;
import learn.myhouse.model.Host;
import learn.myhouse.model.Reservation;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class View {
    public final ConsoleIO io; //facilitates user input and application output

    /**
     * constructor that accepts a Console IO object
     * @param io
     */
    public View(ConsoleIO io){
        this.io = io;
    }

    /**
     * Method that displays the main menu options and returns
     * the user's menu selection
     * @return the user's menu selection
     */
    public MainMenuOption selectMainMenuOption() {
        displayHeader("Main Menu");
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;

        //printing menu options along with corresponding values
        for (MainMenuOption option : MainMenuOption.values()) {
            io.printf("%s. %s%n", option.getValue(), option.getMessage());

            //getting max and min values from menu option values
            min = Math.min(min, option.getValue());
            max = Math.max(max, option.getValue());
        }

        //displaying valid range of input
        String message = String.format("Select [%s-%s]: ", min, max);
        return MainMenuOption.fromValue(io.readInt(message, min, max));
    }

    /**
     * Method that accepts a list of Reservations and displays each reservation
     * in an organized manner.
     * @param reservations
     */
    public void displayReservations(List<Reservation> reservations){
        if(reservations == null || reservations.isEmpty()){ //printing message for empty list
            io.println("No reservations found");
            return;
        }
        io.println("");

        //displaying information for each reservation
        for(Reservation reservation: reservations){
            io.printf("ID: %s, %s - %s, Guest: %s, %s, Email: %s, Total: $%s%n",
                    reservation.getId(),
                    reservation.getStartDate(),
                    reservation.getEndDate(),
                    reservation.getGuest().getLastName(),
                    reservation.getGuest().getFirstName(),
                    reservation.getGuest().getEmail(),
                    reservation.getTotal().setScale(2,RoundingMode.HALF_UP));
        }
    }

    /**
     * Method that accepts a Host and a Guest object, calculates the total and
     * creates a new Reservation based on the user's start date and end date inputs
     * @param guest
     * @param host
     * @return the new Reservation to be made
     */
    public Reservation makeReservation(Guest guest, Host host){
        //reading dates from user
        LocalDate startDate = io.readLocalDate("Enter start date [MM/dd/yyyy]: ");
        LocalDate endDate = io.readLocalDate("Enter end date [MM/dd/yyyy]: ");

        //calculating total
        BigDecimal total = getReservationTotal(startDate,endDate,host);

        return new Reservation(startDate,endDate,guest,host,total);
    }

    /**
     * Method that accepts an existing Reservation, allows the user to change
     * start and end dates, recalculates total and creates a new Reservation
     * with the updated dates.
     * @param reservation
     * @return the reservation that should replace the previous one
     */
    public Reservation editReservation(Reservation reservation){
        //reading dates from user
        LocalDate startDate = reservation.getStartDate();
        LocalDate endDate = reservation.getEndDate();

        //optional input of dates to be uodated
        startDate = io.readUpdateDate("Start", startDate);
        endDate = io.readUpdateDate("End", endDate);

        //calculating total
        BigDecimal total = getReservationTotal(startDate,endDate,reservation.getHost());

        //setting changed fields
        reservation.setStartDate(startDate);
        reservation.setEndDate(endDate);
        reservation.setTotal(total);

        return reservation;
    }

    /**
     * Method that accepts a start date, end date, a host and calculates the reservation
     * total based on the length of stay, the day of the weeks and the rates of the host
     * @param startDate
     * @param endDate
     * @param host
     * @return the total based on dates and rates
     */
    private BigDecimal getReservationTotal(LocalDate startDate, LocalDate endDate, Host host){
        //counting the number of days between start and end dates
        int noOfDays = (int) ChronoUnit.DAYS.between(startDate,endDate);
        BigDecimal weekDay = BigDecimal.ZERO;
        BigDecimal weekEnd = BigDecimal.ZERO;

        for(int i=0;i<noOfDays;i++){
            //checking if day of the week is a weekend for weekend rate
            if(startDate.plusDays(i).getDayOfWeek() == DayOfWeek.FRIDAY ||
                    startDate.plusDays(i).getDayOfWeek() == DayOfWeek.SATURDAY){
                weekEnd = weekEnd.add(BigDecimal.ONE); //adding one to weekend count
            }else { //checking if day of the week is a week day for standard rate
                weekDay = weekDay.add(BigDecimal.ONE); //adding one to week day count
            }
        }

        //multiplying the days counter with its corresponding rate and adding the two sums
        return weekDay.multiply(host.getStandardRate())
                .add(weekEnd.multiply(host.getWeekendRate()));
    }

    /**
     * Method that uses ConsoleIO to display a header
     * @param message
     */
    // displaying header only
    public void displayHeader(String message) {
        io.println("");
        io.println(message);
        io.println("=".repeat(message.length()));
    }

    /**
     * Method that uses ConsoleIO to display an exception
     * @param ex
     */
    public void displayException(Exception ex) {
        displayHeader("A critical error occurred:");
        io.println(ex.getMessage());
    }

    /**
     * Method that uses ConsoleIO to display a status with a single message
     * @param success
     * @param message
     */
    public void displayStatus(boolean success, String message) {
        displayStatus(success, List.of(message));
    }

    /**
     * Method that uses ConsoleIO to display a status with a list of messages
     * as strings
     * @param success
     * @param messages
     */
    public void displayStatus(boolean success, List<String> messages) {
        displayHeader(success ? "Success" : "Error");
        for (String message : messages) {
            io.println(message);
        }
    }

    /**
     * Method that displays the summary of a Reservation to be added or updated.
     * Asks the user to confirm the new reservation to be made or updated
     * @param reservation
     * @return whether the user confirms the Reservation
     */
    public boolean displaySummary(Reservation reservation){
        displayHeader("Summary");
        io.printf("Start: %s%n",reservation.getStartDate());
        io.printf("End: %s%n",reservation.getEndDate());
        io.printf("Total: %s%n",reservation.getTotal().setScale(2, RoundingMode.HALF_UP));
        return io.readBoolean("Is this okay? [y/n]: "); //reading confirmation decision from user
    }

    /**
     * Method that uses ConsoleIO to get a Host email
     * @return the entered email
     */
    public String getHostEmail(){
        return io.readRequiredString("Host Email: ");
    }

    /**
     * Method that uses ConsoleIO to get a Guest email
     * @return the entered email
     */
    public String getGuestEmail(){
        return io.readRequiredString("Guest Email: ");
    }

    /**
     * Method that displays a list of reservations and uses ConsoleIO
     * to allow the user to select a reservation ID from the list
     * @param reservations
     * @return
     */
    public int selectReservationID(List<Reservation> reservations ){
        displayReservations(reservations);
        int id = io.readInt("Select Reservation ID : ");

        try{
            boolean found = reservations.stream().anyMatch(i -> i.getId() == id);

            if(found){
                return  id;
            }else{
                io.println("Reservation ID not found in list");
                return -999;
            }
        } catch (NumberFormatException ex){
            io.println("[INVALID] incorrect number input");
            return -999;
        }

    }

}
