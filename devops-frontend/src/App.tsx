import { BrowserRouter as Router, Routes, Route } from 'react-router-dom'
import Login from './pages/Login'
import MenuDueno from './pages/MenuDueno'
import MenuGestor from './pages/MenuGestor'
import MenuAdministrador from './pages/MenuAdministrador'

export default function App() {
  return (
    <Router basename="/huellitas">
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/MainMenu/" element={<MenuDueno />} />
        <Route path="/MainMenuGestor" element={<MenuGestor />} />
        <Route path="/MainMenuAdministrador" element={<MenuAdministrador />} />
      </Routes>
    </Router>
  )
}
