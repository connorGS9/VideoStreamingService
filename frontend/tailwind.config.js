/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        primary: '#094228',
        secondary: '#34756f',
        dark: '#000000',
        darker: '#10434e',
        border: '#fdfdfd'
      }
    },
  },
  plugins: [],
}