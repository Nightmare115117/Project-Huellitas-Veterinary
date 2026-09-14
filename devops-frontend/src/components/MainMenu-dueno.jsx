import { useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import "../styles/MainMenu-dueno.css";

const API_BASE = import.meta.env.DEV ? "/api" : `${import.meta.env.BASE_URL}api`;

const formatDate = (value) => {
    if (!value) return "--";

    const date = new Date(`${value}T00:00:00`);
    if (Number.isNaN(date.getTime())) {
        return value;
    }

    return date.toLocaleDateString("es-MX", {
        day: "2-digit",
        month: "2-digit",
        year: "numeric"
    });
};

const formatTime = (value) => {
    if (!value) return "--";

    const time = new Date(`1970-01-01T${value}`);
    if (Number.isNaN(time.getTime())) {
        return value;
    }

    return time.toLocaleTimeString("es-MX", {
        hour: "2-digit",
        minute: "2-digit",
        hour12: true
    });
};

const calcularEdad = (fechaNacimiento) => {
    if (!fechaNacimiento) return "Sin edad";

    const birth = new Date(`${fechaNacimiento}T00:00:00`);
    if (Number.isNaN(birth.getTime())) return "Sin edad";

    const today = new Date();
    let years = today.getFullYear() - birth.getFullYear();
    const monthDiff = today.getMonth() - birth.getMonth();

    if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birth.getDate())) {
        years -= 1;
    }

    return `${years} año${years === 1 ? "" : "s"}`;
};

const getEstadoCitaLabel = (estado) => {
    switch (estado) {
        case 1:
            return { label: "Programada", className: "pending" };
        case 2:
            return { label: "Confirmada", className: "success" };
        case 3:
            return { label: "Cancelada", className: "pending" };
        default:
            return { label: "Desconocida", className: "pending" };
    }
};

