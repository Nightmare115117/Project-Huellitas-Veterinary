import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import "../styles/MainMenu-administrador.css";

const API_BASE = import.meta.env.DEV ? "/api" : `${import.meta.env.BASE_URL}api`;

const menuItems = [
    { name: "Inicio", icon: "📊" },
    { name: "Usuarios", icon: "👥" },
    { name: "Mascotas", icon: "🐕" },
    { name: "Citas", icon: "🗓️" },
    { name: "Tratamientos", icon: "💊" },
    { name: "Personal", icon: "👨‍⚕️" },
];

const statusClassMap = {
    Completado: "success",
    Confirmada: "success",
    Activo: "success",
    Disponible: "success",
    Programada: "pending",
    Pendiente: "pending",
    Ocupado: "pending",
    Cancelada: "pending",
};

const extractArrayPayload = (response, fallbackKey) => {
    if (Array.isArray(response)) return response;

    const candidateKeys = [
        fallbackKey,
        "usuarios",
        "users",
        "data",
        "content",
        "items",
        "result",
        "results",
        "records",
        "list",
    ];

    if (response && typeof response === "object") {
        for (const key of candidateKeys) {
            if (Array.isArray(response[key])) return response[key];
        }
    }

    return [];
};

const normalizeStatus = (value) => {
    if (!value && value !== 0) return "Activo";
    return String(value).trim();
};

