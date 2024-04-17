import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import java.util.*;

@RestController
public class AccessManagerController {

    private Map<Integer, List<String>> userAccess = new CurrentHashMap<>();

    @PostMapping("/admin/addUser")
    public ResponseEntity<String> addUser(@RequestHeader("Authorization") String authHeader, @RequestBody Map<String, Object> requestBody) {
        if (!isAdmin(authHeader)) {
            return new ResponseEntity<>("Access Denied: Only admins can add users.", HttpStatus.FORBIDDEN);
        }
        Integer userId = (Integer) requestBody.get("userId");
        List<String> resources = (List<String>) requestBody.get("endpoint");
        userAccess.put(userId, resources);
        return new ResponseEntity<>("User access added successfully.", HttpStatus.OK);
    }

    @GetMapping("/user/{resource}")
    public ResponseEntity<String> accessResource(@RequestHeader("Authorization") String authHeader, @PathVariable String resource) {
        if (!isUser(authHeader)) {
            return new ResponseEntity<>("Access Denied: Only users can access resources.", HttpStatus.FORBIDDEN);
        }
        Integer userId = decodeHeader(authHeader).get("userId");
        if (userAccess.getOrDefault(userId, Collections.emptyList()).contains(resource)) {
            return new ResponseEntity<>("Access Granted: You can access " + resource, HttpStatus.OK);
        }
        return new ResponseEntity<>("Access Denied: You do not have access to " + resource, HttpStatus.FORBIDDEN);
    }

    private boolean isAdmin(String authHeader) {
        Map<String, Object> decodedHeader = decodeHeader(authHeader);
        return "admin".equals(decodedHeader.get("role"));
    }

    private boolean isUser(String authHeader) {
        Map<String, Object> decodedHeader = decodeHeader(authHeader);
        return "user".equals(decodedHeader.get("role"));
    }

    private Map<String, Object> decodeHeader(String authHeader) {
        // Decode the Base64 encoded header and return the decoded JSON as a Map
        // Placeholder for actual decoding logic
        return new HashMap<>();
    }
}
