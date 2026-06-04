#!/usr/bin/fish




curl -X POST http://192.168.49.2:32179/api/usuario/guardar \
         -H "Content-Type: application/json" \
         -d '{
       "correo": "hola1@gmail.com",
       "contraseña": "Password123",
       "nombre": "Juan",
       "paterno": "Perez",
       "materno": "Lopez",
       "rol": {
         "idRol": 2
       }
     }'