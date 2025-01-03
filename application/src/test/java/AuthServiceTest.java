import com.example.metrics.UserLoginAttempts;
import com.example.model.entity.User;
import com.example.service.AuthService;
import com.example.service.DetailsService;
import com.example.utils.JwtUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;

class AuthServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private DetailsService detailsService;
    @Mock
    private JwtUtils jwtUtils;
    @Mock
    private UserLoginAttempts userLoginAttempts;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterSuccess() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("rawPassword");

        User savedUser = new User();
        savedUser.setEmail("test@example.com");
        savedUser.setPassword("encodedPassword");

        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(detailsService.createUser(user)).thenReturn(savedUser);
        when(jwtUtils.generateAccessToken(any(HashMap.class), eq(savedUser))).thenReturn("mockToken");

        Pair<User, String> result = authService.register(user);

        assertNotNull(result);
        assertEquals(savedUser, result.getLeft());
        assertEquals("mockToken", result.getRight());
        verify(userLoginAttempts).increment();
        verify(detailsService).createUser(user);
    }

    @Test
    void testAuthenticateSuccess() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("rawPassword");

        User authenticatedUser = new User();
        authenticatedUser.setEmail("test@example.com");
        authenticatedUser.setPassword("encodedPassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(authenticatedUser, null, authenticatedUser.getAuthorities()));
        when(jwtUtils.generateAccessToken(any(HashMap.class), eq(authenticatedUser))).thenReturn("mockToken");

        Pair<User, String> result = authService.authenticate(user);

        assertNotNull(result);
        assertEquals(authenticatedUser, result.getLeft());
        assertEquals("mockToken", result.getRight());
        verify(userLoginAttempts).increment();
    }


    @Test
    void testRegisterEncodesPassword() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("rawPassword");

        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(detailsService.createUser(user)).thenAnswer(invocation -> invocation.getArgument(0));

        authService.register(user);

        assertEquals("encodedPassword", user.getPassword());
        verify(passwordEncoder).encode("rawPassword");
    }

    @Test
    void testAuthenticateThrowsExceptionOnInvalidCredentials() {
        User user = new User();
        user.setEmail("invalid@example.com");
        user.setPassword("invalidPassword");

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        when(authenticationManager.authenticate(token)).thenThrow(new RuntimeException("Authentication failed"));

        assertThrows(RuntimeException.class, () -> authService.authenticate(user));
        verify(userLoginAttempts).increment();
    }
}
