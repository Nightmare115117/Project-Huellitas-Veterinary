import { useState, useEffect, useRef } from "react";
import { useNavigate } from "react-router-dom";
import { getApiBaseHost } from '../Host';
import Swal from "sweetalert2";
import "sweetalert2/dist/sweetalert2.min.css";
import "../styles/login.css";

function Login() {
  const [user, setUser] = useState("");
  const [contraseña, setContraseña] = useState("");
  const [mostrar, setMostrar] = useState(false); // controla mostrar/ocultar
  const navigate = useNavigate();
  const audioRef = useRef(null);


  // 🎵 Música mientras Login está montado
  useEffect(() => {
    const mes = new Date().getMonth();
    const audioSrc = {
      1: "/Music/TANK QUINCEAÑERA (PARODIA).mp3",
      2: "/Music/La mejor música de elevador o ascensor.mp3",
      7: "/Music/Marcha militar alemana _Erika_ - Subtitulado en Español(MP3_160K).mp3",
      11: "/Music/Sleigh Ride (Indian Christmas Remix) - Vindaloo Singh.mp3"
    };

    if (audioSrc[mes]) {
      audioRef.current = new Audio(audioSrc[mes]);
      audioRef.current.loop = true;
      audioRef.current.volume = 0.5;

      const iniciarMusica = () => {
        audioRef.current.play().catch(err =>
          console.log("Error al reproducir audio:", err)
        );
        document.removeEventListener("click", iniciarMusica);
        document.removeEventListener("keydown", iniciarMusica);
      };

      document.addEventListener("click", iniciarMusica);
      document.addEventListener("keydown", iniciarMusica);

      return () => {
        audioRef.current.pause();
        audioRef.current.currentTime = 0;
        document.removeEventListener("click", iniciarMusica);
        document.removeEventListener("keydown", iniciarMusica);
      };
    }
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
  
    const esCorreo = user.includes("@");
  
    if (!esCorreo || user === '') {
      alert("Ingresa un correo válido");
      return;
    }
  
    try {
      const response = await fetch(`/api/usuario/login`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({
          correo: user,
          contrasena: contraseña   // 👈 sin ñ (importante)
        })
      });
      const data = await response.json();
      if (data.login === true) {
        sessionStorage.setItem("Usuario", user);
        Swal.fire({
          title: "Login correcto",
          text: "Bienvenido, has iniciado sesión correctamente",
          icon: "success",
          didOpen: () => {
            const icon = Swal.getIcon();
            if (icon) {
              icon.style.animation = "none";
              icon.offsetHeight; // fuerza reflow
              icon.style.animation = "";
            }
          }
        });
        fetch(`/api/usuario/Rol?correo=${user}`)
        .then(res => res.json())
        .then(data => {
          if (data === 2) {
            navigate("/MainMenu/");
          } else if (data === 1) {
            navigate("/MainMenuAdministrador");
          } else  if (data === 3){
            navigate("/MainMenuGestor");
          }
        })
        .catch(err => console.error(err));
      } else {
        Swal.fire({
          title: "Credenciales Incorrectas",
          text: "Por favor, ingrese sus credenciales nuevamente",
          icon: "error"
        });
      }
  
    } catch (error) {
      console.error("Error:", error);
      Swal.fire({
        title: "Credenciales Incorrectas",
        text: "Por favor, ingrese sus credenciales nuevamente",
        icon: "error"
      });
    }
  };

  return (
    <div id="app-wrapper">
      <div className="login-container">
        <form className="login-box" onSubmit={handleSubmit}>
          <img src="/Logo.svg" alt="Logo" className="logo" />
          <h2>Iniciar sesión</h2>

          <input
            type="text"
            placeholder="Ingrese su correo"
            value={user}
            onChange={(e) => setUser(e.target.value)}
            required
          />

          <input
            type={mostrar ? "text" : "password"}
            placeholder="Contraseña"
            value={contraseña}
            onChange={(e) => setContraseña(e.target.value)}
            required
          />

          <label className="custom-checkbox">
            <input
              className="checkbox"
              type="checkbox"
              checked={mostrar}
              onChange={() => setMostrar(!mostrar)}
            />
            <span className="checkmark"></span>
            <span className="label-text">Mostrar contraseña</span>
          </label>

          <button type="submit">Entrar</button>
        </form>
      </div>
    </div>
  );
}

export default Login;