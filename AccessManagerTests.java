import static org.junit.Assert.*;
import org.junit.Test;

public class AccessManagerTests {

    // Test decoding of Base64 encoded header
    @Test
    public void testHeaderDecoding() {
        String base64Header = "encodedHeaderString"; // Replace with actual encoded header
        String expectedDecodedHeader = "{\"userId\":123456,\"accountName\": \"XXXXXXX\",\"role\": \"admin\"}";
        assertEquals(expectedDecodedHeader, AccessManager.decodeHeader(base64Header));
    }

    // Test adding user access as admin
    @Test
    public void testAddUserAccessAsAdmin() {
        AccessManager manager = new AccessManager();
        String adminHeader = "adminEncodedHeader"; // Replace with actual encoded admin header
        assertTrue(manager.addUserAccess(adminHeader, 123456, new String[]{"resource A", "resource B", "resource C"}));
    }

    // Test adding user access as non-admin (should fail)
    @Test(expected = UnauthorizedAccessException.class)
    public void testAddUserAccessAsNonAdmin() {
        AccessManager manager = new AccessManager();
        String userHeader = "userEncodedHeader"; // Replace with actual encoded user header
        manager.addUserAccess(userHeader, 123456, new String[]{"resource A", "resource B", "resource C"});
    }

    // Test accessing resource with permission
    @Test
    public void testAccessResourceWithPermission() {
        AccessManager manager = new AccessManager();
        String userHeader = "userEncodedHeader"; // Replace with actual encoded user header
        manager.addUserAccess("adminEncodedHeader", 123456, new String[]{"resource A"});
        assertTrue(manager.checkUserAccess(userHeader, "resource A"));
    }

    // Test accessing resource without permission
    @Test
    public void testAccessResourceWithoutPermission() {
        AccessManager manager = new AccessManager();
        String userHeader = "userEncodedHeader"; // Replace with actual encoded user header
        assertFalse(manager.checkUserAccess(userHeader, "resource B"));
    }
}
