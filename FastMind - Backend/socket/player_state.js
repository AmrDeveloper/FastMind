/**
 * Simple Module to Change user state on database 
 * Online and Offline
 */

const database = require("../database/config.js")

/**
 * Description : Make this user state is online
 * @param {*} username : User Username to make this user state Online
 */
function makeStateOnLine(username){
    var user = [username]
    var updateQuery = "UPDATE player SET online = 1 WHERE username = ?"
    var user = [username]
    database.query(updateQuery,user, (err, results, rows) => {
        if (err) {
            throw err
        }
    })
}

/**
 * Description : Make this user state is off line
 * @param {*} username : User Username to make this user state Offline
 */
function makeStateOffLine(username){
    var user = [username]
    var updateQuery = "UPDATE player SET online = 0 WHERE username = ?"
    var user = [username]
    database.query(updateQuery,user, (err, results, rows) => {
        if (err) {
            throw err
        }
    })
}


/**
 * Description : Make this user state is playing so he cant play now
 * @param {*} username : User Username to make this user state Online
 */
function makeStatePlaying(username){
    var updateQuery = "UPDATE player SET playing = 1 WHERE username = ?"
    var user = [username]
    database.query(updateQuery,user, (err, results, rows) => {
        if (err) {
            throw err
        }
    })
}


/**
 * Description : Make this user state is not playing so he can play now
 * @param {*} username : User Username to make this user state Online
 */
function makeStateFree(username){
    var updateQuery = "UPDATE player SET playing = 0 WHERE username = ?"
    var user = [username]
    database.query(updateQuery,user, (err, results, rows) => {
        if (err) {
            throw err
        }
    })
}

//Export Make Online/Offline method
module.exports.makeStateOnLine = makeStateOnLine
module.exports.makeStateOffLine = makeStateOffLine

//Export Make Playing/not Playing method
module.exports.makeStatePlaying = makeStatePlaying
module.exports.makeStateFree = makeStateFree