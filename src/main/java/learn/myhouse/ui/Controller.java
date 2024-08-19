package learn.myhouse.ui;

import learn.myhouse.data.DataException;
import learn.myhouse.domain.GuestService;
import learn.myhouse.domain.HostService;
import learn.myhouse.domain.ReservationService;
import learn.myhouse.domain.Result;
import learn.myhouse.model.Guest;
import learn.myhouse.model.Host;
import learn.myhouse.model.Reservation;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class Controller {
    //repositories
    private final ReservationService reservationService;
    private final HostService hostService;
    private final GuestService guestService;

    //view
    private final View view;

    /**
     * constructor that accepts the three services and a view as a dependency injection
     * @param reservationService
     * @param hostService
     * @param guestService
     * @param view
     */
    public Controller(ReservationService reservationService, HostService hostService, GuestService guestService, View view) {
        this.reservationService = reservationService;
        this.hostService = hostService;
        this.guestService = guestService;
        this.view = view;
    }

    /**
     * Method that runs the app by calling the runAppLoop method
     */
    public void run() {
        view.displayHeader("Welcome to Don't Wreck My House");
        try {
            runAppLoop(); //running app loop
        } catch (DataException ex) {
            view.displayException(ex);
        }
        view.displayHeader("Goodbye."); //goodbye message
    }

    /**
     * The method that gets the menu selection and uses that to decide
     * which app function should be carried out
     * @throws DataException
     */
    private void runAppLoop() throws DataException {
        MainMenuOption option;
        do {
            option = view.selectMainMenuOption(); //getting user manu option and storing
            switch (option) { //deciding which method to run based on user menu selection
                case VIEW_RESERVATIONS_BY_DATE:
                    viewReservationsByHost();
                    break;
                case MAKE_RESERVATION:
                    makeReservation();
                    break;
                case EDIT_RESERVATION:
                    editReservation();
                    break;
                case CANCEL_RESERVATION:
                    cancelReservation();
                    break;
            }
        } while (option != MainMenuOption.EXIT);
    }

    /**
     * Method that uses the view, host service and reservation service to
     * allow the user to select a host and view a sorted (by date) list of reservations
     * for that particular host
     */
    private void viewReservationsByHost(){
        view.displayHeader("View Reservations for Host");
        String email = view.getHostEmail(); //getting user email input
        Host host = hostService.findByEmail(email); //finding Host by email
        if(host==null){ //checking if email belongs to an existing host
            view.io.println("\nHost does not exist");
            return;
        }

        //retrieving and displaying the sorted list of reservations for the given Host
        view.displayHeader(String.format("%s: %s,%s",host.getLastName(),host.getCity(),host.getState()));
        List<Reservation> reservations = reservationService.findByHostId(host.getId());
        view.displayReservations(reservationService.sortByDate(reservations));
    }

    /**
     * Method that uses the view, host service, reservation service and
     * guest service to allow a user to select a guest and their desired
     * host to make a new Reservation. Displays a list of the current
     * Reservations under the chosen host before they are allowed to
     * enter reservation details.
     * @throws DataException
     */
    private void makeReservation() throws DataException {
        view.displayHeader("Make a Reservation");
        Guest guest = guestService.findByEmail(view.getGuestEmail()); //finding Guest by email
        if(guest == null){
            view.io.println("\nguest does not exist");
            return;
        }

        Host host = hostService.findByEmail(view.getHostEmail()); //finding Host by email
        if(host == null){
            view.io.println("\nhost does not exist");
            return;
        }

        //retrieving and displaying the sorted list of reservations for the given Host
        view.displayHeader(String.format("%s: %s,%s",host.getLastName(),host.getCity(),host.getState()));
        List<Reservation> reservations = reservationService.findByHostId(host.getId());
        view.displayReservations(reservationService.sortByDate(reservations));

        //making reservation
        Reservation reservation = view.makeReservation(guest,host);


        boolean confirm = view.displaySummary(reservation); //displaying summary

        //checking for user confirmation and displaying result of the task
        if(confirm){
            Result<Reservation> result = reservationService.add(reservation);

            if(!result.isSuccess()){
                view.displayStatus(false, result.getErrorMessages());
            }else{
                String successMessage = String.format("Reservation %s created",result.getPayload().getId());
                view.displayStatus(true, successMessage);
            }
        }else{
            view.io.println("Reservation was not created");
        }
    }

    /**
     * Method that uses the view, host service, reservation service and
     * guest service to allow a user to select a guest and their desired
     * host to update an existing Reservation. Displays a list of the current
     * Reservations between the guest and chosen host before they are allowed to
     * alter the dates of the reservation.
     * @throws DataException
     */
    public void editReservation() throws DataException {
        view.displayHeader("Edit a Reservation");
        Guest guest = guestService.findByEmail(view.getGuestEmail()); //finding Guest by email
        if(guest == null){
            view.io.println("\nguest does not exist");
            return;
        }

        Host host = hostService.findByEmail(view.getHostEmail()); //finding Host by email
        if(host == null){
            view.io.println("\nhost does not exist");
            return;
        }

        //retrieving list of reservations for the given guest and host
        view.displayHeader(String.format("%s: %s,%s",host.getLastName(),host.getCity(),host.getState()));
        List<Reservation> reservations = reservationService.findByHostId(host.getId())
                .stream()
                .filter(i -> i.getGuest().getId() == guest.getId())
                .filter(i -> i.getStartDate().isAfter(LocalDate.now()))
                .collect(Collectors.toList());

        if(!reservations.isEmpty()){
            //accepting user reservation ID input while displaying current reservations between Host and Guest
            int id = view.selectReservationID(reservationService.sortByDate(reservations));
            if(id != -999){
                Reservation beforeUpdate = reservations.stream().filter(i -> i.getId() == id).collect(Collectors.toList()).get(0);

                if(beforeUpdate == null){
                    return;
                }

                Reservation reservation = view.editReservation(beforeUpdate); //begin to update reservation

                //checking for user confirmation and displaying result of the task
                boolean confirm = view.displaySummary(beforeUpdate);
                if(confirm){
                    Result<Reservation> result = reservationService.update(reservation);

                    if(!result.isSuccess()){
                        view.displayStatus(false, result.getErrorMessages());
                    }else{
                        String successMessage = String.format("Reservation %s updated",reservation.getId());
                        view.displayStatus(true, successMessage);
                    }
                }else{
                    view.io.println("Reservation was not updated");
                }
            }
        }else{
            view.io.println("No future reservations found for this guest at host's location");
        }
    }

    /**
     * Method that uses the view, host service, reservation service and
     * guest service to allow a user to select a guest and their desired
     * host to delete an existing Reservation. Displays a list of the current
     * future reservations under guest and chosen host before they are allowed to
     * delete the desired reservation.
     * @throws DataException
     */
    public void cancelReservation() throws DataException {
        view.displayHeader("Cancel A Reservation");
        Guest guest = guestService.findByEmail(view.getGuestEmail()); //finding Guest by email
        if(guest == null){
            view.io.println("\nguest does not exist");
            return;
        }

        Host host = hostService.findByEmail(view.getHostEmail()); //finding Host by email
        if(host == null){
            view.io.println("\nhost does not exist");
            return;
        }

        Result<Reservation> result = new Result<>();

        //finding all future reservations between Host and Guest
        List<Reservation> reservations = reservationService.findByHostId(host.getId())
                .stream()
                .filter(i -> i.getGuest().getId() == guest.getId())
                .filter(i -> i.getStartDate().isAfter(LocalDate.now()))
                .collect(Collectors.toList());

        if(!reservations.isEmpty()){
            /*accepting user reservation ID input while displaying current future
             reservations between Host and Guest */
            int id = view.selectReservationID(reservationService.sortByDate(reservations));
            result = reservationService.deleteById(id, host.getId());

            //checking if deletion was successful and displaying status message
            if(!result.isSuccess()){
                view.displayStatus(false, result.getErrorMessages());
            }else{
                String successMessage = String.format("Reservation %s deleted",id);
                view.displayStatus(true, successMessage);
            }
        }else{
            view.io.println("No future reservations found for this guest at host's location");
        }
    }
}