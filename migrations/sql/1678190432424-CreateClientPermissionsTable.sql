/* CreateClientPermissionsTable

You can write your SQL migration here.
Please be sure that your migration work before pushing them to git.
You can run your migration by using DatabaseMigration artifact and running
`java -jar DatabaseMigration.jar migrate`
*/

create table client_permissions
(
    client_id     int null,
    permission_id int null,
    constraint client_permissions_ranks_id_fk
        foreign key (client_id) references clients (id)
            on update cascade on delete cascade,
    constraint client_permissions_permissions_id_fk_2
        foreign key (permission_id) references permissions (id)
            on update cascade on delete cascade
);

create unique index client_permissions_rank_id_permission_id_uindex
    on client_permissions (client_id, permission_id);
