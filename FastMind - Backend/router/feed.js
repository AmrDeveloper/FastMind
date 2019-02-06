const express = require('express')
const router = express.Router()
const database = require("../database/config.js")


/**
 * Request Type : GET
 * Description  : Get all feeds in database
 */
router.get('/api/feed', (req, res) => {
    let sqlQuery = "SELECT * FROM  feed"

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
    let id = req.query.id

    let sqlQuery = "SELECT * FROM feed WHERE id = ?"

    database.query(sqlQuery, id, (err, result, rows) => {
        if (err) { throw err }

        let playersArray = JSON.stringify(result)
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
 * Description  : Get All Feed sorted by score
 */
router.get("/api/feed/sorted/score", (req, res) => {
    let sqlQuery = "SELECT * FROM feed ORDER BY score DESC"

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
 * Request Type : POST
 * Description  : Insert one feed into database
 */
router.post("/api/feed/insert", (req, res) => {
    //Query Attribute
    let player1 = req.query.player1
    let player2 = req.query.player2
    let result = req.query.result
    let score = req.query.score

    let feed = [
        [player1, player2, result, score]
    ]

    let sqlQuery = "INSERT INTO feed(player1,player2,result,score) VALUES ?"

    database.query(sqlQuery, [feed], (err, results, rows) => {
        if (err) { throw err }
        res.status(200).end()
    })
})

/**
 * Request Type : GET
 * Description  : Get All Feeds in database that match search
 */
router.get("/api/feed/search", (req, res) => {
    var keyword = req.query.keyword

    var sqlQuery = "SELECT * FROM feed WHERE  "
        + "player1 LIKE '%" + keyword + "%' "
        + "OR player2 LIKE '%" + keyword + "%'"

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
 * Request Type : DELETE
 * Description  : Delete one feed on database based on id
 */
router.delete("/api/feed/delete", (req, res) => {
    let id = req.query.id;

    let sqlQuery = "DELETE FROM feed WHERE id = ?"

    database.query(sqlQuery, email, (err, res, rows) => {
        if (err) { throw err }
    })
    res.status(200).send("Feed Deleted")
    res.end()
})

/**
 * Request Type : DELETE
 * Description  : Delete one feed on database 
 */
router.delete("/api/feeds/delete/all", (req, res) => {
    let sqlQuery = "truncate table feed"
    database.query(sqlQuery, (err, result) => {
        if (err) { throw err }
    })
    res.status(200).send("Delete Done")
    res.end()
})

//Export Feed Router
module.exports = router;