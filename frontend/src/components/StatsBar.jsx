import { IconAlert, IconCheck, IconRoute, IconTruck, IconWrench } from './Icons'

export default function StatsBar({ vehiculos, alertas }) {
  const contar = (estado) => vehiculos.filter((v) => v.estado === estado).length

  const tarjetas = [
    { clase: 'total', icono: <IconTruck />, valor: vehiculos.length, texto: 'Vehículos en flota' },
    { clase: 'disponible', icono: <IconCheck />, valor: contar('DISPONIBLE'), texto: 'Disponibles' },
    { clase: 'ruta', icono: <IconRoute />, valor: contar('EN_RUTA'), texto: 'En ruta' },
    { clase: 'mantenimiento', icono: <IconWrench />, valor: contar('EN_MANTENIMIENTO'), texto: 'En mantenimiento' },
    { clase: 'alerta', icono: <IconAlert />, valor: alertas.length, texto: 'Alertas pendientes' },
  ]

  return (
    <section className="stats">
      {tarjetas.map((t) => (
        <div key={t.texto} className={`stat stat-${t.clase}`}>
          <span className="stat-icono">{t.icono}</span>
          <div>
            <strong>{t.valor}</strong>
            <span>{t.texto}</span>
          </div>
        </div>
      ))}
    </section>
  )
}