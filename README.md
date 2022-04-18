# SLogin
Simple Minecraft plugin for authentication.
All data is saved in **SQLite** or **MySQL**.
Passwords are encrypted with BCrypt.

You can translate this plugin and add your own language:
https://poeditor.com/join/project?hash=Ti7QHfbKNZ

## Commands

Player Commands:

    /login <pass>
    /register <pass>
    /changepassword <old pass> <new pass>
    /email


Admin Commands:

    /seralogin playerinfo <nick>
    /seralogin forcelogin <nick>
    /seralogin register <nick> <pass>
    /seralogin unregister <nick>
    /seralogin changepassword <nick> <new pass>

## Permissions

* slogin.register - register command
* slogin.login - login command
* slogin.changepassword - changepassword command
* slogin.email - email command
* slogin.admin - administrator commands
* slogin.* - all commans

## Configs

Config file:

    https://github.com/Serafinowy/SLogin/blob/main/src/main/resources/config.yml


Available languages:

    https://github.com/Serafinowy/SLogin/tree/main/src/main/resources/translations


## Discord: Serafin#3535
