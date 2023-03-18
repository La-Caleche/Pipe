/* SeedRanks

You can write your SQL migration here.
Please be sure that your migration work before pushing them to git.
You can run your migration by using DatabaseMigration artifact and running
`java -jar DatabaseMigration.jar migrate`
*/

createPlayerRank {
	INSERT INTO ranks (slug, color_code, is_default, created_at, updated_at) VALUES ('player', '#f4f2e5', true, DEFAULT, DEFAULT);
}

createAdminRank {
	INSERT INTO ranks (slug, color_code, created_at, updated_at) VALUES ('admin', '#f05454', DEFAULT, DEFAULT);
}

createVipRank {
	INSERT INTO ranks (slug, color_code, created_at, updated_at) VALUES ('vip', '#f1c40f', DEFAULT, DEFAULT);
}

createViipRank {
	INSERT INTO ranks (slug, color_code, created_at, updated_at) VALUES ('viip', '#9b59b6', DEFAULT, DEFAULT);
}

createStaffRank {
	INSERT INTO ranks (slug, color_code, created_at, updated_at) VALUES ('staff', '#1abc9c', DEFAULT, DEFAULT);
}