function MainMenuDueno() {
    const navigate = useNavigate();
    const [activeItem, setActiveItem] = useState("Inicio");
    const [ownerName, setOwnerName] = useState("Dueño");
    const [mascotas, setMascotas] = useState([]);
    const [citas, setCitas] = useState([]);
    const [loading, setLoading] = useState(true);

    const correo = sessionStorage.getItem("Usuario")?.replace(/^"|"$/g, "") || "";

    useEffect(() => {
        if (!correo) {
            navigate("/");
            return;
        }

        let isMounted = true;

        const fetchData = async () => {
            try {
                const [perfilRes, mascotasRes, citasRes] = await Promise.all([
                    fetch(`${API_BASE}/usuario/Nombre/${correo}`),
                    fetch(`${API_BASE}/mascota/${correo}`),
                    fetch(`${API_BASE}/cita/${correo}`)
                ]);

                if (!perfilRes.ok || !mascotasRes.ok || !citasRes.ok) {
                    throw new Error("No se pudo cargar la información del dueño");
                }

                const perfilData = await perfilRes.json();
                const mascotasData = await mascotasRes.json();
                const citasData = await citasRes.json();

                if (!isMounted) return;

                setOwnerName(perfilData?.nombre || "Dueño");
                setMascotas(Array.isArray(mascotasData) ? mascotasData : []);
                setCitas(Array.isArray(citasData) ? citasData : []);
            } catch (error) {
                console.error(error);
                if (isMounted) {
                    setOwnerName("Dueño");
                    setMascotas([]);
                    setCitas([]);
                }
            } finally {
                if (isMounted) {
                    setLoading(false);
                }
            }
        };

        fetchData();

        return () => {
            isMounted = false;
        };
    }, [correo, navigate]);

    const handleLogout = () => {
        sessionStorage.clear();
        navigate("/");
    };

    const menuItems = [
        { name: "Inicio", icon: "🏠" },
        { name: "Mis Mascotas", icon: "🐾" },
        { name: "Citas", icon: "🗓️" },
        { name: "Tratamientos", icon: "💊" },
        { name: "Historial", icon: "📋" },
        { name: "Perfil", icon: "👤" }
    ];

    const pets = Array.isArray(mascotas) ? mascotas : [];
    const pet = pets[0] || null;
    const petName = pet?.nombre || (pets.length > 0 ? "Mis mascotas" : "Mi mascota");
    const petSpecies = pet?.raza?.especie?.nombre || pet?.raza?.nombre || "Sin especie";
    const petListSummary = pets.length > 0 ? pets.map((item) => item.nombre || "Mascota").join(", ") : "Sin mascotas";
    const nextCita = [...citas].sort((a, b) => new Date(a.fecha) - new Date(b.fecha))[0] || null;

    const changeMenu = (item) => {
        setActiveItem(item);
    };

    return (
        <div className="owner-container">
            <aside className="owner-sidebar">
                <div className="owner-header">
                    <h2>Mi Huellitas</h2>
                    <p>Bienvenido, {ownerName}</p>
                </div>

                <nav className="owner-nav">
                    {menuItems.map((item) => (
                        <button
                            type="button"
                            key={item.name}
                            className={`owner-menu-item ${
                                activeItem === item.name ? "active" : ""
                            }`}
                            onClick={() => changeMenu(item.name)}
                        >
                            <span className="owner-menu-icon">{item.icon}</span>
                            <span>{item.name}</span>
                        </button>
                    ))}
                </nav>

                <button type="button" className="owner-logout" onClick={handleLogout}>
                    🚪 Cerrar Sesión
                </button>
            </aside>

            <main className="owner-content">
                {loading ? (
                    <div className="owner-section">
                        <div className="owner-loading">Cargando información del dueño...</div>
                    </div>
                ) : (
                    <>
                        {activeItem === "Inicio" && (
                            <div className="owner-section">
                                <h1>Panel del dueño</h1>
                                <p className="owner-subtitle">Resumen de tus mascotas y sus cuidados</p>

                                <div className="owner-cards">
                                    <div className="owner-card">
                                        <span className="owner-card-icon">🐾</span>
                                        <div>
                                            <h3>Mis mascotas</h3>
                                            <strong>{pets.length > 0 ? `${pets.length}` : "0"}</strong>
                                            <p>{pets.length > 0 ? petListSummary : "Sin mascotas registradas"}</p>
                                        </div>
                                    </div>

                                    <div className="owner-card">
                                        <span className="owner-card-icon">🗓️</span>
                                        <div>
                                            <h3>Próxima cita</h3>
                                            <strong>{nextCita ? formatDate(nextCita.fecha) : "--"}</strong>
                                            <p>{nextCita ? `${formatTime(nextCita.entradaAgendada)} / ${nextCita.nombreVeterinario || "Veterinario"}` : "Sin citas registradas"}</p>
                                        </div>
                                    </div>

                                    <div className="owner-card">
                                        <span className="owner-card-icon">💊</span>
                                        <div>
                                            <h3>Tratamiento</h3>
                                            <strong>{pet ? "Activo" : "Sin dato"}</strong>
                                            <p>{pet ? "Revisión y cuidado continuo" : "No hay información disponible"}</p>
                                        </div>
                                    </div>

                                    <div className="owner-card">
                                        <span className="owner-card-icon">📋</span>
                                        <div>
                                            <h3>Historial</h3>
                                            <strong>{citas.length}</strong>
                                            <p>Eventos registrados</p>
                                        </div>
                                    </div>
                                </div>

                                <div className="owner-table-container">
                                    <h2>Actividad reciente</h2>
                                    {citas.length === 0 ? (
                                        <div className="owner-empty">No hay citas registradas para este usuario.</div>
                                    ) : (
                                        <table className="owner-table">
                                            <thead>
                                                <tr>
                                                    <th>Fecha</th>
                                                    <th>Hora</th>
                                                    <th>Veterinario</th>
                                                    <th>Estado</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                {citas.slice(0, 3).map((cita) => {
                                                    const estado = getEstadoCitaLabel(cita.estadoCita);
                                                    return (
                                                        <tr key={cita.idCita}>
                                                            <td>{formatDate(cita.fecha)}</td>
                                                            <td>{formatTime(cita.entradaAgendada)}</td>
                                                            <td>{cita.nombreVeterinario || "Pendiente de asignación"}</td>
                                                            <td>
                                                                <span className={`status ${estado.className}`}>
                                                                    {estado.label}
                                                                </span>
                                                            </td>
                                                        </tr>
                                                    );
                                                })}
                                            </tbody>
                                        </table>
                                    )}
                                </div>
                            </div>
                        )}

                        {activeItem === "Mis Mascotas" && (
                            <div className="owner-section">
                                <h1>Mis mascotas</h1>
                                <p className="owner-subtitle">Información principal de tus mascotas</p>

                                {!pets.length ? (
                                    <div className="owner-empty">No se encontró ninguna mascota asociada a este usuario.</div>
                                ) : (
                                    <div className="owner-table-container">
                                        <table className="owner-table">
                                            <thead>
                                                <tr>
                                                    <th>Nombre</th>
                                                    <th>Especie</th>
                                                    <th>Raza</th>
                                                    <th>Edad</th>
                                                    <th>Fecha de nacimiento</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                {pets.map((petItem) => (
                                                    <tr key={petItem.idMascota || `${petItem.nombre}-${petItem.fechaNacimiento}`}>
                                                        <td>{petItem.nombre}</td>
                                                        <td>{petItem.raza?.especie?.nombre || petItem.raza?.nombre || petItem.especie || "Sin especie"}</td>
                                                        <td>{petItem.raza?.nombre || petItem.raza || petItem.breed || "Sin raza"}</td>
                                                        <td>{calcularEdad(petItem.fechaNacimiento)}</td>
                                                        <td>{formatDate(petItem.fechaNacimiento)}</td>
                                                    </tr>
                                                ))}
                                            </tbody>
                                        </table>
                                    </div>
                                )}
                            </div>
                        )}

                        {activeItem === "Citas" && (
                            <div className="owner-section">
                                <h1>Citas</h1>
                                <p className="owner-subtitle">Tus próximas citas veterinarias</p>

                                <div className="owner-table-container">
                                    {citas.length === 0 ? (
                                        <div className="owner-empty">Todavía no tienes citas registradas.</div>
                                    ) : (
                                        <table className="owner-table">
                                            <thead>
                                                <tr>
                                                    <th>Fecha</th>
                                                    <th>Hora</th>
                                                    <th>Motivo</th>
                                                    <th>Doctor</th>
                                                    <th>Estado</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                {citas.map((cita) => {
                                                    const estado = getEstadoCitaLabel(cita.estadoCita);
                                                    return (
                                                        <tr key={cita.idCita}>
                                                            <td>{formatDate(cita.fecha)}</td>
                                                            <td>{formatTime(cita.entradaAgendada)}</td>
                                                            <td>Consulta veterinaria</td>
                                                            <td>{cita.nombreVeterinario || "Pendiente"}</td>
                                                            <td>
                                                                <span className={`status ${estado.className}`}>
                                                                    {estado.label}
                                                                </span>
                                                            </td>
                                                        </tr>
                                                    );
                                                })}
                                            </tbody>
                                        </table>
                                    )}
                                </div>
                            </div>
                        )}

                        {activeItem === "Tratamientos" && (
                            <div className="owner-section">
                                <h1>Tratamientos</h1>
                                <p className="owner-subtitle">Medicamentos y cuidados actuales</p>

                                <div className="owner-table-container">
                                    <table className="owner-table">
                                        <thead>
                                            <tr>
                                                <th>Medicamento</th>
                                                <th>Descripción</th>
                                                <th>Frecuencia</th>
                                                <th>Estado</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <tr>
                                                <td>{pet ? "Antiinflamatorio" : "Sin medicamento"}</td>
                                                <td>{pet ? "Seguimiento veterinario" : "Sin información"}</td>
                                                <td>{pet ? "Cada 12 horas" : "--"}</td>
                                                <td>
                                                    <span className="status success">
                                                        {pet ? "Activo" : "Sin dato"}
                                                    </span>
                                                </td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        )}

                        {activeItem === "Historial" && (
                            <div className="owner-section">
                                <h1>Historial</h1>
                                <p className="owner-subtitle">Registros médicos de {petName}</p>

                                <div className="owner-table-container">
                                    {citas.length === 0 ? (
                                        <div className="owner-empty">Aún no hay historial médico disponible.</div>
                                    ) : (
                                        <table className="owner-table">
                                            <thead>
                                                <tr>
                                                    <th>Fecha</th>
                                                    <th>Tipo</th>
                                                    <th>Veterinario</th>
                                                    <th>Resultado</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                {citas.map((cita) => {
                                                    const estado = getEstadoCitaLabel(cita.estadoCita);
                                                    return (
                                                        <tr key={`historial-${cita.idCita}`}>
                                                            <td>{formatDate(cita.fecha)}</td>
                                                            <td>Consulta</td>
                                                            <td>{cita.nombreVeterinario || "Pendiente"}</td>
                                                            <td>
                                                                <span className={`status ${estado.className}`}>
                                                                    {estado.label}
                                                                </span>
                                                            </td>
                                                        </tr>
                                                    );
                                                })}
                                            </tbody>
                                        </table>
                                    )}
                                </div>
                            </div>
                        )}

                        {activeItem === "Perfil" && (
                            <div className="owner-section">
                                <h1>Perfil</h1>
                                <p className="owner-subtitle">Datos del propietario</p>

                                <div className="owner-table-container">
                                    <table className="owner-table">
                                        <thead>
                                            <tr>
                                                <th>Campo</th>
                                                <th>Detalle</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <tr>
                                                <td>Nombre</td>
                                                <td>{ownerName}</td>
                                            </tr>
                                            <tr>
                                                <td>Correo</td>
                                                <td>{correo}</td>
                                            </tr>
                                            <tr>
                                                <td>Mascotas</td>
                                                <td>{petListSummary}</td>
                                            </tr>
                                            <tr>
                                                <td>Especie</td>
                                                <td>{petSpecies}</td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        )}
                    </>
                )}
            </main>
        </div>
    );
}

export default MainMenuDueno;