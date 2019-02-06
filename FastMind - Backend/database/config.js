const mysql = require('mysql');

const connectionPool = mysql.createPool({
    connectionLimit: 10,
    host: 'localhost',
    user: 'root',
    password: '',
    database: 'fastmind',
})

function getConnection() {
    return connectionPool;
}

module.exports = getConnection();