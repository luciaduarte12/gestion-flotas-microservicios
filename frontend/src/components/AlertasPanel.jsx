import { useCallback, useEffect, useState } from 'react'
import { atenderAlerta, listarAlertasPendientes } from '../api'

export default function AlertasPanel() {
  const [alertas, setAlertas] = useState([])
  const [error, setError] = useState('')

  const cargar = useCallback(async () => {
    try {
      setAlertas(await listarAlertasPendientes())
      setError('')
    } catch (e) {
      setError(e.message)
    }
  }, [])

  useEffect(() => {
    cargar()
    const intervalo = setInterval(cargar, 5000)
    return () => clearInterval(intervalo)
  }, [cargar])

  const atender = async (id) => {
    try {
      await atenderAlerta(id)
      await cargar()
    } catch (e) {
      setError(e.message)
    }
  }

  return (
    <section className="panel">
      <h2>Alertas de mantenimiento <span className="contador">{alertas.length}</span></h2>

      {error && <p className="error">{error}</p>}

      <ul className="alertas">
        {alertas.map((a) => (
          <li key={a.id}>
            <div>
              <strong>{a.patente}</strong>
              <p>{a.mensaje}</p>
              <small>
                {a.kilometraje.toLocaleString('es-AR')} km ·{' '}
                {new Date(a.creadaEn).toLocaleString('es-AR')}
              </small>
            </div>
            <button onClick={() => atender(a.id)}>Atender</button>
          </li>
        ))}
        {alertas.length === 0 && <li className="vacio">No hay alertas pendientes.</li>}
      </ul>
    </section>
  )
}