import './App.css'
import AlertasPanel from './components/AlertasPanel'
import VehiculosPanel from './components/VehiculosPanel'

export default function App() {
  return (
    <div className="app">
      <header>
        <h1>Gestión de Flotas</h1>
        <p>Los vehículos publican sus cambios de estado y mantenimiento genera las alertas.</p>
      </header>
      <main>
        <VehiculosPanel />
        <AlertasPanel />
      </main>
    </div>
  )
}
