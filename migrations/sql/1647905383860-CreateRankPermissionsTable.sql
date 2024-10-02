/* CreateRankPermissionsTable

You can write your SQL migration here.
Please be sure that your migration work before pushing them to git.
You can run your migration by using DatabaseMigration artifact and running
`java -jar DatabaseMigration.jar migrate`
*/

create table rank_permissions
(
    rank_id       int null,
    permission_id int null,
    constraint rank_permissions_ranks_id_fk
        foreign key (rank_id) references ranks (id)
            on update cascade on delete cascade,
    constraint rank_permissions_permissions_id_fk_2
        foreign key (permission_id) references permissions (id)
            on update cascade on delete cascade
);

create unique index rank_permissions_rank_id_permission_id_uindex
    on rank_permissions (rank_id, permission_id);
