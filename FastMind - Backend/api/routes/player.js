const express = require('express')
const router = express.Router()
const database = require("../../database/config.js")

/**
 * Request Type : GET
 * Description  : Check if player information is valid
 */
router.get('/api/player/login', (req, res) => {
    let email = req.query.email
    let password = req.query.password

    let sqlQuery = `SELECT * FROM player WHERE email = ? AND password = ${password} LIMIT 0, 1`

    let player = [
        [email]
    ]

    database.query(sqlQuery, [player], (err, result, rows) => {
        if (err) { throw err }

        let playersArray = JSON.stringify(result)
        playersArray = JSON.parse(playersArray)

        if (playersArray) {
            res.status(200).json(playersArray[0])
        } else {
            res.status(404).end()
        }
    })
})

/**
 * Request Type : GET
 * Description  : Get all players in database
 */
router.get('/api/players', (req, res) => {
    let sqlQuery = "SELECT * FROM  player"

    database.query(sqlQuery, (err, results, rows) => {
        if (err) { throw err }

        let playersArray = JSON.stringify(results)
        playersArray = JSON.parse(playersArray)

        let playersJSONArray = {}

        playersJSONArray.result = {
            number: playersArray.length,
            players: playersArray,
        }

        res.status(200).json(playersJSONArray)
    })
})

/**
 * Request Type : GET
 * Description  : Get one player in database using username attribute
 */
router.get('/api/player', (req, res) => {
    let username = req.query.username

    let sqlQuery = "SELECT * FROM player WHERE username = ? LIMIT 0, 1"

    database.query(sqlQuery, username, (err, result, rows) => {
        if (err) { throw err }

        let playersArray = JSON.stringify(result)
        playersArray = JSON.parse(playersArray)

        if (playersArray) {
            res.status(200).json(playersArray[0])
        } else {
            res.status(404).end()
        }
    })
})

/**
 * Request Type : GET
 * Description  : Get All online players
 */
router.get("/api/players/online", (req, res) => {
    var sqlQuery = "SELECT * FROM player WHERE online = 1"

    database.query(sqlQuery, (err, results, rows) => {
        if (err) { throw err }

        var playersArray = JSON.stringify(results)
        playersArray = JSON.parse(playersArray)

        var playersJSONArray = {}

        playersJSONArray.result = {
            number: playersArray.length,
            players: playersArray,
        }
        res.status(200).json(playersJSONArray)
    })
})

/**
 * Request Type : GET
 * Description  : Get All offline players
 */
router.get("/api/players/offline", (req, res) => {
    var sqlQuery = "SELECT * FROM player WHERE online = 0"

    database.query(sqlQuery, (err, results, rows) => {
        if (err) { throw err }

        var playersArray = JSON.stringify(results)
        playersArray = JSON.parse(playersArray)

        var playersJSONArray = {}

        playersJSONArray.result = {
            number: playersArray.length,
            players: playersArray,
        }
        res.status(200).json(playersJSONArray)
    })
})

/**
 * Request Type : GET
 * Description  : Get All players with state playing
 */
router.get("/api/players/playing", (req, res) => {
    var sqlQuery = "SELECT * FROM player WHERE playing = 1"

    database.query(sqlQuery, (err, results, rows) => {
        if (err) { throw err }

        var playersArray = JSON.stringify(results)
        playersArray = JSON.parse(playersArray)

        var playersJSONArray = {}

        playersJSONArray.result = {
            number: playersArray.length,
            players: playersArray,
        }
        res.status(200).json(playersJSONArray)
    })
})

/**
 * Request Type : GET
 * Description  : Get All players with state free (not playing)
 */
router.get("/api/players/free", (req, res) => {
    var sqlQuery = "SELECT * FROM player WHERE playing = 0"

    database.query(sqlQuery, (err, results, rows) => {
        if (err) { throw err }

        var playersArray = JSON.stringify(results)
        playersArray = JSON.parse(playersArray)

        var playersJSONArray = {}

        playersJSONArray.result = {
            number: playersArray.length,
            players: playersArray,
        }
        res.status(200).json(playersJSONArray)
    })
})

/**
 * Request Type : GET
 * Description  : Get All players with state free (not playing)
 */
