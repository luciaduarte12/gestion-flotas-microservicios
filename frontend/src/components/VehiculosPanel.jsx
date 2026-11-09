import { useCallback, useEffect, useState } from 'react'
import { crearVehiculo, listarVehiculos, transicionar } from '../api'

// Acciones válidas por estado: refleja las transiciones del patrón State del backend
const ACCIONES = {
  DISPONIBLE: [
    { ruta: 'iniciar-ruta', etiqueta: 'Iniciar ruta' },
    { ruta: 'enviar-a-mantenimiento', etiqueta: 'Enviar a mantenimiento' },
  ],
  EN_RUTA: [{ ruta: 'finalizar-ruta', etiqueta: 'Finalizar ruta' }],
  EN_MANTENIMIENTO: [
    { ruta: 'finalizar-mantenimiento', etiqueta: 'Finalizar mantenimiento' },
  ],
}

const ETIQUETAS = {
  DISPONIBLE: 'Disponible',
  EN_RUTA: 'En ruta',
  EN_MANTENIMIENTO: 'En mantenimiento',
}

const FORM_VACIO = { patente: '', marca: '', modelo: '', kilometraje: '' }

export default function VehiculosPanel() {
  const [vehiculos, setVehiculos] = useState([])
  const [form, setForm] = useState(FORM_VACIO)
  const [error, setError] = useState('')

  const cargar = useCallback(async () => {
    try {
      setVehiculos(await listarVehiculos())
      setError('')
    } catch (e) {
      setError(e.message)
    }
  }, [])

  useEffect(() => {
    cargar()
  }, [cargar])

  const cambiarCampo = (e) =>
    setForm({ ...form, [e.target.name]: e.target.value })

  const crear = async (e) => {
    e.preventDefault()
    try {
      await crearVehiculo({ ...form, kilometraje: Number(form.kilometraje) })
      setForm(FORM_VACIO)
      await cargar()
    } catch (err) {
      setError(err.message)
    }
  }

  const ejecutar = async (id, accion) => {
    try {
      await transicionar(id, accion)
      await cargar()
    } catch (err) {
      setError(err.message)
    }
  }

  return (
    <section className="panel">
      <h2>Vehículos</h2>

      {error && <p className="error">{error}</p>}

      <form className="form" onSubmit={crear}>
        <input name="patente" placeholder="Patente" value={form.patente}
               onChange={cambiarCampo} required />
        <input name="marca" placeholder="Marca" value={form.marca}
               onChange={cambiarCampo} required />
        <input name="modelo" placeholder="Modelo" value={form.modelo}
               onChange={cambiarCampo} required />
        <input name="kilometraje" type="number" min="0" placeholder="Kilometraje"
               value={form.kilometraje} onChange={cambiarCampo} required />
        <button type="submit">Crear vehículo</button>
      </form>

      <table>
        <thead>
          <tr>
            <th>Patente</th>
            <th>Vehículo</th>
            <th>Km</th>
            <th>Estado</th>
            <th>Acciones</th>
          </tr>
        </thead>
        <tbody>
          {vehiculos.map((v) => (
            <tr key={v.id}>
              <td>{v.patente}</td>
              <td>{v.marca} {v.modelo}</td>
              <td>{v.kilometraje.toLocaleString('es-AR')}</td>
              <td>
                <span className={`estado estado-${v.estado}`}>
                  {ETIQUETAS[v.estado] ?? v.estado}
                </span>
              </td>
              <td className="acciones">
                {(ACCIONES[v.estado] ?? []).map((a) => (
                  <button key={a.ruta} onClick={() => ejecutar(v.id, a.ruta)}>
                    {a.etiqueta}
                  </button>
                ))}
              </td>
            </tr>
          ))}
          {vehiculos.length === 0 && (
            <tr>
              <td colSpan="5" className="vacio">No hay vehículos cargados.</td>
            </tr>
          )}
        </tbody>
      </table>
    </section>
  )
}