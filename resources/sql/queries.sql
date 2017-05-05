-- :name upsert-template :! :n
-- :doc "Upsert" a leiningen or boot template record
insert into templates (template_name, build_system, description, github_url)
values (:template-name, :build-system, :description, :github-url)
on conflict (template_name, build_system)
do update set build_system = :build-system, description = :description, github_url = :github-url;

-- :name all-templates :? :*
-- :doc Get all templates
select * from templates order by template_name;

-- :name templates :? :*
-- :doc Get all templates for build system
select * from templates where build_system = :build-system;

-- :name delete-all-templates :! :n
delete from templates;