package learn.myhouse.data;

import learn.myhouse.model.Host;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HostFileRepositoryTest {
    private static final String SEED_FILE_PATH = "./data/hosts-seed.csv";
    private static final String TEST_FILE_PATH = "./data/hosts-test.csv";

    private final HostFileRepository repo = new HostFileRepository(TEST_FILE_PATH);

    @BeforeEach
    public void setup() throws IOException {
        Path seedPath = Paths.get(SEED_FILE_PATH);
        Path testPath = Paths.get(TEST_FILE_PATH);

        Files.copy(seedPath,testPath, StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    public void shouldFindAll(){
        List<Host> all = repo.findAll();
        assertEquals(1000,all.size());
        assertEquals("eyearnes0@sfgate.com",all.get(0).getEmail());
        assertEquals("3 Nova Trail",all.get(0).getAddress());

    }

    @Test
    public void shouldFindExistingEmail(){
        Host actual = repo.findByEmail("smarkee@cafepress.com");
        assertNotNull(actual);
        assertEquals("a7a4459e-f511-4599-9481-9de67fbeff51",actual.getId());
        assertEquals("Marke",actual.getLastName());
    }

    @Test
    public void shouldNotFindNonExistingEmail(){
        Host actual = repo.findByEmail("not_an_email");
        assertNull(actual);
    }

}