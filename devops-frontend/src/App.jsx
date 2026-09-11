import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Login from "./components/Login";
import MainMenuGestor from "./components/MainMenu-gestor";
import MainMenuCliente from "./components/MainMenu-dueno";
import MainMenuAdministrador from "./components/MainMenu-administrador";

function App() {
  return (
    <Router basename="/huellitas">
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/MainMenu/" element={<MainMenuCliente/>} />
        <Route path="/MainMenuGestor" element={<MainMenuGestor />} />
        <Route path="/MainMenuAdministrador" element={<MainMenuAdministrador />} />
      </Routes>
    </Router>
  );
}

export default App;