function MainMenuAdministrador() {
    const navigate = useNavigate();
    const correo = sessionStorage.getItem("Usuario")?.replace(/^"|"$/g, "") || "";
    const [activeItem, setActiveItem] = useState(menuItems[0].name);
    const [adminName, setAdminName] = useState("Administrador");
    const [loading, setLoading] = useState(true);
    const [dashboard, setDashboard] = useState({
        users: [],
        pets: [],
        appointments: [],
        treatments: [],
        staff: [],
        recentActivity: [],
        summaryCards: [],
    });

    useEffect(() => {
        if (!correo) {
            navigate("/");
            return;
        }

        let isMounted = true;

        const fetchData = async () => {
            try {
                const [perfilRes, usuariosRes, mascotasRes, citasRes, tratamientosRes] = await Promise.all([
                    fetch(`${API_BASE}/usuario/Nombre/${correo}`),
                    fetch(`${API_BASE}/usuario`),
                    fetch(`${API_BASE}/mascota`),
                    fetch(`${API_BASE}/cita`),
                    fetch(`${API_BASE}/tratamiento`),
                ]);

                const perfilData = perfilRes.ok ? await perfilRes.json() : null;
                const usuariosData = usuariosRes.ok ? await usuariosRes.json() : [];
                const mascotasData = mascotasRes.ok ? await mascotasRes.json() : [];
                const citasData = citasRes.ok ? await citasRes.json() : [];
                const tratamientosData = tratamientosRes.ok ? await tratamientosRes.json() : [];

                if (!isMounted) return;

                const users = extractArrayPayload(usuariosData, "usuarios").map((user) => ({
                    name: user.nombreCompleto || user.nombre || user.name || user.correo || user.email || "Sin nombre",
                    email: user.correo || user.email || user.username || "-",
                    role: user.rol?.nombre || user.rol || user.role || user.rolUsuario || "Usuario",
                    status: normalizeStatus(user.estado || user.status || user.estatus || user.activo),
                }));

                const pets = extractArrayPayload(mascotasData, "mascotas").map((pet) => ({
                    name: pet.nombre || pet.name || "Sin nombre",
                    species: pet.raza?.especie?.nombre || pet.especie || pet.species || pet.tipo || "Sin especie",
                    breed: pet.raza?.nombre || pet.raza || pet.breed || pet.tipoRaza || "Sin raza",
                    owner: pet.dueno?.nombre || pet.dueno || pet.owner || pet.nombreDueno || "Sin dueño",
                }));

                const appointments = extractArrayPayload(citasData, "citas").map((cita) => ({
                    date: cita.fecha || cita.date || cita.agenda || "-",
                    time: cita.entradaAgendada || cita.hora || cita.time || cita.horaCita || "-",
                    pet: cita.mascota?.nombre || cita.pet || cita.nombreMascota || "Sin mascota",
                    owner: cita.dueno?.nombre || cita.dueno || cita.owner || cita.nombreDueno || "Sin dueño",
                    status: normalizeStatus(cita.estadoCita || cita.status || cita.estatus || cita.estado || "Pendiente"),
                }));

                const treatments = extractArrayPayload(tratamientosData, "tratamientos").map((tratamiento) => ({
                    pet: tratamiento.mascota?.nombre || tratamiento.pet || tratamiento.nombreMascota || "Sin mascota",
                    medicine: tratamiento.medicamento?.nombre || tratamiento.medicamento || tratamiento.medicine || tratamiento.nombreMedicamento || "Sin medicamento",
                    description: tratamiento.descripcion || tratamiento.description || tratamiento.observaciones || "Sin descripción",
                    status: normalizeStatus(tratamiento.estado || tratamiento.status || tratamiento.estatus || tratamiento.activo || "Activo"),
                }));

                const staff = users.filter((user) => {
                    const role = user.role?.toLowerCase?.() || "";
                    return role.includes("veter") || role.includes("asist") || role.includes("admin") || role.includes("gestor");
                });

                const summaryCards = [
                    { title: "Usuarios", value: users.length, detail: "Usuarios registrados", icon: "👥" },
                    { title: "Mascotas", value: pets.length, detail: "Mascotas registradas", icon: "🐕" },
                    { title: "Citas", value: appointments.filter((item) => item.status === "Pendiente" || item.status === "Programada").length, detail: "Citas pendientes", icon: "🗓️" },
                    { title: "Tratamientos", value: treatments.filter((item) => item.status === "Activo").length, detail: "Tratamientos activos", icon: "💊" },
                ];

                const recentActivity = appointments.slice(0, 3).map((appointment) => ({
                    user: appointment.owner,
                    action: `Solicitó una cita para ${appointment.pet}`,
                    date: appointment.date,
                    status: appointment.status,
                }));

                setAdminName(perfilData?.nombre || perfilData?.name || "Administrador");
                setDashboard({
                    users,
                    pets,
                    appointments,
                    treatments,
                    staff,
                    recentActivity: recentActivity.length ? recentActivity : [],
                    summaryCards,
                });
            } catch (error) {
                console.error(error);
                if (isMounted) {
                    setDashboard({
                        users: [],
                        pets: [],
                        appointments: [],
                        treatments: [],
                        staff: [],
                        recentActivity: [],
                        summaryCards: [],
                    });
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

    const renderSectionContent = () => {
        switch (activeItem) {
            case "Inicio":
                return (
                    <div className="admin-section">
                        <div className="admin-page-header">
                            <div>
                                <h1>Resumen del sistema</h1>
                                <p className="admin-subtitle">Vista general de Huellitas</p>
                            </div>
                        </div>

                        <div className="admin-cards">
                            {dashboard.summaryCards.length > 0 ? (
                                dashboard.summaryCards.map((card) => (
                                    <div key={card.title} className="admin-card">
                                        <span className="admin-card-icon">{card.icon}</span>
                                        <div>
                                            <h3>{card.title}</h3>
                                            <strong>{card.value}</strong>
                                            <p>{card.detail}</p>
                                        </div>
                                    </div>
                                ))
                            ) : (
                                <div className="admin-empty">No hay información disponible del sistema.</div>
                            )}
                        </div>

                        <div className="admin-section-panel">
                            <h2>Actividad reciente</h2>
                            {dashboard.recentActivity.length > 0 ? (
                                <table className="admin-table">
                                    <thead>
                                        <tr>
                                            <th>Usuario</th>
                                            <th>Actividad</th>
                                            <th>Fecha</th>
                                            <th>Estado</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {dashboard.recentActivity.map((entry, index) => (
                                            <tr key={`${entry.user}-${entry.date}-${index}`}>
                                                <td>{entry.user}</td>
                                                <td>{entry.action}</td>
                                                <td>{entry.date}</td>
                                                <td>
                                                    <span className={`status ${statusClassMap[entry.status] || "success"}`}>
                                                        {entry.status}
                                                    </span>
                                                </td>
                                            </tr>
                                        ))}
                                    </tbody>
                                </table>
                            ) : (
                                <div className="admin-empty">No hay actividad reciente para mostrar.</div>
                            )}
                        </div>
                    </div>
                );
            case "Usuarios":
                return (
                    <div className="admin-section">
                        <div className="admin-page-header">
                            <div>
                                <h1>Usuarios</h1>
                                <p className="admin-subtitle">Administración de usuarios registrados</p>
                            </div>
                        </div>
                        <div className="admin-section-panel">
                            {dashboard.users.length > 0 ? (
                                <table className="admin-table">
                                    <thead>
                                        <tr>
                                            <th>Nombre</th>
                                            <th>Correo</th>
                                            <th>Rol</th>
                                            <th>Estado</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {dashboard.users.map((user) => (
                                            <tr key={`${user.email}-${user.name}`}>
                                                <td>{user.name}</td>
                                                <td>{user.email}</td>
                                                <td>{user.role}</td>
                                                <td>
                                                    <span className={`status ${statusClassMap[user.status] || "success"}`}>
                                                        {user.status}
                                                    </span>
                                                </td>
                                            </tr>
                                        ))}
                                    </tbody>
                                </table>
                            ) : (
                                <div className="admin-empty">No se encontraron usuarios.</div>
                            )}
                        </div>
                    </div>
                );
            case "Mascotas":
                return (
                    <div className="admin-section">
                        <div className="admin-page-header">
                            <div>
                                <h1>Mascotas</h1>
                                <p className="admin-subtitle">Mascotas registradas en el sistema</p>
                            </div>
                        </div>
                        <div className="admin-section-panel">
                            {dashboard.pets.length > 0 ? (
                                <table className="admin-table">
                                    <thead>
                                        <tr>
                                            <th>Nombre</th>
                                            <th>Especie</th>
                                            <th>Raza</th>
                                            <th>Dueño</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {dashboard.pets.map((pet) => (
                                            <tr key={`${pet.name}-${pet.owner}`}>
                                                <td>{pet.name}</td>
                                                <td>{pet.species}</td>
                                                <td>{pet.breed}</td>
                                                <td>{pet.owner}</td>
                                            </tr>
                                        ))}
                                    </tbody>
                                </table>
                            ) : (
                                <div className="admin-empty">No se encontraron mascotas.</div>
                            )}
                        </div>
                    </div>
                );
            case "Citas":
                return (
                    <div className="admin-section">
                        <div className="admin-page-header">
                            <div>
                                <h1>Citas</h1>
                                <p className="admin-subtitle">Gestión de citas veterinarias</p>
                            </div>
                        </div>
                        <div className="admin-section-panel">
                            {dashboard.appointments.length > 0 ? (
                                <table className="admin-table">
                                    <thead>
                                        <tr>
                                            <th>Fecha</th>
                                            <th>Hora</th>
                                            <th>Mascota</th>
                                            <th>Dueño</th>
                                            <th>Estado</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {dashboard.appointments.map((appointment, index) => (
                                            <tr key={`${appointment.pet}-${appointment.date}-${index}`}>
                                                <td>{appointment.date}</td>
                                                <td>{appointment.time}</td>
                                                <td>{appointment.pet}</td>
                                                <td>{appointment.owner}</td>
                                                <td>
                                                    <span className={`status ${statusClassMap[appointment.status] || "success"}`}>
                                                        {appointment.status}
                                                    </span>
                                                </td>
                                            </tr>
                                        ))}
                                    </tbody>
                                </table>
                            ) : (
                                <div className="admin-empty">No hay citas registradas.</div>
                            )}
                        </div>
                    </div>
                );
            case "Tratamientos":
                return (
                    <div className="admin-section">
                        <div className="admin-page-header">
                            <div>
                                <h1>Tratamientos</h1>
                                <p className="admin-subtitle">Administración de tratamientos</p>
                            </div>
                        </div>
                        <div className="admin-section-panel">
                            {dashboard.treatments.length > 0 ? (
                                <table className="admin-table">
                                    <thead>
                                        <tr>
                                            <th>Mascota</th>
                                            <th>Medicamento</th>
                                            <th>Descripción</th>
                                            <th>Estatus</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {dashboard.treatments.map((treatment, index) => (
                                            <tr key={`${treatment.pet}-${treatment.medicine}-${index}`}>
                                                <td>{treatment.pet}</td>
                                                <td>{treatment.medicine}</td>
                                                <td>{treatment.description}</td>
                                                <td>
                                                    <span className={`status ${statusClassMap[treatment.status] || "success"}`}>
                                                        {treatment.status}
                                                    </span>
                                                </td>
                                            </tr>
                                        ))}
                                    </tbody>
                                </table>
                            ) : (
                                <div className="admin-empty">No hay tratamientos registrados.</div>
                            )}
                        </div>
                    </div>
                );
            case "Personal":
                return (
                    <div className="admin-section">
                        <div className="admin-page-header">
                            <div>
                                <h1>Personal</h1>
                                <p className="admin-subtitle">Equipo de atención veterinaria</p>
                            </div>
                        </div>
                        <div className="admin-section-panel">
                            {dashboard.staff.length > 0 ? (
                                <table className="admin-table">
                                    <thead>
                                        <tr>
                                            <th>Nombre</th>
                                            <th>Rol</th>
                                            <th>Turno</th>
                                            <th>Estado</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {dashboard.staff.map((member) => (
                                            <tr key={`${member.name}-${member.role}`}>
                                                <td>{member.name}</td>
                                                <td>{member.role}</td>
                                                <td>{member.shift || "-"}</td>
                                                <td>
                                                    <span className={`status ${statusClassMap[member.status] || "success"}`}>
                                                        {member.status}
                                                    </span>
                                                </td>
                                            </tr>
                                        ))}
                                    </tbody>
                                </table>
                            ) : (
                                <div className="admin-empty">No hay personal disponible.</div>
                            )}
                        </div>
                    </div>
                );
            default:
                return null;
        }
    };

    return (
        <div className="admin-container">
            <aside className="admin-sidebar">
                <div className="admin-header">
                    <h2>Panel Administrativo</h2>
                    <p>Bienvenido, {adminName}</p>
                </div>

                <nav className="admin-nav" aria-label="Menú principal del administrador">
                    {menuItems.map((item) => (
                        <button
                            type="button"
                            key={item.name}
                            className={`admin-menu-item ${activeItem === item.name ? "active" : ""}`}
                            onClick={() => setActiveItem(item.name)}
                        >
                            <span className="admin-menu-icon">{item.icon}</span>
                            <span>{item.name}</span>
                        </button>
                    ))}
                </nav>

                <button type="button" className="admin-logout" onClick={handleLogout}>
                    🚪 Cerrar Sesión
                </button>
            </aside>

            <main className="admin-content">
                {loading ? (
                    <div className="admin-section">
                        <div className="admin-loading">Cargando información del administrador...</div>
                    </div>
                ) : (
                    renderSectionContent()
                )}
            </main>
        </div>
    );
}

export default MainMenuAdministrador;