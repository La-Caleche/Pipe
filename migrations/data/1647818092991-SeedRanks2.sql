/* SeedRanks2

You can write your SQL migration here.
Please be sure that your migration work before pushing them to git.
You can run your migration by using DatabaseMigration artifact and running
`java -jar DatabaseMigration.jar migrate`
*/
changeStaffColor {
    UPDATE ranks SET color_code = '#e91e63' WHERE slug = 'staff';
}

createMemberRank {
	INSERT INTO ranks (slug, color_code, created_at, updated_at) VALUES ('member', '#1abc9c', DEFAULT, DEFAULT);
}