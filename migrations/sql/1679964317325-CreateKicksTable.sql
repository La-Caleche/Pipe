/* CreateKicksTable

You can write your SQL migration here.
Please be sure that your migration work before pushing them to git.
You can run your migration by using DatabaseMigration artifact and running
`java -jar DatabaseMigration.jar migrate`
*/

CreateKicksTable {
    create table kicks
    (
        id         int                                 null,
        author_id  int                                 not null,
        client_id  int                                 not null,
        reason     varchar(255)                        null,
        created_at timestamp default current_timestamp null,
        updated_at  timestamp default current_timestamp null,
        constraint kicks_pk
            primary key (id),
        constraint kicks_clients_id_fk
            foreign key (author_id) references clients (id),
        constraint kicks_clients_id_fk2
            foreign key (client_id) references clients (id)
    );
}
