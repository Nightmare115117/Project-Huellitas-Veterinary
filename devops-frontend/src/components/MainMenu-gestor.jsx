import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Swal from "sweetalert2";
import "sweetalert2/dist/sweetalert2.min.css";
import "../styles/MainMenu-gestor.css";

function MainMenuGestor() {
  const navigate = useNavigate();
  const correo = sessionStorage.getItem("Usuario")?.replace(/^"|"$/g, "");
  const [gestor, setGestor] = useState("");
  const activeItem = useState("");

  const handleLogout = () => {
    Swal.fire({
      title: "Cerrar Sesion",
      text: "Seguro de querer cerrar sesion?",
      icon: "question",
      showCancelButton: true,
      confirmButtonColor: "#3085d6",
      cancelButtonColor: "#d33",
    }).then((result) => {
      if (result.isConfirmed) {
        navigate("/");
        sessionStorage.clear;
        Swal.fire({
          title: "Sesion cerrada con exito",
          text: "Hasta Pronto",
          icon: "success"
        })
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
    if (!correo) return
    fetch(`/api/usuario/Nombre/${correo}`)
    .then(res => res.json())
    .then(data => setGestor(data))
    .catch(err => console.log(err));
  }, [correo])

  return (
    <>
      <div className="main-menu">

        <div className={`sidebar primary ${gestor ? "active" : ""}`}>
          <h2 style={{color: "white"}}>Menú Principal</h2>
          <h3 style={{color: "white"}}>Bienvenido, {gestor ? gestor.nombre : "Gestor de Sucursal"}</h3>

          <button onClick={handleLogout} className="btn">Cerrar Sesión</button>
        </div>

        <div className={`sidebar secondary ${activeItem}`}>
          {activeItem === "" &&
            <>
              
            </>
          }
        </div>
      </div>
    </>
  );
}

export default MainMenuGestor;