import { useState } from 'react'
import { crearVehiculo, transicionar } from '../api'
import { IconCheck, IconRoute, IconWrench } from './Icons'

// Acciones válidas por estado: refleja las transiciones del patrón State del backend
const ACCIONES = {
  DISPONIBLE: [
    { ruta: 'iniciar-ruta', etiqueta: 'Iniciar ruta', clase: 'azul' },
    { ruta: 'enviar-a-mantenimiento', etiqueta: 'Enviar a mantenimiento', clase: 'ambar' },
  ],
  EN_RUTA: [{ ruta: 'finalizar-ruta', etiqueta: 'Finalizar ruta', clase: 'verde' }],
  EN_MANTENIMIENTO: [
    { ruta: 'finalizar-mantenimiento', etiqueta: 'Finalizar mantenimiento', clase: 'verde' },
  ],
}

const ESTADOS = {
  DISPONIBLE: { texto: 'Disponible', icono: <IconCheck width={14} height={14} /> },
  EN_RUTA: { texto: 'En ruta', icono: <IconRoute width={14} height={14} /> },
  EN_MANTENIMIENTO: { texto: 'En mantenimiento', icono: <IconWrench width={14} height={14} /> },
}

const FORM_VACIO = { patente: '', marca: '', modelo: '', kilometraje: '' }

export default function VehiculosPanel({ vehiculos, onCambio }) {
  const [form, setForm] = useState(FORM_VACIO)
  const [error, setError] = useState('')
  const [enviando, setEnviando] = useState(false)

  const cambiarCampo = (e) => setForm({ ...form, [e.target.name]: e.target.value })

  const crear = async (e) => {
    e.preventDefault()
    if (enviando) return
    setEnviando(true)
    try {
      await crearVehiculo({
        ...form,
        patente: form.patente.trim().toUpperCase(),
        kilometraje: Number(form.kilometraje),
      })
      setForm(FORM_VACIO)
      setError('')
      await onCambio()
    } catch (err) {
      setError(err.message)
    } finally {
      setEnviando(false)
    }
  }

  const ejecutar = async (id, accion) => {
    try {
      await transicionar(id, accion)
      setError('')
      await onCambio()
    } catch (err) {
      setError(err.message)
    }
  }

  return (
    <section className="tarjeta">
      <h2>Flota</h2>

      {error && <p className="error">{error}</p>}

      <form className="form" onSubmit={crear}>
        <input name="patente" placeholder="Patente (AB123CD)" value={form.patente}
               onChange={cambiarCampo} maxLength={10} required />
        <input name="marca" placeholder="Marca" value={form.marca}
               onChange={cambiarCampo} required />
        <input name="modelo" placeholder="Modelo" value={form.modelo}
               onChange={cambiarCampo} required />
        <input name="kilometraje" type="number" min="0" placeholder="Kilometraje"
               value={form.kilometraje} onChange={cambiarCampo} required />
        <button type="submit" className="btn azul" disabled={enviando}>
          {enviando ? 'Creando...' : 'Agregar a la flota'}
        </button>
      </form>

      <div className="tabla-wrap">
        <table>
          <thead>
            <tr>
              <th>Patente</th>
              <th>Vehículo</th>
              <th>Kilometraje</th>
              <th>Estado</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            {vehiculos.map((v) => {
              const estado = ESTADOS[v.estado] ?? { texto: v.estado, icono: null }
              return (
                <tr key={v.id}>
                  <td>
                    <span className="patente">
                      <small>ARGENTINA</small>
                      <b>{v.patente}</b>
                    </span>
                  </td>
                  <td>
                    <strong>{v.marca}</strong> {v.modelo}
                  </td>
                  <td>{v.kilometraje.toLocaleString('es-AR')} km</td>
                  <td>
                    <span className={`estado estado-${v.estado}`}>
                      {estado.icono} {estado.texto}
                    </span>
                  </td>
                  <td>
                    <div className="acciones">
                      {(ACCIONES[v.estado] ?? []).map((a) => (
                        <button key={a.ruta} className={`btn ${a.clase}`}
                                onClick={() => ejecutar(v.id, a.ruta)}>
                          {a.etiqueta}
                        </button>
                      ))}
                    </div>
                  </td>
                </tr>
              )
            })}
            {vehiculos.length === 0 && (
              <tr>
                <td colSpan="5" className="vacio">No hay vehículos cargados.</td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </section>
  )
}