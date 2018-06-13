#安装mysql
sudo apt-get update
sudo apt-get install mysql-server
#设置ROOT的密码
sudo mysql -u root <<EOF
use mysql;
UPDATE user SET plugin='mysql_native_password' WHERE user='root';
UPDATE user SET password=PASSWORD('123456') WHERE uesr='root';
flush privileges;
exit;
EOF
