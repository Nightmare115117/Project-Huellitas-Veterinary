package com.Proyect_DevOps.sucursales.models;

import com.Proyect_DevOps.users.models.UsuarioModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * Entidad JPA que representa al gestor o encargado administrativo de una sucursal veterinaria.
 * Contiene RFC, salario y la vinculación uno a uno con la cuenta de usuario del gestor.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@Entity
@Table(name = "gestores")
public class GestorModel {
    
    /**
     * Identificador único autoincremental del gestor.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idGestor;
    
    /**
     * Registro Federal de Contribuyentes (RFC) único del gestor.
     */
    @Column(unique = true)
    private String rfc;

    /**
     * Salario o remuneración económica asignada al gestor.
     */
    private double salario;

    /**
     * Cuenta de usuario {@link UsuarioModel} vinculada al gestor.
     */
    @OneToOne
    @JoinColumn(name = "idUsuario")
    private UsuarioModel usuario;

    /**
     * Constructor con RFC, salario y usuario asociado.
     *
     * @param rfc Registro Federal de Contribuyentes.
     * @param salario Salario asignado.
     * @param usuario Usuario del sistema vinculado.
     */
    public GestorModel(String rfc, double salario, UsuarioModel usuario) {
        this.rfc = rfc;
        this.salario = salario;
        this.usuario = usuario;
    }

    /**
     * Constructor por defecto requerido por JPA.
     */
    public GestorModel() {
    }
    
    /**
     * Obtiene el identificador del gestor.
     *
     * @return Identificador numérico único.
     */
    public int getIdGestor() {
        return idGestor;
    }
    
    /**
     * Establece el identificador del gestor.
     *
     * @param idGestor Identificador a asignar.
     */
    public void setIdGestor(int idGestor) {
        this.idGestor = idGestor;
    }
    
    /**
     * Obtiene el RFC del gestor.
     *
     * @return Cadena con el RFC.
     */
    public String getRfc() {
        return rfc;
    }
    
    /**
     * Establece el RFC del gestor.
     *
     * @param rfc RFC a asignar.
     */
    public void setRfc(String rfc) {
        this.rfc = rfc;
    }
    
    /**
     * Obtiene el salario del gestor.
     *
     * @return Salario numérico.
     */
    public double getSalario() {
        return salario;
    }
    
    /**
     * Establece el salario del gestor.
     *
     * @param salario Salario a asignar.
     */
    public void setSalario(double salario) {
        this.salario = salario;
    }
    
    /**
     * Obtiene el usuario del gestor.
     *
     * @return Entidad {@link UsuarioModel}.
     */
    public UsuarioModel getUsuario() {
        return usuario;
    }
    
    /**
     * Asocia la cuenta de usuario al gestor.
     *
     * @param usuario Usuario a vincular.
     */
    public void setUsuario(UsuarioModel usuario) {
        this.usuario = usuario;
    }
    
    /**
     * Representación textual del gestor.
     *
     * @return Cadena formateada del gestor.
     */
    @Override
    public String toString() {
        return "idGestor=" + idGestor + ", rfc=" + rfc + ", salario=" + salario + ", usuario=" + (usuario != null ? usuario.getIdUsuario() : "null");
    }
}
