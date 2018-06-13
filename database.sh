
#连接数据库，初始化
mysql -u root -p  <<EOF

CREATE DATABASE hardware;

USE hardware;

CREATE TABLE info (
    address VARCHAR(30) NOT NULL, 
    cpuUsage FLOAT NOT NULL, 
    memUsage FLOAT NOT NULL, 
    diskSize INT NOT NULL, 
    bandwidth FLOAT NOT NULL, 
    onlineTime FLOAT NOT NULL
    );


INSERT INTO info VALUES ("21117",10.9,17.6,100,100,12);

EOF
