package learn.myhouse.data;

import learn.myhouse.model.Guest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GuestFileRepositoryTest {

    private static final String SEED_FILE_PATH = "./data/guests-seed.csv";
    private static final String TEST_FILE_PATH = "./data/guests-test.csv";

    private final GuestFileRepository repo = new GuestFileRepository(TEST_FILE_PATH);

    @BeforeEach
    public void setup() throws IOException {
        Path seedPath = Paths.get(SEED_FILE_PATH);
        Path testPath = Paths.get(TEST_FILE_PATH);

        Files.copy(seedPath,testPath, StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    public void shouldFindAll(){
        List<Guest> all = repo.findAll();
        assertEquals(1000,all.size());
        assertEquals("slomas0@mediafire.com",all.get(0).getEmail());
        assertEquals("Sullivan",all.get(0).getFirstName());

    }

    @Test
    public void shouldFindExistingEmail(){
        Guest actual = repo.findByEmail("pkemmis1d@harvard.edu");
        assertNotNull(actual);
        assertEquals("(202) 8298371",actual.getPhone());
        assertEquals("Kemmis",actual.getLastName());
    }

    @Test
    public void shouldNotFindNonExistingEmail(){
        Guest actual = repo.findByEmail("not_an_email");
        assertNull(actual);
    }

}