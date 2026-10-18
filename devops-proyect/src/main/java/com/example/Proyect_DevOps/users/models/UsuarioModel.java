package com.example.Proyect_DevOps.users.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Entidad JPA que representa a un usuario del sistema (personal médico, administrativo o cliente).
 * Implementa cifrado en reposo para el correo electrónico (AES-256), una huella de búsqueda (HMAC-SHA256)
 * y almacenamiento de contraseñas con hash criptográfico BCrypt.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@Entity
@Table(name = "usuarios")
public class UsuarioModel {

    /**
     * Identificador único autoincremental del usuario.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idUsuario;

    /**
     * Nombre de pila del usuario.
     */
    private String nombre;

    /**
     * Primer apellido (apellido paterno).
     */
    private String paterno;

    /**
     * Segundo apellido (apellido materno).
     */
    private String materno;

    /**
     * Correo electrónico cifrado simétricamente con AES-256 en reposo.
     */
    @Column(nullable = false, unique = true)
    private String correo;

    /**
     * Huella digital criptográfica (HMAC-SHA256) del correo electrónico para búsquedas exactas e indexación.
     */
    @Column(nullable = false, unique = true)
    private String correoHMAC;

    /**
     * Contraseña protegida mediante algoritmo de derivación de claves BCrypt.
     */
    @Column(nullable = false)
    private String contraseña;

    /**
     * Rol asignado al usuario que define sus permisos y acceso.
     */
    @ManyToOne
    @JoinColumn(name = "idRol")
    private RolModel rol;
    
    /**
     * Constructor completo para registrar un usuario.
     *
     * @param nombre Nombre del usuario.
     * @param paterno Apellido paterno.
     * @param materno Apellido materno.
     * @param correo Correo electrónico en claro (antes del cifrado).
     * @param contraseña Contraseña del usuario.
     * @param rol Rol que se asignará al usuario.
     */
    public UsuarioModel(String nombre, String paterno, String materno, String correo, String contraseña, RolModel rol) {
        this.nombre = nombre;
        this.paterno = paterno;
        this.materno = materno;
        this.correo = correo;
        this.contraseña = contraseña;
        this.rol = rol;
    }

    /**
     * Constructor por defecto requerido por JPA.
     */
    public UsuarioModel() {
    }

    /**
     * Obtiene el identificador del usuario.
     *
     * @return Identificador numérico único.
     */
    public int getIdUsuario() {
        return idUsuario;
    }
    
    /**
     * Establece el identificador del usuario.
     *
     * @param idUsuario Identificador numérico a asignar.
     */
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    /**
     * Obtiene el nombre del usuario.
     *
     * @return Nombre de pila.
     */
    public String getNombre() {
        return nombre;
    }
    
    /**
     * Establece el nombre del usuario.
     *
     * @param nombre Nombre de pila a asignar.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el apellido paterno.
     *
     * @return Apellido paterno.
     */
    public String getPaterno() {
        return paterno;
    }
    
    /**
     * Establece el apellido paterno.
     *
     * @param paterno Apellido paterno a asignar.
     */
    public void setPaterno(String paterno) {
        this.paterno = paterno;
    }

    /**
     * Obtiene el apellido materno.
     *
     * @return Apellido materno.
     */
    public String getMaterno() {
        return materno;
    }
    
    /**
     * Establece el apellido materno.
     *
     * @param materno Apellido materno a asignar.
     */
    public void setMaterno(String materno) {
        this.materno = materno;
    }

    /**
     * Obtiene el correo electrónico del usuario.
     *
     * @return Correo electrónico (cifrado en base de datos o descifrado en memoria).
     */
    public String getCorreo() {
        return correo;
    }
    
    /**
     * Establece el correo electrónico del usuario.
     *
     * @param correo Correo electrónico a asignar.
     */
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    /**
     * Obtiene la huella digital criptográfica HMAC del correo.
     *
     * @return Huella HMAC en formato Base64.
     */
    public String getCorreoHMAC() {
        return correoHMAC;
    }

    /**
     * Establece la huella digital criptográfica HMAC del correo.
     *
     * @param correoHMAC Huella digital calculada con HMAC-SHA256.
     */
    public void setCorreoHMAC(String correoHMAC) {
        this.correoHMAC = correoHMAC;
    }

    /**
     * Obtiene el hash BCrypt de la contraseña del usuario.
     *
     * @return Hash de la contraseña.
     */
    public String getContraseña() {
        return contraseña;
    }
    
    /**
     * Establece la contraseña (o su hash codificado) del usuario.
     *
     * @param contraseña Contraseña o hash a asignar.
     */
    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    /**
     * Obtiene el rol asignado al usuario.
     *
     * @return Instancia de {@link RolModel}.
     */
    public RolModel getRol() {
        return rol;
    }
    
    /**
     * Asocia un rol al usuario.
     *
     * @param rol Rol a vincular.
     */
    public void setRol(RolModel rol) {
        this.rol = rol;
    }
}
