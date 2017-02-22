# MwMServer
#sqlplus /nolog
Oracle12c创建新用户会提示公共用户名或角色无效，建议使用11g
　　SQL> conn / as sysdba;

　　SQL>create user username identified by password

　　SQL> grant dba to username;

　　SQL> conn username/password
