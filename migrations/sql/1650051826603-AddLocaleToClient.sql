/* AddLocaleToClient

You can write your SQL migration here.
Please be sure that your migration work before pushing them to git.
You can run your migration by using DatabaseMigration artifact and running
`java -jar DatabaseMigration.jar migrate`
*/

addLocaleId {
    alter table clients
        add locale_id int null;
}

addFkToLocale {
    alter table clients
        add constraint clients_locales_id_fk
            foreign key (locale_id) references locales (id)
                on update cascade on delete set null;
}