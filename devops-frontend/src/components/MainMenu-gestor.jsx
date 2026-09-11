import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Swal from "sweetalert2";
import "sweetalert2/dist/sweetalert2.min.css";
import "../styles/MainMenu-gestor.css";

const API_BASE = import.meta.env.DEV ? "/api" : `${import.meta.env.BASE_URL}api`;

function MainMenuGestor() {
  const navigate = useNavigate();

  const correo = sessionStorage
    .getItem("Usuario")
    ?.replace(/^"|"$/g, "");

  const [gestor, setGestor] = useState(null);
  const [activeItem, setActiveItem] = useState("");

  const handleLogout = () => {
    Swal.fire({
      title: "Cerrar Sesión",
      text: "¿Seguro de querer cerrar sesión?",
      icon: "question",
      showCancelButton: true,
      confirmButtonColor: "#3085d6",
      cancelButtonColor: "#d33",
      confirmButtonText: "Sí, cerrar sesión",
      cancelButtonText: "Cancelar",
    }).then((result) => {
      if (result.isConfirmed) {
        sessionStorage.clear();
        navigate("/");

        Swal.fire({
          title: "Sesión cerrada con éxito",
          text: "Hasta pronto",
          icon: "success",
          timer: 2000,
          showConfirmButton: false,
        });
      }
    });
  };

  useEffect(() => {
    if (!correo) {
      Swal.fire({
        title: "Sesión Expirada",
        text: "Por favor, inicia sesión nuevamente",
        icon: "warning",
        timer: 3000,
        timerProgressBar: true,
        showConfirmButton: false,
      }).then(() => {
        navigate("/");
      });
    }
  }, [correo, navigate]);

  useEffect(() => {
    if (!correo) return;

    fetch(`${API_BASE}/usuario/Nombre/${correo}`)
      .then((res) => {
        if (!res.ok) {
          throw new Error("No se pudo obtener la información del gestor");
        }

        return res.json();
      })
      .then((data) => setGestor(data))
      .catch((err) => {
        console.error(err);

        Swal.fire({
          title: "Error",
          text: "No se pudo cargar la información del gestor",
          icon: "error",
        });
      });
  }, [correo, setGestor]);

  return (
    <div className="main-menu">

      <div className={`sidebar primary ${gestor ? "active" : ""}`}>
        <h2>Menú Principal</h2>

        <h3>
          Bienvenido,{" "}
          {gestor ? gestor.nombre : "Gestor de Sucursal"}
        </h3>

        <div className="menu-options">

          <button
            className={`btn ${activeItem === "usuarios" ? "selected" : ""}`}
            onClick={() => setActiveItem("usuarios")}
          >
            Usuarios
          </button>

          <button
            className={`btn ${activeItem === "sucursales" ? "selected" : ""}`}
            onClick={() => setActiveItem("sucursales")}
          >
            Sucursal
          </button>

          <button
            className={`btn ${activeItem === "reportes" ? "selected" : ""}`}
            onClick={() => setActiveItem("reportes")}
          >
            Reportes
          </button>

          <button
            onClick={handleLogout}
            className="btn logout"
          >
            Cerrar Sesión
          </button>

        </div>
      </div>

      <div className={`sidebar secondary ${activeItem}`}>

        {activeItem === "" && (
          <div className="welcome-panel">
            <h2>Panel del Gestor</h2>
            <p>
              Selecciona una opción del menú para comenzar.
            </p>
          </div>
        )}

        {activeItem === "usuarios" && (
          <div>
            <h2>Usuarios</h2>
            <p>Administración de usuarios.</p>
          </div>
        )}

        {activeItem === "sucursales" && (
          <div>
            <h2>Sucursal</h2>
            <p>Información y administración de la sucursal.</p>
          </div>
        )}

        {activeItem === "reportes" && (
          <div>
            <h2>Reportes</h2>
            <p>Consulta los reportes de la sucursal.</p>
          </div>
        )}

      </div>

    </div>
  );
}

export default MainMenuGestor;