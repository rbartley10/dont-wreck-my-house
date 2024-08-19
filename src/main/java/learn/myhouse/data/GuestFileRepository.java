package learn.myhouse.data;

import learn.myhouse.model.Guest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class GuestFileRepository implements GuestRepository {
    private final String filePath;

    /**
     * constructor that accepts and sets the file path of the repository
     * @param filePath
     */
    public GuestFileRepository(@Value("./data/guests.csv") String filePath) {
        this.filePath = filePath;
    }

    /**
     * Method that reads each line of the given file and uses deserialize method
     * to convert each line to a Guest object and add to the list of Guests
     * @return the list of guests produced from the file
     */
    @Override
    public List<Guest> findAll(){
        ArrayList<Guest> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            reader.readLine(); // read header

            for (String line = reader.readLine(); line != null; line = reader.readLine()) { //reading each line
                //splitting each line into an array of strings with each element representing a field
                String[] fields = line.split(",", -1);
                if (fields.length == 6) {
                    //use deserialize to make a Guest object using the fields within the array
                    result.add(deserialize(fields));
                }
            }
        } catch (IOException ex) {

        }
        return result;
    }

    /**
     * Method that retrieves a guest by their email
     * @param email
     * @return the Guest with the given email
     */
    @Override
    public Guest findByEmail(String email){
        return findAll().stream()
                .filter(i -> i.getEmail().equals(email))
                .findFirst().orElse(null);
    }

    /**
     * Method that reads an array of strings and creates a Guest object
     * with the elements assigned to each field
     * @param fields
     * @return the Guest that was created from the fields
     */
    private Guest deserialize(String[] fields) {
        Guest result = new Guest();
        result.setId(Integer.parseInt(fields[0]));
        result.setFirstName(fields[1]);
        result.setLastName(fields[2]);
        result.setEmail(fields[3]);
        result.setPhone(fields[4]);
        result.setState(fields[5]);
        return result;
    }
}
