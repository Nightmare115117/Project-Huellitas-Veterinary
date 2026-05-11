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

@Entity
@Table(name = "Consulta-Medica")
public class ConsultaMedicaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idConsulta;

    @OneToOne
    private CitaModel cita;

    @ManyToMany
    @JoinTable(name = "Consulta-Tratamiento", joinColumns = @JoinColumn(name = "idCita"),
    inverseJoinColumns = @JoinColumn(name = "idTratamiento"))
    private List<TratamientoModel> tratamientos = new ArrayList<>();

    private String motivo;
    private String diagnostico;
    private String recomendaciones;
    private double peso;
    
    @ManyToOne
    @JoinColumn(name = "idMascota", nullable = false)
    private MascotaModel idMascota;

    public ConsultaMedicaModel(CitaModel cita, String motivo, 
        String diagnostico, String recomendaciones, MascotaModel idMascota){

        this.cita = cita;
        this.motivo = motivo;
        this.diagnostico = diagnostico;
        this.recomendaciones = recomendaciones;
        this.idMascota = idMascota;
    }

    public ConsultaMedicaModel(){    
    }

    public int getId(){
        return idConsulta;
    }
 
    public void setId(int idConsulta){
        this.idConsulta = idConsulta;
    }

    public String getMotivo(){
        return motivo;
    }

    public void setMotivo(String motivo){
        this.motivo = motivo;
    }

    public String getDiagnostivo(){
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico){
        this.diagnostico = diagnostico;
    }

    public String getRecomendaciones(){
        return recomendaciones;
    }

    public void setRecomendaciones(String recomendaciones){
        this.recomendaciones = recomendaciones;
    }

    public List<TratamientoModel> getListaTratamientos(){
        return tratamientos;
    }

    public void setListaTratamientos(List<TratamientoModel> tratamientos){
        this.tratamientos = tratamientos;
    }

    public CitaModel getCita() {
        return cita;
    }

    public void setCita(CitaModel cita) {
        this.cita = cita;
    }

    @Override
    public String toString(){
        return "idConsulta=" + idConsulta + ", Motivo=" + motivo + 
        ", Diagnostico=" + diagnostico + ", Recomendaciones=" + recomendaciones + ", cita=" + cita;
    }

    public MascotaModel getIdMascota() {
        return idMascota;
    }

    public void setIdMascota(MascotaModel idMascota) {
        this.idMascota = idMascota;
    }
}
