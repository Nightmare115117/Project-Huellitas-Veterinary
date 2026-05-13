a#!/usr/bin/fish

sudo pacman -Syu

set DIR "$HOME/Documentos/programa/Project-Huellitas-Veterinary"

if test -d $DIR
    echo "El proyecto ya existe, actualizando..."
    cd $DIR
    git pull
else
    mkdir -p ~/Documentos/programa
    cd ~/Documentos/programa
    git clone https://github.com/Nightmare11512/Project-Huellitas-Veterinary
end

sudo pacman -S --noconfirm  --needed git fnm mariadb jdk21-openjdk avahi nss-mdns
sudo systemctl enable --now avahi-daemon

# Habilitar mDNS en nsswitch
if not grep -q "mdns_minimal" /etc/nsswitch.conf
    sudo sed -i 's/resolve/mdns_minimal resolve/' /etc/nsswitch.conf
end

fnm env --use-on-cd | source

set BACKEND "$HOME/Documentos/programa/Project-Huellitas-Veterinary/devops-proyect"
set FRONTEND "$HOME/Documentos/programa/Project-Huellitas-Veterinary/devops-frontend"

cd $FRONTEND

fnm install 24
fnm use 24
npm install
npm run dev -- --host </dev/null > /tmp/frontend.log 2>&1 &

if not test -d /var/lib/mysql/mysql
    sudo mariadb-install-db --user=mysql --basedir=/usr --datadir=/var/lib/mysql
end

# Configurar insensibilidad a mayúsculas en MariaDB
set MARIADB_CONF "/etc/my.cnf.d/server.cnf"
if not grep -q "lower_case_table_names" $MARIADB_CONF
    echo "Configurando insensibilidad a mayúsculas en MariaDB..."
    printf '\n[mysqld]\nlower_case_table_names=1\n' | sudo tee -a $MARIADB_CONF > /dev/null
end

sudo systemctl start mariadb

sudo mariadb -e "
CREATE DATABASE IF NOT EXISTS huellitas_db;
CREATE USER IF NOT EXISTS 'Admin'@'localhost' IDENTIFIED BY 'a123b123';
GRANT ALL PRIVILEGES ON huellitas_db.* TO 'Admin'@'localhost';
FLUSH PRIVILEGES;
"

echo "[client]
user=Admin
password=a123b123" > ~/.my.cnf

chmod 600 ~/.my.cnf

set BACKUP_DIR "$HOME/backups"

if test -d "$BACKUP_DIR"
        set BACKUP_FILES (ls -t $BACKUP_DIR/huellitas_*.sql 2>/dev/null)
        if test -n "$BACKUP_FILES"
            set LATEST_BACKUP $BACKUP_FILES[1]
            echo "📁 Backup encontrado: $LATEST_BACKUP"
            echo "🔄 Restaurando..."
            mariadb huellitas_db < "$LATEST_BACKUP"
            if test $status -eq 0
                echo "Backup restaurado exitosamente."
            else
                echo "Error al restaurar backup."
            end
        else
            echo "No se encontraron archivos de backup. Spring creará las tablas vacías."
        end
    else
        echo "No existe directorio ~/backups. Spring creará las tablas vacías."
    end

cd $BACKEND

./mvnw spring-boot:run </dev/null > /tmp/backend.log 2>&1 &

echo "✅ Frontend corriendo - logs en /tmp/frontend.log"
echo "✅ Backend corriendo - logs en /tmp/backend.log"
echo "🌐 Frontend: http://localhost:5173 o http://$hostname.local:5173"
echo "🚀 Backend: http://localhost:8080 http://$hostname.local:5173"
