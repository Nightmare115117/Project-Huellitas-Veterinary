package com.Proyect_DevOps.consultaMedica.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

/**
 * Entidad JPA que representa un tratamiento médico veterinario.
 * Contiene el nombre del medicamento, dosis/descripción, costo y estatus de cumplimiento.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@Entity
@Table(name = "tratamientos")
public class TratamientoModel {

    /**
     * Identificador único autoincremental del tratamiento.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idTratamiento;

    /**
     * Nombre o tipo de fármaco recetado.
     */
    private String medicamento;

    /**
     * Instrucciones o pautas de dosificación del tratamiento.
     */
    private String descripcion;

    /**
     * Costo económico asociado al tratamiento.
     */
    private double costo;

    /**
     * Estado del tratamiento (0=Pendiente/Incompleto, 1=Concluido).
     */
    private int estatus;

    /**
     * Consultas médicas en las que se ha formulado este tratamiento.
     */
    @ManyToMany(mappedBy = "tratamientos")
    private List<ConsultaMedicaModel> consultas = new ArrayList<>();

    /**
     * Constructor con atributos principales del tratamiento.
     *
     * @param medicamento Nombre del fármaco.
     * @param descripcion Detalle de dosis o frecuencia.
     * @param costo Costo monetario.
     * @param estatus Estado del tratamiento (0 o 1).
     */
    public TratamientoModel(String medicamento, String descripcion, double costo, int estatus){
        this.medicamento = medicamento;
        this.descripcion = descripcion;
        this.costo = costo;
        this.estatus = estatus;
    }

    /**
     * Constructor por defecto requerido por JPA.
     */
    public TratamientoModel(){
    }

    /**
     * Obtiene el nombre del medicamento.
     *
     * @return Medicamento recetado.
     */
    public String getMedicamento(){
        return medicamento;
    }

    /**
     * Establece el medicamento del tratamiento.
     *
     * @param medicamento Nombre a asignar.
     */
    public void setMedicamento(String medicamento){
        this.medicamento = medicamento;
    }

    /**
     * Obtiene la descripción o pauta del tratamiento.
     *
     * @return Descripción de la posología.
     */
    public String getDescripcion(){
        return descripcion;
    }

    /**
     * Establece la descripción del tratamiento.
     *
     * @param descripcion Pautas de dosificación.
     */
    public void setDescripcion(String descripcion){
        this.descripcion = descripcion;
    }

    /**
     * Obtiene el costo del tratamiento.
     *
     * @return Costo numérico.
     */
    public double getCosto(){
        return costo;
    }

    /**
     * Establece el costo del tratamiento.
     *
     * @param costo Importe económico.
     */
    public void setCosto(double costo){
        this.costo = costo;
    }

    /**
     * Obtiene las consultas médicas donde se prescribió el tratamiento.
     *
     * @return Lista de {@link ConsultaMedicaModel}.
     */
    public List<ConsultaMedicaModel> getListaConsultas(){
        return consultas;
    }

    /**
     * Asocia la lista de consultas médicas al tratamiento.
     *
     * @param consultas Lista de consultas médicas.
     */
    public void setListaConsultas(List<ConsultaMedicaModel> consultas){
        this.consultas = consultas;
    }

    /**
     * Obtiene el estatus del tratamiento.
     *
     * @return Estatus numérico (0=Incompleto, 1=Completado).
     */
    public int getStatus(){
        return estatus;
    }

    /**
     * Establece el estatus del tratamiento.
     *
     * @param estatus Código de estatus.
     */
    public void setStatus(int estatus){
        this.estatus = estatus;
    }

    /**
     * Representación textual del tratamiento.
     *
     * @return Cadena formateada del tratamiento.
     */
    @Override
    public String toString(){
        return "idTratamiento=" + idTratamiento + ", medicamento=" + medicamento + 
        ", descripcion=" + descripcion + ", costo=" + costo + ", estatus=" + estatus;
    }

    /**
     * Obtiene el identificador del tratamiento.
     *
     * @return Identificador numérico.
     */
    public int getIdTratamiento() {
        return idTratamiento;
    }

    /**
     * Establece el identificador del tratamiento.
     *
     * @param idTratamiento Identificador numérico único.
     */
    public void setIdTratamiento(int idTratamiento) {
        this.idTratamiento = idTratamiento;
    }
}
