alter table custom_user
    add photo_path varchar(255);

alter table custom_user
    alter column photo_path set default 'noUserIcon.png';
