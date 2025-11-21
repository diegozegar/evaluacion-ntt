package com.evaluacion.msvc.users.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "correo"))
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String nombre;

    @Column(unique = true)
    private String correo;

    private String contraseña;

    private boolean active;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Phone> telefonos = new ArrayList<>();

    @Column(length = 500)
    private String token;

    // NUEVOS CAMPOS
    @Column(updatable = false)
    private LocalDateTime creado;

    private LocalDateTime modificado;

    private LocalDateTime ultimoLogin;
    
    @PrePersist
    public void prePersist() {
        
        this.creado = LocalDateTime.now().withNano(0);
        this.modificado = LocalDateTime.now().withNano(0);
        this.ultimoLogin = LocalDateTime.now().withNano(0);
    }

    @PreUpdate
    public void preUpdate() {
        this.modificado = LocalDateTime.now().withNano(0);
    }
}
