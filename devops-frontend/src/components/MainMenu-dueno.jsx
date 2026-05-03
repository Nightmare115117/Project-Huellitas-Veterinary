import { useNavigate } from "react-router-dom";
import { Select, MenuItem, FormControl, InputLabel} from "@mui/material";
import { useEffect, useState } from "react";
import { getApiBaseHost } from '../Host';
import { LocalizationProvider, DatePicker, TimePicker } from '@mui/x-date-pickers';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';

import Swal from "sweetalert2";
import "sweetalert2/dist/sweetalert2.min.css";
import "../styles/MainMenu-dueno.css";

const TablaCitas = ({ citas }) => {
  if (!citas || !Array.isArray(citas)) {
    return <p style={{color: "black", opacity: 1}}>No hay citas registradas.</p>;
  } else if (citas.length === 0) {
    return <p style={{color: "black", opacity: 1}}>No hay citas registradas.</p>;
  } else {
    return (
      <div className="tabla-container">
        <table className="tabla-citas">
          <thead>
            <tr>
              <th>Fecha</th>
              <th>Estado</th>
              <th>Entrada Agendada</th>
              <th>Hora de Salida</th>
            </tr>
          </thead>
          <tbody>
            {citas.map((cita) => (
              <tr key={cita.idCita}>
                <td>{cita.fecha}</td>
                <td>
                  <span className={`estado ${
                    cita.estadoCita === 2 ? "completado" 
                    : cita.estadoCita === 0 ? "Asignacion Pendiente"
                    : cita.estadoCita === 1 ? "pendiente" 
                    : "cancelado"
                  }`}>
                    {cita.estadoCita === 2 ? "Completado" 
                    : cita.estadoCita === 1 ? "Pendiente" 
                    : cita.estadoCita === 0 ? "Asignacion Pendiente"
                    : "Cancelado"}
                  </span>
                </td>
                <td>{cita.entradaAgendada}</td>
                <td>{cita.estado_cita === 1 ? cita.horaSalida : "—"}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    );
  }
};

const TablaMascotas = ({mascotas}) => {
  if (!mascotas || !Array.isArray(mascotas)){
    return <p style={{color: "black", opacity: 1}}>No hay mascotas registradas</p>
  } else if (mascotas.length === 0) {
    return <p style={{color: "black", opacity: 1}}>No hay mascotas registradas</p>
  } else {
    return (
      <div className="tabla-container">
        <table className="tabla-mascotas">
          <thead>
            <tr>
              <th>Nombre</th>
              <th>Fecha de Nacimiento</th>
              <th>Peso</th>
              <th>Especie</th>
              <th>Raza</th>
            </tr>
          </thead>
          <tbody>
            {mascotas.map((mascota) => (
              <tr key={mascota.idMascota}>
                <td>{mascota.nombre}</td>
                <td>{mascota.fechaNacimiento}</td>
                <td>{mascota.peso} Kg</td>
                <td>{mascota.raza.especie.nombre}</td>
                <td>{mascota.raza.nombre}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    );
  }
};

const TablaTratamientos = ({tratamientos}) => {
  if (!tratamientos || !Array.isArray(tratamientos)) {
    return <p style={{color: "black", opacity: 1}}>No hay tratamientos en su historial</p>
  } else if (tratamientos.length === 0) {
    return <p style={{color: "black", opacity: 1}}></p>
  } else {
    return (
      <div className="tabla-container">
        <table className="tabla-tratamientos">
          <thead>
            <tr>
              <th>Medicamento</th>
              <th>Descripción</th>
              <th>Costo</th>
              <th>Estatus</th>
            </tr>
          </thead>
          <tbody>
            {tratamientos.map((tratamiento) => (
              <tr key={tratamiento.idTratamiento}>
                <td>{tratamiento.medicamento}</td>
                <td>{tratamiento.descripcion}</td>
                <td>{tratamiento.costo}</td>
                <td>
                  <span className={`estado ${
                    tratamiento.estatus === 1 ? "completado" 
                    : tratamiento.estatus === 100 ? "Asignacion Pendiente"
                    : tratamiento.estatus === 0 ? "pendiente" 
                    : "cancelado"
                  }`}>
                    {tratamiento.estatus === 0 ? "Activo"
                    : "Completado"}
                  </span>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    )
  }
};


function MainMenu() {
  const navigate = useNavigate();
  const [dueno, setDueno] = useState(null);
  const [activeItem, setActiveItem] = useState("");
  const [mascotas, setMascotas] = useState([]);
  const [selectedMascota, setSelectedMascota] = useState("");
  const [selectedMascotaCita, setSelectedMascotaCita] = useState("");
  const [ListaCitas, setCitas] = useState([]);
  const correo = sessionStorage.getItem("Usuario")?.replace(/^"|"$/g, "");
  const menuItems = ["Mascotas", "Citas", "Tratamientos"];
  const [noMascotas, setNoMascota] = useState(0);
  const [noCitas, setNoCita] = useState(0);
  const [isLeaving, setIsLeaving] = useState(false);
  const mascotaSeleccionadaObj = mascotas.find((m) => m.idMascota === Number(selectedMascota));
  const [tratamientos, setTratamientos] = useState([]);
  const [noTratamientos, setNoTratamientos] = useState("");
  const [mostrarFormCita, setMostrarFormCita] = useState(false);
  const [fechaCita, setFechaCita] = useState(null);
  const [horaCita, setHoraCita] = useState(null);
  const [refreshCitas, setRefreshCitas] = useState(0);
  const [isSubmitting, setIsSubmitting] = useState(false);

  const handleLogout = () => {
    Swal.fire({
      title: "Cerrar Sesión",
      text: "¿Estás seguro de querer cerrar sesión?",
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#3085d6",
      cancelButtonColor: "#d33",
    }).then((result) => {
      if (result.isConfirmed) {
        navigate("/");
        sessionStorage.clear();
        Swal.fire({ title: "Sesión cerrada con Exito", text: "Hasta pronto", icon: "success" });
      }
    });
  };

  useEffect(() => {
    if (!correo) {
      Swal.fire({
        title: "Sesión Expirada",
        text: "Por favor de iniciar sesion nuevamente",
        icon: "warning", 
        timer: 3000,
        timerProgressBar: true,
        showConfirmButton: false  
      }).then(() => {
        navigate("/");
      });
    }
  }, [correo]);

  useEffect(() => {
    if (!correo) return;
    fetch(`http://${getApiBaseHost()}:8080/usuario/getNombre`, {
      method: "POST",
      headers: { "Content-Type": "text/plain" },
      body: correo,
    })
      .then((res) => res.json())
      .then((data) => setDueno(data))
      .catch((err) => console.error(err));
  }, [correo]);

  useEffect(() => {
    if (!correo) return;
      fetch(`http://${getApiBaseHost()}:8080/mascota/usuario/${correo}`)
      .then(res => res.json())
      .then(data => setNoMascota(data))
      .catch(err => console.error(err));
  }, [correo]);

  useEffect(() => {
    if (!correo) return;
      fetch(`http://${getApiBaseHost()}:8080/cita/usuario/${correo}`)
      .then(res => res.json())
      .then(data => setNoCita(data))
      .catch(err => console.error(err));
  }, [correo]);

  useEffect(() => {
    if (!correo) return;
    fetch(`http://${getApiBaseHost()}:8080/tratamiento/${encodeURIComponent(correo)}`)
    .then(res => res.json())
    .then(data => setNoTratamientos(data))
    .catch(err => console.error(err));
  }, [correo]);

  const changeMenu = (item) => {
    if (item === activeItem) return;

    setIsLeaving(true);

    setTimeout(() => {
      setActiveItem(item);
      setIsLeaving(false);
    }, 250);
  };

  useEffect(() => {
    if (!correo) return;
  
    const url = mascotaSeleccionadaObj
      ? `http://${getApiBaseHost()}:8080/cita/${encodeURIComponent(correo)}/${mascotaSeleccionadaObj.idMascota}`
      : `http://${getApiBaseHost()}:8080/cita/${correo}`;
  
    fetch(url)
      .then(async (res) => {
        if (!res.ok) {
          const text = await res.text();
          throw new Error(text);
        }
        return res.json();
      })
      .then((data) => setCitas(data))
      .catch((err) => console.error("Error en cita API:", err));
  
  }, [correo, mascotaSeleccionadaObj, refreshCitas]);

  useEffect(() => {
    if (!correo) return;
    fetch(`http://${getApiBaseHost()}:8080/mascota/${correo}`)
    .then(res => res.json())
    .then(data => setMascotas(data))
    .catch((err => console.error(err)));
  }, [correo])

  useEffect(() => {
    if (!ListaCitas || ListaCitas === 0) return;

    const hoy = new Date();

    if (ListaCitas && ListaCitas.length > 0) {
      
      ListaCitas.forEach(element => {
        if (element.estadoCita === 1) {
          
          const fechaCitaHoy = new Date(element.fecha);
          const diferenciaDias = Math.ceil((fechaCitaHoy - hoy) / (1000 * 60 * 60 * 24));

          const keyRecordatorio = `recordatorio_${element.idCita}`;
          const yaSeAviso = localStorage.getItem(keyRecordatorio);

          if (diferenciaDias === 1 && !yaSeAviso) {
            Swal.fire({
              title: "Recordatorio de cita",
              text: `Tienes una cita mañana ${element.fecha} a las ${element.entradaAgendada}`,
              icon: "info",
              confirmButtonColor: "#3085d6",
              confirmButtonText: "Entendido"
            }).then (() => {
              localStorage.setItem(keyRecordatorio, "true");
            });
          }
        }
      });
    }
  }, [ListaCitas])

  useEffect(() => {
    if (!correo) return;
    if (!mascotaSeleccionadaObj) {
      fetch(`http://${getApiBaseHost()}:8080/tratamiento/usuario/${encodeURIComponent(correo)}`)
      .then(res => res.json())
      .then(data => setTratamientos(data))
      .catch(err => console.log(err));
     } else {
      fetch(`http://${getApiBaseHost()}:8080/tratamiento/usuario/${encodeURIComponent(correo)}/${mascotaSeleccionadaObj.idMascota}`)
      .then(res => res.json())
      .then(data => setTratamientos(data))
      .catch(err => console.error(err));
    }
  }, [correo, mascotaSeleccionadaObj]);

  async function crearCita() {
    // Validar que los campos no estén vacíos
    if (!selectedMascotaCita || !fechaCita || !horaCita) {
      Swal.fire({
        title: "Campos incompletos",
        text: "Por favor selecciona una mascota, fecha y hora para la cita.",
        icon: "warning"
      });
      return;
    }

    if (isSubmitting) return;
    setIsSubmitting(true);
    try {
    // Formatear fecha y hora para el backend
    const fechaFormateada = fechaCita.format('YYYY-MM-DD');      // ej: "2026-05-02"
    const horaFormateada = horaCita.format('HH:mm:ss');          // ej: "20:00:00"
  
    const response = await fetch(`http://${getApiBaseHost()}:8080/cita/crear`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        correoCliente: correo,
        idMascota: selectedMascotaCita,          // ← asegúrate de usar el estado correcto (el del modal)
        fecha: fechaFormateada,
        entradaAgendada: horaFormateada,
      })
    });
    const data = await response.json();
    console.log(data);
    if (response.ok) {
      Swal.fire({
        title: "Cita creada con éxito",
        text: "La cita ha sido creada con éxito",
        icon: "success"
      });
      setMostrarFormCita(false);
      setFechaCita(null);
      setHoraCita(null);
      setSelectedMascotaCita("");
      setRefreshCitas(prev => prev + 1);
      // Recargar las citas para mostrar la nueva
      // Puedes llamar nuevamente al fetch de citas o simplemente recargar la lista
      // Por simplicidad, recarga la página o vuelve a obtener las citas:
      //window.location.reload(); // o mejor: dispara un useEffect que dependa de una variable de recarga
    } else {
      Swal.fire({
        title: "Error al crear la cita",
        text: data.message || "Por favor, intente nuevamente",
        icon: "error"
      });
      setMostrarFormCita(false);
      setFechaCita(null);
      setHoraCita(null);
      setSelectedMascotaCita("");
    }
    } catch (error) {
      console.error("Error al crear la cita:", error);
      Swal.fire({
        title: "Error al crear la cita",
        text: error.message || "Por favor, intente nuevamente",
        icon: "error"
      });
    } finally {
      setIsSubmitting(false);
    }
  }

  return (
    <>
      <div className="main-menu" style={{ display: "flex" }}>

        <div className={`sidebar primary ${dueno ? "active" : ""}`}>
          <h2 style={{color: "white"}}>Menú Principal</h2>
          <h3 style={{color: "white"}}>Bienvenido, {dueno ? dueno.nombre : "Dueño de Mascota"}</h3>
          <nav className="nav-links">
            {menuItems.map((item) => (
              <div
                key={item}
                className={`menu-item ${activeItem === item ? "active" : ""}`}
                onClick={() => {changeMenu(item); setSelectedMascota("")}}
                style={{ cursor: "pointer", margin: "5px 0" }}>
                {item}
              </div>
            ))}
          </nav>
          <button type="button" onClick={handleLogout} className="btn">Cerrar Sesión</button>
        </div>
        <div className={`sidebar secondary ${activeItem}`}>

        {activeItem === "" && (
          <>
            <h2 style={{fontWeight: "bold", color: "black"}}>Datos Generales</h2>

            <div className="slydes-wrapper">
              <div className={`slydes ${isLeaving ? "slide-out" : ""}`}>
                <span className="icon">🐕</span>

                <div className="card-text">
                  <span style={{fontWeight: "bold", fontSize: "30px"}}>&nbsp;Mascotas</span>
                  <span className="subtitle">
                  &nbsp;Usted tiene {noMascotas} {noMascotas === 1 ? "Mascota" : "Mascotas"}
                  </span>
                </div>
              </div>

              <div className={`slydes ${isLeaving ? "slide-out" : ""}`}>
                <span className="icon">🗓️</span>

                <div className="card-text">
                  <span style={{fontWeight: "bold", fontSize: "30px"}}>&nbsp;Citas</span>
                  <span className="subtitle">
                  &nbsp;Usted tiene {noCitas} {noCitas === 1 ? "Cita" : "Citas"} pendientes
                  </span>
                </div>
              </div>

              <div className={`slydes ${isLeaving ? "slide-out" : ""}`}>
                <span className="icon">💉</span>

                <div className="card-text">
                  <span style={{fontWeight: "bold", fontSize: "30px"}}>&nbsp;Tratamientos</span>
                  <span className="subtitle">
                  &nbsp;Usted tiene {noTratamientos} {noTratamientos === 1 ? "Tratamiento" : "Tratamientos"}
                  </span>
                </div>
              </div>
            </div>
          </>
        )}


        {activeItem === "Mascotas" && (
          <div className={isLeaving ? "slide-out" : "slide-in"}>
            <h2 style={{fontWeight: "bold", color: "black"}}>Datos de Mascotas</h2>
            <TablaMascotas mascotas={mascotas}></TablaMascotas>
          </div>
        )}

        {activeItem === "Citas" && (
          <div className={isLeaving ? "slide-out" : "slide-in"}>
            <h2 style={{fontWeight: "bold", color: "black"}}>Datos de Citas</h2> 
            <div style={{ display: "flex", alignItems: "center", gap: "10px" }}>
              <FormControl fullWidth sx={{ m: 1, minWidth: 200 }}>
                  <InputLabel id="select-mascota-label">Filtrar por Mascota</InputLabel>
                  <Select
                      labelId="select-mascota-label"
                      value={selectedMascota}
                      label="Filtrar por Mascota"
                      onChange={(e) => setSelectedMascota(e.target.value)}
                  >
                      <MenuItem value=""><em>Todas las mascotas</em></MenuItem>
                      {mascotas.map((m) => (
                          <MenuItem key={m.idMascota} value={m.idMascota}>{m.nombre}</MenuItem>
                      ))}
                  </Select>
              </FormControl>
              <button className="botonCrear" onClick={() => setMostrarFormCita(true)}>Crear Cita</button>
          </div>
            <TablaCitas citas={ListaCitas}></TablaCitas>
          </div>
        )}

        {activeItem === "Tratamientos" && (
          <div className={isLeaving ? "slide-out" : "slide-in"}>
            <h2 style={{fontWeight: "bold", color: "black"}}>Datos de Tratamientos</h2>
            <FormControl fullWidth sx={{ m: 1, minWidth: 200 }}>
              <InputLabel id="select-mascota-label">Filtrar por Mascota</InputLabel>
              <Select
                labelId="select-mascota-label"
                value={selectedMascota}
                label="Filtrar por Mascota" // Esto es vital para que el borde no corte el texto
                onChange={(e) => setSelectedMascota(e.target.value)}
              >
                <MenuItem value=""><em>Todas las mascotas</em></MenuItem>
                {mascotas.map((m) => (
                  <MenuItem key={m.idMascota} value={m.idMascota}>{m.nombre}</MenuItem>
                ))}
              </Select>
            </FormControl>
            <TablaTratamientos tratamientos={tratamientos}></TablaTratamientos>
          </div>
        )}

        {activeItem != "" && (
          <>
          <p></p>
          <button onClick={() => {setSelectedMascota(""); setActiveItem("")}} className="btn">Volver</button>
          </>
        )} 
        </div>
      </div>
      
      {mostrarFormCita && (
        <div style={{
            position: "fixed",
            top: 0, left: 0,
            width: "100%", height: "100%",
            backgroundColor: "rgba(0,0,0,0.5)",
            display: "flex",
            justifyContent: "center",
            alignItems: "center",
            zIndex: 1000
        }}>
            <div style={{
                backgroundColor: "white",
                padding: "30px",
                borderRadius: "10px",
                width: "400px"
            }}>
                <h3>Crear Cita</h3>
                <FormControl fullWidth sx={{ m: 1, minWidth: 200 }}>
                  <InputLabel id="select-mascota-label">Crear Cita para:</InputLabel>
                  <Select
                      labelId="select-mascota-label"
                      value={selectedMascotaCita}
                      label="Filtrar por Mascota"
                      onChange={(e) => setSelectedMascotaCita(e.target.value)}
                  >
                      <MenuItem value=""><em>Seleccione uno</em></MenuItem>
                      {mascotas.map((m) => (
                          <MenuItem key={m.idMascota} value={m.idMascota}>{m.nombre}</MenuItem>
                      ))}
                  </Select>
              
                  <LocalizationProvider dateAdapter={AdapterDayjs}>
                      <p></p>
                          <DatePicker
                              label="Fecha de la cita"
                              value={fechaCita}
                              onChange={(newValue) => setFechaCita(newValue)}
                          />
                      <p></p>
                          <TimePicker
                              label="Hora de entrada"
                              value={horaCita}
                              onChange={(newValue) => setHoraCita(newValue)}
                          />
                      <p></p>
                  </LocalizationProvider>
                </FormControl>

                <div style={{ display: "flex", gap: "10px", marginTop: "20px" }}>
                    <button onClick={() => {setMostrarFormCita(false); setFechaCita(null); setHoraCita(null); setSelectedMascotaCita("")}}>Cancelar</button>
                    <button 
                      type="button"
                      onClick={crearCita} 
                      disabled={isSubmitting}
                    >
                      {isSubmitting ? "Guardando..." : "Guardar"}
                    </button>
                </div>
            </div>
        </div>
    )}
  </>
  );
}

export default MainMenu;