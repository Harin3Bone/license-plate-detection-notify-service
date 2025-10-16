PG_USER=<POSTGRES_ROOT_USER>
PG_DATABASE=postgres
PG_INIT_DATABASE=detection_notification
SCRIPT_PATH=/var/lib/scripts

psql -U $PG_USER -d $PG_DATABASE -f $SCRIPT_PATH/01_init_db.sql
psql -U $PG_USER -d $PG_INIT_DATABASE -f $SCRIPT_PATH/02_install_plugin.sql
psql -U $PG_USER -d $PG_INIT_DATABASE -f $SCRIPT_PATH/03_init_table.sql
psql -U $PG_USER -d $PG_INIT_DATABASE -f $SCRIPT_PATH/04_init_user.sql
psql -U $PG_USER -d $PG_INIT_DATABASE -f $SCRIPT_PATH/05_init_demo_data.sql
psql -U $PG_USER -d $PG_INIT_DATABASE -f $SCRIPT_PATH/06_init_geography.sql
