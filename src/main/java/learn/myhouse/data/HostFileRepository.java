package learn.myhouse.data;

import learn.myhouse.model.Host;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Repository
public class HostFileRepository implements HostRepository {
    private final String filePath;

    /**
     * constructor that accepts and sets the file path of the repository
     * @param filePath
     */
    public HostFileRepository(@Value("./data/hosts.csv") String filePath) {
        this.filePath = filePath;
    }

    /**
     * Method that reads each line of the given file and uses deserialize method
     * to convert each line to a Host object and add to the list of Hosts
     * @return the list of hosts produced from the file
     */
    @Override
    public List<Host> findAll(){
        ArrayList<Host> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            reader.readLine(); // read header

            //splitting each line into an array of strings with each element representing a field
            for (String line = reader.readLine(); line != null; line = reader.readLine()) { //reading each line

                String[] fields = line.split(",", -1);
                if (fields.length == 10) {
                    //use deserialize to make a Host object using the fields within the array
                    result.add(deserialize(fields));
                }
            }
        } catch (IOException ex) {

        }
        return result;
    }

    /**
     * Method that retrieves a host by their email
     * @param email
     * @return the host with the given email
     */
    @Override
    public Host findByEmail(String email){
        return findAll().stream()
                .filter(i -> i.getEmail().equals(email))
                .findFirst().orElse(null);
    }

    /**
     * Method that reads an array of strings and creates a Host object
     * with the elements assigned to each field
     * @param fields
     * @return the Host that was created from the fields
     */
    private Host deserialize(String[] fields) {
        Host result = new Host();
        result.setId(fields[0]);
        result.setLastName(fields[1]);
        result.setEmail(fields[2]);
        result.setPhone(fields[3]);
        result.setAddress(fields[4]);
        result.setCity(fields[5]);
        result.setState(fields[6]);
        result.setPostCode(fields[7]);
        result.setStandardRate(new BigDecimal(fields[8]));
        result.setWeekendRate(new BigDecimal(fields[9]));
        return result;
    }

}
