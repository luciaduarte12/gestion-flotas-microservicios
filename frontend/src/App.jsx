import { useCallback, useEffect, useState } from 'react'
import './App.css'
import { listarAlertasPendientes, listarVehiculos } from './api'
import AlertasPanel from './components/AlertasPanel'
import { IconTruck } from './components/Icons'
import StatsBar from './components/StatsBar'
import VehiculosPanel from './components/VehiculosPanel'

export default function App() {
  const [vehiculos, setVehiculos] = useState([])
  const [alertas, setAlertas] = useState([])
  const [enLinea, setEnLinea] = useState(true)
  const [cargando, setCargando] = useState(true)

  const cargar = useCallback(async () => {
    try {
      const [v, a] = await Promise.all([listarVehiculos(), listarAlertasPendientes()])
      setVehiculos(v)
      setAlertas(a)
      setEnLinea(true)
      setCargando(false)
    } catch {
      setEnLinea(false)
    }
  }, [])

  useEffect(() => {
    cargar()
    const intervalo = setInterval(cargar, 4000)
    return () => clearInterval(intervalo)
  }, [cargar])

  return (
    <div className="app">
      <header className="cabecera">
        <div className="marca">
          <span className="marca-icono"><IconTruck width={28} height={28} /></span>
          <div>
            <h1>FlotaControl</h1>
            <p>Gestión de flotas y mantenimiento preventivo</p>
          </div>
        </div>
        <span className={`conexion ${enLinea ? 'ok' : 'caida'}`}>
          <i /> {enLinea ? 'Sistema en línea' : 'Conectando con el servidor...'}
        </span>
      </header>
      <div className="ruta" />

      <main className="contenido">
        <StatsBar vehiculos={vehiculos} alertas={alertas} />
        <div className="paneles">
          <VehiculosPanel vehiculos={vehiculos} cargando={cargando} onCambio={cargar} />
          <AlertasPanel alertas={alertas} onCambio={cargar} />
        </div>
      </main>

      <footer className="pie">
        Cada cambio de estado se publica como evento en RabbitMQ y el servicio de mantenimiento genera las alertas.
      </footer>
    </div>
  )
}
