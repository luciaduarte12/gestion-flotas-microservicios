import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      '/vehiculos': 'http://localhost:8080',
      '/alertas': 'http://localhost:8080',
    },
  },
})
