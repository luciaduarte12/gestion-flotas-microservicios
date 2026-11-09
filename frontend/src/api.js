async function request(url, options) {
  const res = await fetch(url, options)

  if (!res.ok) {
    const texto = await res.text()
    let mensaje = texto
    try {
      const json = JSON.parse(texto)
      mensaje = json.message || json.error || texto
    } catch {
      // la respuesta no era JSON (por ejemplo, el texto del 409): se usa tal cual
    }
    throw new Error(mensaje || `Error ${res.status}`)
  }

  const tipo = res.headers.get('content-type') || ''
  return tipo.includes('application/json') ? res.json() : null
}

export const listarVehiculos = () => request('/vehiculos')

export const crearVehiculo = (vehiculo) =>
  request('/vehiculos', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(vehiculo),
  })

export const transicionar = (id, accion) =>
  request(`/vehiculos/${id}/${accion}`, { method: 'POST' })

export const listarAlertasPendientes = () => request('/alertas?atendida=false')

export const atenderAlerta = (id) =>
  request(`/alertas/${id}/atender`, { method: 'POST' })