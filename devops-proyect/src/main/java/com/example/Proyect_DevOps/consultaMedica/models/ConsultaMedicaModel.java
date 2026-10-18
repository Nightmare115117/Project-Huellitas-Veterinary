package com.example.Proyect_DevOps.consultaMedica.models;

import java.util.ArrayList;
import java.util.List;

import com.example.Proyect_DevOps.citas.models.CitaModel;
import com.example.Proyect_DevOps.mascotas.models.MascotaModel;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * Entidad JPA que representa la consulta médica veterinaria realizada a una mascota.
 * Almacena el diagnóstico clínico, motivo de visita, recomendaciones médicas, peso registrado,
 * la cita vinculada y los tratamientos indicados.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@Entity
@Table(name = "Consulta-Medica")
public class ConsultaMedicaModel {

    /**
     * Identificador único autoincremental de la consulta médica.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idConsulta;

    /**
     * Cita médica previamente agendada asociada a esta consulta.
     */
    @OneToOne
    private CitaModel cita;

    /**
     * Lista de tratamientos médicos prescritos durante la consulta.
     */
    @ManyToMany
    @JoinTable(name = "Consulta-Tratamiento", joinColumns = @JoinColumn(name = "idCita"),
    inverseJoinColumns = @JoinColumn(name = "idTratamiento"))
    private List<TratamientoModel> tratamientos = new ArrayList<>();

    /**
     * Motivo de la visita manifestado por el dueño o cuidador.
     */
    private String motivo;

    /**
     * Diagnóstico emitido por el médico veterinario.
     */
    private String diagnostico;

    /**
     * Recomendaciones, cuidados o cuidados posoperatorios prescritos.
     */
    private String recomendaciones;

    /**
     * Peso en kilogramos de la mascota registrado durante la consulta.
     */
    private double peso;
    
    /**
     * Mascota que recibió la consulta médica.
     */
    @ManyToOne
    @JoinColumn(name = "idMascota", nullable = false)
    private MascotaModel idMascota;

    /**
     * Constructor con los datos principales de la consulta clínica.
     *
     * @param cita Cita agendada vinculada.
     * @param motivo Causa o síntoma de la consulta.
     * @param diagnostico Dictamen médico veterinario.
     * @param recomendaciones Instrucciones y cuidados.
     * @param idMascota Mascota examinada.
     */
    public ConsultaMedicaModel(CitaModel cita, String motivo, 
        String diagnostico, String recomendaciones, MascotaModel idMascota){

        this.cita = cita;
        this.motivo = motivo;
        this.diagnostico = diagnostico;
        this.recomendaciones = recomendaciones;
        this.idMascota = idMascota;
    }

    /**
     * Constructor por defecto requerido por JPA.
     */
    public ConsultaMedicaModel(){    
    }

    /**
     * Obtiene el identificador de la consulta.
     *
     * @return Identificador numérico.
     */
    public int getId(){
        return idConsulta;
    }
 
    /**
     * Establece el identificador de la consulta.
     *
     * @param idConsulta Identificador a asignar.
     */
    public void setId(int idConsulta){
        this.idConsulta = idConsulta;
    }

    /**
     * Obtiene el identificador de la consulta médica.
     *
     * @return Identificador numérico.
     */
    public int getIdConsulta(){
        return idConsulta;
    }

    /**
     * Establece el identificador de la consulta médica.
     *
     * @param idConsulta Identificador a asignar.
     */
    public void setIdConsulta(int idConsulta){
        this.idConsulta = idConsulta;
    }

    /**
     * Obtiene el motivo de consulta.
     *
     * @return Motivo o descripción inicial.
     */
    public String getMotivo(){
        return motivo;
    }

    /**
     * Establece el motivo de la consulta médica.
     *
     * @param motivo Texto explicativo del motivo.
     */
    public void setMotivo(String motivo){
        this.motivo = motivo;
    }

    /**
     * Obtiene el diagnóstico clínico veterinario.
     *
     * @return Diagnóstico médico.
     */
    public String getDiagnostivo(){
        return diagnostico;
    }

    /**
     * Asigna el diagnóstico de la consulta.
     *
     * @param diagnostico Diagnóstico veterinario.
     */
    public void setDiagnostico(String diagnostico){
        this.diagnostico = diagnostico;
    }

    /**
     * Obtiene las recomendaciones de cuidado.
     *
     * @return Recomendaciones clínicas.
     */
    public String getRecomendaciones(){
        return recomendaciones;
    }

    /**
     * Establece las recomendaciones veterinarias.
     *
     * @param recomendaciones Texto con las instrucciones de cuidado.
     */
    public void setRecomendaciones(String recomendaciones){
        this.recomendaciones = recomendaciones;
    }

    /**
     * Obtiene la lista de tratamientos asociados a esta consulta.
     *
     * @return Lista de {@link TratamientoModel}.
     */
    public List<TratamientoModel> getListaTratamientos(){
        return tratamientos;
    }

    /**
     * Asigna la lista de tratamientos de la consulta médica.
     *
     * @param tratamientos Lista de tratamientos prescritos.
     */
    public void setListaTratamientos(List<TratamientoModel> tratamientos){
        this.tratamientos = tratamientos;
    }

    /**
     * Obtiene la cita asociada.
     *
     * @return Entidad {@link CitaModel}.
     */
    public CitaModel getCita() {
        return cita;
    }

    /**
     * Asigna la cita médica vinculada.
     *
     * @param cita Cita médica a asociar.
     */
    public void setCita(CitaModel cita) {
        this.cita = cita;
    }

    /**
     * Representación en cadena de la consulta médica.
     *
     * @return Cadena con información relevante de la consulta.
     */
    @Override
    public String toString(){
        return "idConsulta=" + idConsulta + ", Motivo=" + motivo + 
        ", Diagnostico=" + diagnostico + ", Recomendaciones=" + recomendaciones + ", cita=" + cita;
    }

    /**
     * Obtiene la mascota atendida.
     *
     * @return Entidad {@link MascotaModel}.
     */
    public MascotaModel getIdMascota() {
        return idMascota;
    }

    /**
     * Asigna la mascota a la consulta médica.
     *
     * @param idMascota Mascota atendida.
     */
    public void setIdMascota(MascotaModel idMascota) {
        this.idMascota = idMascota;
    }

    /**
     * Obtiene el peso de la mascota registrado en la consulta.
     *
     * @return Peso en kilogramos.
     */
    public double getPeso() {
        return peso;
    }

    /**
     * Asigna el peso de la mascota para la consulta.
     *
     * @param peso Peso en kilogramos.
     */
    public void setPeso(double peso) {
        this.peso = peso;
    }
}