router.get("/api/players/challenge", (req, res) => {
    var sqlQuery = "SELECT * FROM player WHERE playing = 0 AND online = 1"

    database.query(sqlQuery, (err, results, rows) => {
        if (err) { throw err }

        var playersArray = JSON.stringify(results)
        playersArray = JSON.parse(playersArray)

        var playersJSONArray = {}

        playersJSONArray.result = {
            number: playersArray.length,
            players: playersArray,
        }
        res.status(200).json(playersJSONArray)
    })
})

/**
 * Request Type : GET
 * Description  : Get All Players sorted by level
 */
router.get("/api/players/rank/level", (req, res) => {
    let sqlQuery = "SELECT * FROM  player ORDER BY level DESC"

    database.query(sqlQuery, (err, results, rows) => {
        if (err) { throw err }

        let playersArray = JSON.stringify(results)
        playersArray = JSON.parse(playersArray)

        let playersJSONArray = {}

        playersJSONArray.result = {
            number: playersArray.length,
            players: playersArray,
        }
        res.status(200).json(playersJSONArray)
    })
})

/**
 * Request Type : GET
 * Description  : Get All Players sorted by score
 */
router.get("/api/players/rank/score", (req, res) => {
    let sqlQuery = "SELECT * FROM  player ORDER BY score DESC"

    database.query(sqlQuery, (err, results, rows) => {
        if (err) { throw err }

        let playersArray = JSON.stringify(results)
        playersArray = JSON.parse(playersArray)

        let playersJSONArray = {}

        playersJSONArray.result = {
            number: playersArray.length,
            players: playersArray,
        }
        res.status(200).json(playersJSONArray)
    })
})

/**
 * Request Type : GET
 * Description  : Get Top 10 players from database 
 *                sorted by score and level 
 */
router.get("/api/players/top", (req, res) => {
    let sqlQuery = "SELECT * FROM  player ORDER BY score DESC,level DESC LIMIT 10"

    database.query(sqlQuery, (err, results, rows) => {
        if (err) { throw err }

        let playersArray = JSON.stringify(results)
        playersArray = JSON.parse(playersArray)

        let playersJSONArray = {}

        playersJSONArray.result = {
            number: playersArray.length,
            players: playersArray,
        }
        res.status(200).json(playersJSONArray)
    })
})

/**
 * Request Type : GET
 * Description  : Get All players in database that match search
 */
router.get("/api/players/search", (req, res) => {
    var keyword = req.query.keyword

    var sqlQuery = "SELECT * FROM player WHERE  "
        + "username LIKE '%" + keyword + "%' "
        + "OR email LIKE '%" + keyword + "%'"

    database.query(sqlQuery, (err, results, rows) => {
        if (err) { throw err }

        var playersArray = JSON.stringify(results)
        playersArray = JSON.parse(playersArray)

        var playersJSONArray = {}

        playersJSONArray.result = {
            number: playersArray.length,
            players: playersArray,
        }
        res.status(200).json(playersJSONArray)
    })
})

/**
 * Request Type : GET
 * Description  : Get 5 players in database that match search
 *              : this request just for searchview autocomplete
 */
router.get("/api/players/search/limit", (req, res) => {
    var keyword = req.query.keyword

    var sqlQuery = "SELECT * FROM player WHERE  "
        + "username LIKE '%" + keyword + "%' "
        + "OR email LIKE '%" + keyword + "%' LIMIT 5"

    database.query(sqlQuery, (err, results, rows) => {
        if (err) { throw err }

        var playersArray = JSON.stringify(results)
        playersArray = JSON.parse(playersArray)

        var playersJSONArray = {}

        playersJSONArray.result = {
            number: playersArray.length,
            players: playersArray,
        }
        //playersJSONArray.players = playersArray;

        res.status(200).json(playersJSONArray)
    })
})

/**
 * Request Type : POST
 * Description  : Insert one player into database
 */
router.post("/api/player/insert", (req, res) => {
    //Query Attribute
    let username = req.query.username
    let email = req.query.email
    let password = req.query.password
    //Default Attribute
    let score = 0
    let level = 1
    let online = 1
    let playing = 0
    let winNumber = 0
    let loseNumber = 0
    let avatarId = 0

    let player = [
        [username, email, password, score, level, online, playing,winNumber,loseNumber,avatarId]
    ]

    let sqlQuery = "INSERT INTO  player(username,email,password,score,level,online,playing,winNum,loseNum,avatarID) VALUES ?"

    database.query(sqlQuery, [player], (err, results, rows) => {
        if (err) { throw err }
        console.log(results)
        console.log(rows)
        if(results.insertId === 0){
            res.status(404).send("failure").end()
        }else{
            res.status(200).send("success").end()
        }
    })
})

