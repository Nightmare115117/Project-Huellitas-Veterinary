#!/usr/bin/fish

cd $HOME/Documentos/
./instalarelproyecto.fish

mariadb -D huellitas_db -e "INSERT IGNORE INTO roles (NOMBRE) VALUES ('Administrador'), ('Cliente'), ('Gestor'), ('Veterinario');"

set BACKUP_DIR "$HOME/backups"
mkdir -p "$BACKUP_DIR"

sudo mariadb-dump  HUELLITAS_DB > "$BACKUP_DIR/huellitas_$(date +%Y-%m-%d).sql"

echo "Backup completo"
