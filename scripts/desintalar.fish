#!/usr/bin/fish

echo "=== Desinstalando Huellitas Veterinary ==="

echo "Deteniendo procesos..."
pkill -f "spring-boot" 2>/dev/null
pkill -f "node" 2>/dev/null
pkill -f "npm" 2>/dev/null

echo "Eliminando base de datos y usuario..."
sudo systemctl start mariadb
sudo mariadb -e "
DROP DATABASE IF EXISTS huellitas_db;
DROP USER IF EXISTS 'Admin'@'localhost';
FLUSH PRIVILEGES;
"

echo "Eliminando archivos del proyecto..."
set DIR "$HOME/Documentos/programa/Project-Huellitas-Veterinary"
if test -d $DIR
    rm -rf $DIR
    rm -rf $HOME/Documentos/programa
    echo "Carpeta del proyecto eliminada."
else
    echo "No se encontró la carpeta del proyecto."
end

echo "Desinstalando dependencias..."
fnm uninstall 24
sudo pacman -Rns --noconfirm fnm mariadb jdk21-openjdk

echo ""
echo "=== Desinstalación completada con éxito ==="
