const app = require('express')()
const server = require('http').Server(app)
const socketIO = require('socket.io')(server)
const game = require("./socket/game_socket")

//Get Routers
const playerRouter = require("./api/routes/player.js")
const feedRouter = require("./api/routes/feed.js")

//Use Routers
app.use(playerRouter)
app.use(feedRouter)

//Run Game Socket IO
game.runSocket(socketIO)

//Server PORT
const port = process.env.PORT || 3000

//Listen for 3000 port
server.listen(port, () => {
    console.log(`Server running at PORT ${port}`);
})