/**
 * Request Type : put
 * Update Player Score and level on database
 */
router.put("/api/player/sync", (req, res) => {
    let username = req.query.username
    let score = req.query.score
    let level = req.query.level

    let sqlQuery = "UPDATE player SET score = " + score + ", level = " + level + " WHERE username = ?"

    database.query(sqlQuery, username, (err, results, rows) => {
        if (err) {
            throw err
        }
        if (results.changedRows === 0) {
            res.status(404).send("failure").end()
        } else {
            res.status(200).send("success").end()
        }
    })
})

/**
 * Request Type : put
 * Update Player avatar id on database
 */
router.put("/api/player/update/avatar", (req, res) => {
    let email = req.query.email
    let avatar = req.query.avatar

    let sqlQuery = "UPDATE player SET avatarID = " + avatar + " WHERE email = ?"

    database.query(sqlQuery, email, (err, results, rows) => {
        if (err) {
            throw err
        }
        if (results.changedRows === 0) {
            res.status(404).send("failure").end()
        } else {
            res.status(200).send("success").end()
        }
    })
})

/**
 * Request Type : put
 * Update Player username on database
 */
router.put("/api/player/update/username", (req, res) => {
    let email = req.query.email
    let username = req.query.username

    let sqlQuery = "UPDATE player SET username = ? WHERE email = ?"

    let info = [
        username,email   
    ]

    database.query(sqlQuery, info, (err, results, rows) => {
        if (err) {
            throw err
        }
        if (results.changedRows === 0) {
            res.status(404).send("failure").end()
        } else {
            res.status(200).send("success").end()
        }
    })
})

/**
 * Request Type : put
 * Update Player Password on database
 */
router.put("/api/player/update/email", (req, res) => {
    let email = req.query.email
    let nemail = req.query.nemail

    let sqlQuery = "UPDATE player SET email = ? WHERE email = ?"

    let info = [
        nemail,email   
    ]

    database.query(sqlQuery, info, (err, results, rows) => {
        if (err) {
            throw err
        }
        if (results.changedRows === 0) {
            res.status(404).send("failure").end()
        } else {
            res.status(200).send("success").end()
        }
    })
})

/**
 * Request Type : put
 * Update Player Password on database
 */
router.put("/api/player/update/pass", (req, res) => {
    let email = req.query.email
    let password = req.query.password

    let sqlQuery = "UPDATE player SET password = ? WHERE email = ?"

    let info = [
        password,email   
    ]

    database.query(sqlQuery, info, (err, results, rows) => {
        if (err) {
            throw err
        }
        if (results.changedRows === 0) {
            res.status(404).send("failure").end()
        } else {
            res.status(200).send("success").end()
        }
    })
})

/**
 * Request Type : put
 * Update Player Avatar Index on database
 */
router.put("/api/player/update/avatar",(req,res)=>{
    let email = req.query.email
    let avatarIndex = req.query.avatar

    let sqlQuery = "UPDATE player SET avatarID = " + avatarIndex + " WHERE email = ?"
    database.query(sqlQuery, email, (err, results, rows) => {
        if (err) {
            throw err
        }
        if (results.changedRows === 0) {
            res.status(404).send("failure").end()
        } else {
            res.status(200).send("success").end()
        }
    })
})

/**
 * Request Type : DELETE
 * Description  : Delete one player on database based on email address
 */
router.delete("/api/player/delete", (req, res) => {
    let email = req.query.email;

    let sqlQuery = "DELETE FROM player WHERE email = ?"

    database.query(sqlQuery, email, (err, results, rows) => {
        if (err) { throw err }
        if (results.changedRows === 0) {
            res.status(404).send("failure").end()
        } else {
            res.status(200).send("success").end()
        }
    })
})

/**
 * Request Type : DELETE
 * Description  : Delete All players on database
 */
router.delete("/api/players/delete/all", (req, res) => {
    let sqlQuery = "truncate table player"
    database.query(sqlQuery, (err, result) => {
        if (err) { throw err }
        if (results.changedRows === 0) {
            res.status(404).send("failure").end()
        } else {
            res.status(200).send("success").end()
        }
    })
})

//Export Player Router
module.exports = router;