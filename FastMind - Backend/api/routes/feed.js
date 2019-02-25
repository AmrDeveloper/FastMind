const express = require('express')
const router = express.Router()
const database = require("../../database/config.js")


/**
 * Request Type : GET
 * Description  : Get all feeds in database
 */
router.get('/api/feeds', (req, res) => {
    let sqlQuery = "SELECT * FROM feed"

    database.query(sqlQuery, (err, results, rows) => {
        if (err) { throw err }

        let feedsArray = JSON.stringify(results)
        feedsArray = JSON.parse(feedsArray)

        let feedsJSONArray = {}

        feedsJSONArray.result = {
            number: feedsArray.length,
            feeds : feedsArray,
        }

        res.status(200).json(feedsJSONArray)
    })
})

/**
 * Request Type : GET
 * Description  : Get one feed in database using username attribute
 */
router.get('/api/feed', (req, res) => {
    let id = req.query.id

    let sqlQuery = "SELECT * FROM feed WHERE id = ?"

    database.query(sqlQuery, id, (err, result, rows) => {
        if (err) { throw err }

        let feedsArray = JSON.stringify(result)
        feedsArray = JSON.parse(feedsArray)

        let feedsJSONArray = {}

        feedsJSONArray.result = {
            number: feedsArray.length,
            feeds : feedsArray,
        }

        res.status(200).json(feedsJSONArray)
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

        let feedsArray = JSON.stringify(results)
        feedsArray = JSON.parse(feedsArray)

        let feedsJSONArray = {}

        feedsJSONArray.result = {
            number: feedsArray.length,
            feeds : feedsArray,
        }
        res.status(200).json(feedsJSONArray)
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
        if(results.changedRows === 0){
            res.status(404).send("failure").end()
        }else{
            res.status(200).send("success").end()
        }
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

        var feedsArray = JSON.stringify(results)
        feedsArray = JSON.parse(playersArray)

        var feedsJSONArray = {}

        feedsJSONArray.result = {
            number: feedsArray.length,
            feeds : feedsArray,
        }
        res.status(200).json(feedsJSONArray)
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
        if(results.changedRows === 0){
            res.status(404).send("failure").end()
        }else{
            res.status(200).send("success").end()
        }
    })
})

/**
 * Request Type : DELETE
 * Description  : Delete one feed on database 
 */
router.delete("/api/feeds/delete/all", (req, res) => {
    let sqlQuery = "truncate table feed"
    database.query(sqlQuery, (err, result) => {
        if (err) { throw err }
        if(results.changedRows === 0){
            res.status(404).send("failure").end()
        }else{
            res.status(200).send("success").end()
        }
    })
})

//Export Feed Router
module.exports = router;