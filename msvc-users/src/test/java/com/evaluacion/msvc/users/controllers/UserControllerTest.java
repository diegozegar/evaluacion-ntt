package com.evaluacion.msvc.users.controllers;

import com.evaluacion.msvc.users.DTO.UserDTO;
import com.evaluacion.msvc.users.DTO.UserResponseDTO;
import com.evaluacion.msvc.users.security.JwtFilter;
import com.evaluacion.msvc.users.security.config.SecurityConfig;
import com.evaluacion.msvc.users.services.IUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class))
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private IUserService userService;

        @MockBean
        private SecurityFilterChain securityFilterChain;

        @MockBean
        JwtFilter jwtFilter;

        @Test
        void findAllUsers_returnsList() throws Exception {
                when(userService.findAllUsers()).thenReturn(List.of());

                mockMvc.perform(get("/api/users/all_users"))
                                .andExpect(status().isOk());

                verify(userService).findAllUsers();
        }

        @Test
        void findUserById_returnsUser() throws Exception {
                UUID id = UUID.randomUUID();
                UserResponseDTO dto = new UserResponseDTO();
                dto.setToken("token");

                when(userService.findUserById(id)).thenReturn(dto);

                mockMvc.perform(get("/api/users/find_user_by_id/" + id))
                                .andExpect(status().isOk());

                verify(userService).findUserById(id);
        }

        @Test
        void findUserByEmail_returnsUser() throws Exception {
                UserResponseDTO dto = new UserResponseDTO();
                dto.setToken("token");
                when(userService.findUserByEmail("user@test.com")).thenReturn(dto);

                mockMvc.perform(get("/api/users/find_user_by_email/user@test.com"))
                                .andExpect(status().isOk());

                verify(userService).findUserByEmail("user@test.com");
        }

        @Test
        void createUser_returnsCreated() throws Exception {
                UserResponseDTO dto = new UserResponseDTO();
                dto.setToken("token");
                when(userService.createUser(any(UserDTO.class))).thenReturn(dto);

                String body = """
                                {
                                  "name":"User Test",
                                  "email":"user@test.com",
                                  "password":"1234",
                                  "phones": [
                                        {
                                          "number": "11111",
                                          "cityCode": "1",
                                          "countryCode": "11"
                                        }
                                    ]
                                }
                                """;

                mockMvc.perform(post("/api/users/create")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                                .andExpect(status().isCreated());

                verify(userService).createUser(any(UserDTO.class));
        }

        @Test
        void updateUserById_returnsUpdated() throws Exception {
                UUID id = UUID.randomUUID();

                UserResponseDTO dto = new UserResponseDTO();
                dto.setToken("updated");

                when(userService.updateById(eq(id), any(UserDTO.class))).thenReturn(dto);

                String body = """
                                {
                                  "name":"User Test UPDATE",
                                  "email":"user@testupdate.com",
                                  "password":"12345",
                                  "phones": [
                                        {
                                          "number": "11111",
                                          "cityCode": "1",
                                          "countryCode": "11"
                                        },
                                        {
                                          "number": "2222",
                                          "cityCode": "2",
                                          "countryCode": "222"
                                        }
                                    ]
                                }
                                """;

                mockMvc.perform(put("/api/users/update_user_by_id/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                                .andExpect(status().isCreated());

                verify(userService).updateById(eq(id), any(UserDTO.class));
        }

        @Test
        void updateUserByEmail_returnsUpdated() throws Exception {
                UserResponseDTO dto = new UserResponseDTO();
                dto.setToken("updated");

                when(userService.updateByEmail(eq("user@testupdate.com"), any(UserDTO.class))).thenReturn(dto);

                String body = """
                                {
                                  "name":"User Test UPDATE BY EMAIL",
                                  "email":"user@testupdate.com",
                                  "password":"12345",
                                  "phones": [
                                        {
                                          "number": "333333",
                                          "cityCode": "3",
                                          "countryCode": "333"
                                        },
                                        {
                                          "number": "444444",
                                          "cityCode": "4",
                                          "countryCode": "444"
                                        }
                                    ]
                                }
                                """;

                mockMvc.perform(put("/api/users/update_user_by_email/user@testupdate.com")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                                .andExpect(status().isCreated());

                verify(userService).updateByEmail(eq("user@testupdate.com"), any(UserDTO.class));
        }

        @Test
        void deleteUserById_returnsOk() throws Exception {
                UUID id = UUID.randomUUID();

                mockMvc.perform(delete("/api/users/delete_by_user_id/" + id))
                                .andExpect(status().isOk());

                verify(userService).deleteUserById(id);
        }


        @Test
        void deleteUserByEmail_returnsNoContent() throws Exception {
                mockMvc.perform(delete("/api/users/delete_by_user_email/user@testupdate.com"))
                                .andExpect(status().isNoContent());

                verify(userService).deleteUserByEmail("user@testupdate.com");
        }

        @Test
        void partialUpdateById_returnsUpdated() throws Exception {
                UUID id = UUID.randomUUID();

                UserResponseDTO dto = new UserResponseDTO();
                dto.setToken("patched");

                when(userService.partialUpdateById(eq(id), anyMap())).thenReturn(dto);

                String body = """
                                {"name":"User Test PARTIAL UPDATE"}
                                """;

                mockMvc.perform(patch("/api/users/partial_update_by_id/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                                .andExpect(status().isOk());

                verify(userService).partialUpdateById(eq(id), anyMap());
        }

        @Test
        void partialUpdateByEmail_returnsUpdated() throws Exception {
                UserResponseDTO dto = new UserResponseDTO();
                dto.setToken("token");

                when(userService.partialUpdateByEmail(eq("user@testupdate.com"), anyMap()))
                                .thenReturn(dto);

                String body = """
                                {"lastname":"User Test PARTIAL UPDATE"}
                                """;

                mockMvc.perform(patch("/api/users/partial_update_by_email/user@testupdate.com")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                                .andExpect(status().isOk());

                verify(userService).partialUpdateByEmail(eq("user@testupdate.com"), anyMap());
        }
}