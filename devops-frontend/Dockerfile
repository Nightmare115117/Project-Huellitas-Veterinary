FROM nginx:alpine

# Copia el build de Vite
COPY dist/ /usr/share/nginx/html

# Configuración opcional para SPA (React Router)
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]