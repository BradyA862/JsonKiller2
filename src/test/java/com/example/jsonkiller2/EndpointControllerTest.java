package com.example.jsonkiller2;

import com.example.jsonkiller2.MD5.MD5;
import com.example.jsonkiller2.UserAccount.UserAccount;
import com.example.jsonkiller2.UserAccount.UserAccountRepository;
import com.example.jsonkiller2.dateTime.DateTime;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EndpointControllerTest {
    @InjectMocks
    EndpointController controller;
    @Mock
    HttpServletRequest request;

    @Mock
    UserAccountRepository repository;

    @Mock
    HashMap<UUID, Long> tokenMap;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void itShouldReturnUnauthWhenUserIsWrong() {
        final var username = UUID.randomUUID().toString();
        final var password = UUID.randomUUID().toString();
        lenient().when(repository.findByUsernameAndPassword(username, password))
                .thenReturn(Optional.empty());
        lenient().when(repository.findByUsernameAndPassword(not(eq(username)), eq(password)))
                .thenReturn(Optional.of(new UserAccount(username, password)));
        assertThrows(ResponseStatusException.class, () -> controller.login(username, password));
    }

    @Test
    void itShouldReturnUnauthWhenPassIsWrong() {
        final var username = UUID.randomUUID().toString();
        final var password = UUID.randomUUID().toString();
        lenient().when(repository.findByUsernameAndPassword(username, password))
                .thenReturn(Optional.empty());
        lenient().when(repository.findByUsernameAndPassword(eq(username), not(eq(password))))
                .thenReturn(Optional.of(new UserAccount(username, password)));
        assertThrows(ResponseStatusException.class, () -> controller.login(username, password));
    }

    @Test
    void itShouldMapTheUUIDToTheIdWhenLoginSuccess() {
        final var username = UUID.randomUUID().toString();
        final var password = UUID.randomUUID().toString();
        final Long id = (long) (Math.random() * 9999999);
        final UserAccount expected = new UserAccount(username, password);
        expected.id = id;
        expected.username = username;
        expected.password = password;
        when(repository.findByUsernameAndPassword(username, password))
                .thenReturn(Optional.of(expected));
        ArgumentCaptor<UUID> captor = ArgumentCaptor.forClass(UUID.class);
        when(tokenMap.put(captor.capture(), eq(id))).thenReturn(0L);
        final var token = controller.login(username, password);
        assertEquals(token, captor.getValue());
    }

    @Test
    void itShouldReturnInvalidIfUsernameExists() {
        final String username = "some username";
        final String password = "some password";
        when(repository.findByUsername(username)).thenReturn(Optional.of(
                new UserAccount(username, password)));
        assertThrows(ResponseStatusException.class, () -> {
            controller.register(username, "");
        });
    }

    @Test
    void itShouldSaveANewUserAccountWhenUserIsUnique() {
        final String username = "some username";
        final String password = "some password";
        when(repository.findByUsername(username)).thenReturn(Optional.empty());
        ArgumentCaptor<UserAccount> captor = ArgumentCaptor.forClass(UserAccount.class);
        when(repository.save(captor.capture())).thenReturn(new UserAccount(username, password));
        Assertions.assertDoesNotThrow(() -> {
            controller.register(username, password);
        });
        assertEquals(new UserAccount(username, password), captor.getValue());
    }

    @Test
    void itShouldThrowUnathWhenIPIsCalledWithBadToken() {
        final var token = UUID.randomUUID();
        when(tokenMap.containsKey(token)).thenReturn(false);
        assertThrows(ResponseStatusException.class, () -> controller.ip(token, request));
    }

    @Test
    void itShouldReturnTheClientsIP() throws JsonProcessingException {
        final String ip = "1.2.3.4";
        final String expected = "{\"ip\":\"" + ip + "\"}";
        final var token = UUID.randomUUID();
        when(request.getRemoteAddr()).thenReturn(ip);
        when(tokenMap.containsKey(token)).thenReturn(true);
        assertEquals(expected, objectMapper.writeValueAsString(controller.ip(token, request)));
    }

////    @Test
////    void itShouldCallDateTime() {
////        final String expected = "05/18/2000";
//        Mockito.mockStatic(LocalDateTime.class).when(LocalDateTime::now)
//                .thenReturn(expected);
//        assertEquals(expected, controller.dateTime());
////    }

    @Test
    void itShouldReturnCurrentDateTime() throws JsonProcessingException {
        LocalDateTime localDateTime = LocalDateTime.of(2005, 05, 14,
                00, 00, 00);

        final String date = "05/14/2005";
        final long millis = 1116046800000L;
        final String time = "00:00:00 AM";
        final String expected = "{\"time\":\"" + time + "\"," +
                "\"milliseconds_since_epoch\":" + millis + "," +
                "\"date\":\"" + date + "\"}";
        Mockito.mockStatic(LocalDateTime.class).when(LocalDateTime::now).thenReturn(localDateTime);
        DateTime thingy = controller.dateTime();
        assertEquals(expected, objectMapper.writeValueAsString(thingy));
    }

    @Test
    void itShouldReturnHeaders() {
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("somehow", "this works");
        Map headers = controller.getHeaders(expectedMap);
        assertEquals(expectedMap, headers);
    }

    @Test
    void itShouldReturnMD5CalculationResult() throws NoSuchAlgorithmException {
        String md5 = "fa4c6baa0812e5b5c80ed8885e55a8a6";
        String original = "example_text";
        MD5 md5Function = controller.md5(original);
        Assertions.assertEquals(md5, md5Function.md5);
        Assertions.assertEquals(original, md5Function.original);
    }

    @Test
    void itShouldTakeInAStringAndReturnHashMap() {
        HashMap validateTrue = new HashMap<String, String>();
        validateTrue.put("validate", true);
        Assertions.assertEquals(validateTrue,
                controller.validateJson("{}"));
    }
}

// "{, \"date\":\"" + date + "\"}";

