package com.sni.hairsalon.service;

import com.sni.hairsalon.service.serviceImpl.AuthentificationServiceImpl;
import com.sni.hairsalon.service.serviceImpl.UserServiceImpl;
import com.sni.hairsalon.dto.request.UserRequestDTO;
import com.sni.hairsalon.dto.response.UserResponseDTO;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

    @Mock
    private UserServiceImpl userService;
    
    @InjectMocks
    private AuthentificationServiceImpl authService;

    @Test
    void signUp_ShouldReturnUserResponseDTO_WhenValidRequest() {
        // Arrange
        UserRequestDTO requestDTO = new UserRequestDTO();
        requestDTO.setEmail("test@test.com");
        requestDTO.setPassword("password123");
        requestDTO.setRole("CLIENT");

        UserResponseDTO expectedResponse = new UserResponseDTO();
        expectedResponse.setEmail("test@test.com");
        expectedResponse.setRole("CLIENT");
        expectedResponse.setId(1L);

        when(userService.createUser(requestDTO)).thenReturn(expectedResponse);

        // Act
        UserResponseDTO actualResponse = authService.signUp(requestDTO);

        // Assert
        assertNotNull(actualResponse);
        assertEquals(expectedResponse.getEmail(), actualResponse.getEmail());
        assertEquals(expectedResponse.getRole(), actualResponse.getRole());
        assertEquals(expectedResponse.getId(), actualResponse.getId());
        verify(userService).createUser(requestDTO);
    }
}