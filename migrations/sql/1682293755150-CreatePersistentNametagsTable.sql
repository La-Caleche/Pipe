/* CreatePersistentNametagsTable

You can write your SQL migration here.
Please be sure that your migration work before pushing them to git.
You can run your migration by using DatabaseMigration artifact and running
`java -jar DatabaseMigration.jar migrate`
*/

createTable {
    create table persistent_nametags
    (
        id          int auto_increment,
        client_id   int                                 not null,
        index_order int                                 not null,
        raw_text    text                                not null,
        created_at  timestamp default current_timestamp null,
        update_at   timestamp default current_timestamp null,
        constraint persistent_nametags_pk
            primary key (id),
        constraint persistent_nametags_clients_id_fk
            foreign key (client_id) references clients (id)
                on delete cascade
    );
}