package learn.myhouse.domain;

import learn.myhouse.data.DataException;
import learn.myhouse.data.GuestRepository;
import learn.myhouse.data.HostRepository;
import learn.myhouse.data.ReservationRepository;
import learn.myhouse.model.Guest;
import learn.myhouse.model.Host;
import learn.myhouse.model.Reservation;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final HostRepository hostRepository;
    private final GuestRepository guestRepository;

    /**
     * constructor that accepts and sets the three repositories that the
     * service will carry out tasks on
     * @param reservationRepository
     * @param hostRepository
     * @param guestRepository
     */
    public ReservationService(ReservationRepository reservationRepository, HostRepository hostRepository, GuestRepository guestRepository) {
        this.reservationRepository = reservationRepository;
        this.hostRepository = hostRepository;
        this.guestRepository = guestRepository;
    }

    /**
     * Method that uses all three repositories to retrieve the current list of
     * reservations
     * @return the current list of guests
     */
    public List<Reservation> findByHostId(String hostId){

        //mapping host id and Host
        Map<String, Host> hostMap = hostRepository.findAll()
                .stream().collect(Collectors.toMap(i->i.getId(),i -> i));

        //mapping guest id and Guest
        Map<Integer, Guest> guestMap = guestRepository.findAll()
                .stream().collect(Collectors.toMap(i -> i.getId(), i -> i));

        //get the list of reservations within a host's reservation file
        List<Reservation> result = reservationRepository.findByHostId(hostId);

        /*using the ID retrieved from file and the mappings to fully retrieve
        each Guest and Host and set the values based on the key (ID)*/
        for(Reservation reservation: result){
            reservation.setHost(hostMap.get(reservation.getHost().getId()));
            reservation.setGuest(guestMap.get(reservation.getGuest().getId()));
        }

        return result;
    }

    /**
     * Method that validates reservation and uses the repository to add the
     * Reservation if it is found to be valid
     * @param reservation
     * @return the reservation result of the attempted addition
     * @throws DataException
     */
    public Result<Reservation> add(Reservation reservation) throws DataException {
        Result<Reservation> result = validate(reservation); //general validation

        if(!result.isSuccess()){ //checking if validation failed
            return result;
        }

        LocalDate start = reservation.getStartDate();
        LocalDate end = reservation.getEndDate();

        //checking if dates overlap with existing reservations
        for(Reservation r: findByHostId(reservation.getHost().getId())){
            if( !(start.isBefore(r.getStartDate()) && end.isBefore(r.getStartDate()))
            && !(start.isAfter(r.getEndDate()) && end.isAfter(r.getEndDate())))
            {
                result.addErrorMessage("date must not overlap with existing reservation");
                return result;
            }
        }

        //add if validations were successful
        result.setPayload(reservationRepository.add(reservation));
        return result;
    }

    /**
     * Method that validates reservation and uses the repository to update the
     * Reservation if it is found to be valid
     * @param reservation
     * @return the reservation result of the attempted update
     * @throws DataException
     */
    public Result<Reservation> update(Reservation reservation) throws DataException {
        Result<Reservation> result = validate(reservation); //general validation

        if(!result.isSuccess()){ //checking if validation failed
            return result;
        }

        LocalDate start = reservation.getStartDate();
        LocalDate end = reservation.getEndDate();

        /*checking if dates overlap with all existing reservations except for the current
         reservation being updated*/
        for(Reservation r: findByHostId(reservation.getHost().getId())){
            if( !(start.isBefore(r.getStartDate()) && end.isBefore(r.getStartDate()))
                    && !(start.isAfter(r.getEndDate()) && end.isAfter(r.getEndDate()))){
                if(reservation.getId() != r.getId()){
                    result.addErrorMessage("date must not overlap with existing reservation");
                    return result;
                }
            }
        }

        boolean updated = reservationRepository.update(reservation); //attempting to update
        if(!updated){
            result.addErrorMessage("reservation does not exist");
        }
        return result;
    }

    /**
     * Method that accepts an ID and a host ID to validate a reservation
     * and uses the repository to delete the Reservation given that it
     * has been founded and validated
     * @param id
     * @param hostId
     * @return the reservation result of the attempted deletion
     * @throws DataException
     */
    public Result<Reservation> deleteById(int id, String hostId) throws DataException {
        Result<Reservation> result = new Result<>();

        //using find first since there will be only one reservation within the file with the given ID
        Reservation reservation = reservationRepository.findByHostId(hostId).stream()
                .filter(i -> i.getId() == id).findFirst().orElse(null);

        //checking if reservation has already passed since we can't cancel a reservation in the past
        if(reservation!=null){
            if( reservation.getStartDate().isBefore(LocalDate.now()) ){
                result.addErrorMessage("cannot delete past reservation");
                return result;
            }
        }

        //attempting to delete
        if(!reservationRepository.deleteById(id,hostId)){  //checking if deletion was unsuccessful
            result.addErrorMessage("reservation does not exist");
        }
        return result;
    }

    /**
     * Method that does general validation for the update and add methods
     * @param reservation
     * @return the reservation result of the attempted validation
     */
    private Result<Reservation> validate(Reservation reservation){
        Result<Reservation> result = validateNulls(reservation); //validating nulls

        if(!result.isSuccess()){
            return result;
        }

        //checking if host exists
        if(reservation.getHost().getId() == null
                || hostRepository.findByEmail(reservation.getHost().getEmail()) == null ){
            result.addErrorMessage("host cannot be found");
        }

        //checking if guest exists
        if(guestRepository.findByEmail(reservation.getGuest().getEmail()) == null){
            result.addErrorMessage("guest cannot be found");
        }

        //checking if start date is before end date
        validateDates(reservation,result);

        return result;
    }

    /**
     * Helper method used by the validation method to ensure that a
     * start and end date is not on the same day or reversed
     * @param reservation
     * @param result
     */
    private void validateDates(Reservation reservation, Result<Reservation> result){
        LocalDate start = reservation.getStartDate();
        LocalDate end = reservation.getEndDate();

        //checking if start date is before end date
        if(!start.isBefore(end)){
            result.addErrorMessage("start date must be before end date");
        }

        //checking if start date is in future
        if(!reservation.getStartDate().isAfter(LocalDate.now())){
            result.addErrorMessage("start date must be in the future");
        }
    }

    /**
     * Helper method used by the validation method to ensure that neither
     * a Reservation nor its fields are null
     * @param reservation
     * @return the reservation result of the attempted validation
     */
    private Result<Reservation> validateNulls(Reservation reservation){
        Result<Reservation> result = new Result<>();

        //checking if Reservation object is null
        if(reservation == null){
            result.addErrorMessage("reservation cannot be null");
            return result;
        }

        //checking if fields are null
        if(reservation.getGuest() == null){
            result.addErrorMessage("guest cannot be null");
        }

        if(reservation.getHost() == null){
            result.addErrorMessage("host cannot be null");
        }

        if(reservation.getStartDate() == null){
            result.addErrorMessage("start date cannot be null");
        }

        if(reservation.getEndDate() == null){
            result.addErrorMessage("end date cannot be null");
        }

        return result;
    }

    /**
     * Method that sorts a list of reservations by date from earliest to latest
     * @param reservations
     * @return the sorted list of reservations
     */
    public List<Reservation> sortByDate(List<Reservation> reservations){
        //sorting by date for a move organized view
        return reservations.stream()
                .sorted(Comparator.comparing(Reservation::getStartDate))
                .collect(Collectors.toList());
    }
}
