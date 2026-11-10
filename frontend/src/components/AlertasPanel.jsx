import { useState } from 'react'
import { atenderAlerta } from '../api'
import { IconAlert, IconCheck } from './Icons'

export default function AlertasPanel({ alertas, onCambio }) {
  const [error, setError] = useState('')

  const atender = async (id) => {
    try {
      await atenderAlerta(id)
      setError('')
      await onCambio()
    } catch (e) {
      setError(e.message)
    }
  }

  return (
    <section className="tarjeta">
      <h2>
        Alertas de mantenimiento
        {alertas.length > 0 && <span className="contador">{alertas.length}</span>}
      </h2>

      {error && <p className="error">{error}</p>}

      <ul className="alertas">
        {alertas.map((a) => (
          <li key={a.id}>
            <span className="alerta-icono"><IconAlert /></span>
            <div className="alerta-cuerpo">
              <strong>{a.patente}</strong>
              <p>{a.mensaje}</p>
              <small>
                {a.kilometraje.toLocaleString('es-AR')} km ·{' '}
                {new Date(a.creadaEn).toLocaleString('es-AR')}
              </small>
            </div>
            <button className="btn verde chico" onClick={() => atender(a.id)}>
              Atender
            </button>
          </li>
        ))}
        {alertas.length === 0 && (
          <li className="sin-alertas">
            <IconCheck width={28} height={28} />
            <span>Todo al día: no hay alertas pendientes.</span>
          </li>
        )}
      </ul>
    </section>
  )
}