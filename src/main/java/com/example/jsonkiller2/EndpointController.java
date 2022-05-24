package com.example.jsonkiller2;

import com.example.jsonkiller2.MD5.MD5;
import com.example.jsonkiller2.UserAccount.UserAccount;
import com.example.jsonkiller2.UserAccount.UserAccountRepository;
import com.example.jsonkiller2.ValidJson.ValidJson;
import com.example.jsonkiller2.dateTime.DateTime;
import com.example.jsonkiller2.ip.IP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class EndpointController {

    private final UserAccountRepository repository;

    private final HashMap<UUID, Long> tokenMap;

    private final RestTemplate rest;

    @Autowired
    public EndpointController(@NonNull UserAccountRepository repository) {
        this.repository = repository;
        this.tokenMap = new HashMap<>();
        rest = new RestTemplate();
    }

    public EndpointController(@NonNull UserAccountRepository repository,
                              @NonNull HashMap<UUID, Long> tokenMap, RestTemplate rest) {
        this.repository = repository;
        this.tokenMap = tokenMap;
        this.rest = rest;
    }

    public void checkAuthorized(UUID token) {
        String url = "http://localhost:8081/isAuthorized?token=" + token;
        final ResponseEntity<Void> response = rest.getForEntity(url, Void.class);

        switch (response.getStatusCode()) {
            case OK:
                return;

            case UNAUTHORIZED:
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

            default:
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /* IP */

    @GetMapping("/ip")
    @CrossOrigin
    public IP ip(@RequestParam UUID token, HttpServletRequest request) {
        if (!tokenMap.containsKey(token))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        return new IP(request.getRemoteAddr());
    }

    /* Date */

    @GetMapping("/date")
    @CrossOrigin
    public DateTime dateTime() {
        return new DateTime(LocalDateTime.now());
    }

    /* HTTP Headers */

    @GetMapping("/headers")
    @CrossOrigin
    public Map getHeaders(@RequestHeader Map<String, String> map) {
        return map;
    }

    /* MD5 */

    @GetMapping("/md5")
    @CrossOrigin
    public MD5 md5(@RequestParam String input) throws NoSuchAlgorithmException {
        return new MD5(input);
    }

    /* JSONValid */

    @GetMapping("/json")
    @CrossOrigin
    public HashMap validateJson(@RequestParam String input) {
        ValidJson thingy = new ValidJson();
        return thingy.isValidJson(input);
    }

    /* Register */
    @GetMapping("/register")
    public void register(@RequestParam String username, @RequestParam String password) {
        if (repository.findByUsername(username).isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT);

        repository.save(new UserAccount(username, password));
    }

    /* Login */
    @GetMapping("/login")
    UUID login(@RequestParam String username, @RequestParam String password) {
        var result = repository.findByUsernameAndPassword(username, password);

        if (result.isEmpty())
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        final var token = UUID.randomUUID();
        tokenMap.put(token, result.get().id);
        return token;
    }



    /* Logout */

}
