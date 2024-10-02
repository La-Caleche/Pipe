/* CreateClientsTable

You can write your SQL migration here.
Please be sure that your migration work before pushing them to git.
You can run your migration by using DatabaseMigration artifact and running
`java -jar DatabaseMigration.jar migrate`
*/

create table clients
(
    id         int auto_increment,
    uuid       varchar(255)                        null,
    constraint clients_pk
        primary key (id)
);

create unique index clients_uuid_uindex
    on clients (uuid);
