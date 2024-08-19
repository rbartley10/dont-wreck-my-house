package learn.myhouse.data;

import learn.myhouse.model.Guest;
import learn.myhouse.model.Host;
import learn.myhouse.model.Reservation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ReservationFileRepository implements ReservationRepository {
    //header present since file will be written to
    private static final String HEADER = "id,start_date,end_date,guest_id,total";
    private final String directory;

    /**
     *constructor that accepts and sets the directory path of the repository
     * @param directory
     */
    public ReservationFileRepository(@Value("./data/reservations") String directory) {
        this.directory = directory;
    }

    /**
     * Method that find a file within a given directory and host ID, reads each
     * line of the file and uses deserialize method to convert each line to a
     * Reservation object and add to the list of reservations
     * @return the list of reservations produced from the file
     */
    @Override
    public List<Reservation> findByHostId(String hostId) {
        ArrayList<Reservation> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(getFilePath(hostId)))) {

            reader.readLine(); // read header

            //reading each line with data and adding each Reservation object to the list
            for (String line = reader.readLine(); line != null; line = reader.readLine()) { //reading each line

                String[] fields = line.split(",", -1);
                if (fields.length == 5) {
                    //use deserialize to make a Reservation object using the fields within the array
                    result.add(deserialize(fields, hostId));
                }
            }
        } catch (IOException ex) {

        }
        return result;
    }

    /**
     * Method that adds a Reservation object to the current list of
     * reservations. Writes the updated list of reservations in
     * the file dedicated to the current host
     * @param reservation
     * @return the Reservation object that was added
     * @throws DataException
     */
    @Override
    public Reservation add(Reservation reservation) throws DataException {
        if (reservation == null) {
            return null;
        }

        List<Reservation> all = findByHostId(reservation.getHost().getId());

        //finding the highest ID plus one
        int nextID = all.stream().
                mapToInt(Reservation::getId).
                max().
                orElse(0) + 1;

        reservation.setId(nextID); //setting the ID before adding
        all.add(reservation); //adding to current list of reservations
        writeAll(all, reservation.getHost().getId()); //overwriting the file with current list of reservations
        return reservation;
    }

    /**
     * Method that updates/replaces an existing Reservation object with a
     * new Reservation object
     * @param reservation
     * @return whether the update was successful
     * @throws DataException
     */
    @Override
    public boolean update(Reservation reservation) throws DataException {
        List<Reservation> all = findByHostId(reservation.getHost().getId());

        //replacing reservation if ID is found
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getId() == reservation.getId()) {
                all.set(i, reservation);
                writeAll(all, reservation.getHost().getId());
                return true;
            }
        }
        return false;
    }

    /**
     * Method that deletes an existing Reservation object given an ID number
     * and a corresponding host ID
     * @param id
     * @param hostID
     * @return whether the deletion was successful
     * @throws DataException
     */
    @Override
    public boolean deleteById(int id, String hostID) throws DataException {

        List<Reservation> all = findByHostId(hostID);

        //removing reservation if ID is founded in the hosts list of reservation
        for(int i=0;i<all.size();i++){
            if(all.get(i).getId() == id){ //checking if panel with ID exists
                all.remove(i);
                writeAll(all,hostID);
                return true;
            }
        }
        return false;
    }

    /**
     * Method that joins the host ID string to the directory to produce
     * the desired file path.
     * @param hostId
     * @return the desired file path
     */
    private String getFilePath(String hostId) {
        return Paths.get(directory, hostId + ".csv").toString();
    }

    /**
     * Method that rewrites a specific reservation file with each current
     * reservation within the list on a line
     * @param reservations
     * @param hostId
     * @throws DataException
     */
    private void writeAll(List<Reservation> reservations, String hostId) throws DataException {
        try (PrintWriter writer = new PrintWriter(getFilePath(hostId))) {

            writer.println(HEADER); //writing header

            //writing each Reservation object as a line of string using serialize
            for (Reservation reservation: reservations) {
                writer.println(serialize(reservation));
            }
        } catch (FileNotFoundException ex) {
            throw new DataException(ex);
        }
    }

    /**
     * Method that accepts a Reservation object and represents it as a line of string
     * with each field separated by commas
     * @param reservation
     * @return The comma delimited string produced
     */
    private String serialize(Reservation reservation) {
        return String.format("%s,%s,%s,%s,%s",
                (reservation.getId()),
                reservation.getStartDate(),
                reservation.getEndDate(),
                reservation.getGuest().getId(),
                reservation.getTotal());
    }

    /**
     * Method that reads an array of strings and creates a Reservation object
     * with the elements assigned to each field
     * @param fields
     * @return the Guest that was created from the fields
     */
    private Reservation deserialize(String[] fields, String id) {
        Reservation result = new Reservation();
        result.setId(Integer.parseInt(fields[0]));
        result.setStartDate(LocalDate.parse(fields[1]));
        result.setEndDate(LocalDate.parse(fields[2]));

        //simply setting guest ID since that is the only guest info stored within the file
        Guest guest = new Guest();
        guest.setId(Integer.parseInt(fields[3]));
        result.setGuest(guest);

        //simply setting host ID since that is the only guest info stored within the file
        Host host = new Host();
        host.setId(id);
        result.setHost(host);

        result.setTotal(new BigDecimal(fields[4]));

        return result;
    }
}
