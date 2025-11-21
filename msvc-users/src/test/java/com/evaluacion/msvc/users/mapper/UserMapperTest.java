package com.evaluacion.msvc.users.mapper;

import com.evaluacion.msvc.users.DTO.PhoneDTO;
import com.evaluacion.msvc.users.DTO.UserDTO;
import com.evaluacion.msvc.users.entities.Phone;
import com.evaluacion.msvc.users.entities.User;
import com.evaluacion.msvc.users.mapper.UserMapper;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private final UserMapper mapper = new UserMapper();

    @Test
    void toEntity_mapsPhonesAndUser() {
        UserDTO dto = new UserDTO();
        dto.setNombre("Diego");
        dto.setCorreo("diego@test.com");
        dto.setContraseña("pass");
        PhoneDTO p = new PhoneDTO();
        p.setNumero("123");
        p.setCodigoCiudad("1");
        p.setCodigoPais("57");
        dto.setTelefonos(List.of(p));

        User user = mapper.toEntity(dto);
        assertEquals("Diego", user.getNombre());
        assertEquals(1, user.getTelefonos().size());
        Phone phone = user.getTelefonos().get(0);
        assertEquals("123", phone.getNumero());
        // la relación inversa debe estar seteada
        assertSame(user, phone.getUser());
    }
}
