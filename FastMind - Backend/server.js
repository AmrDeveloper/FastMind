const app = require('express')()
const server = require('http').Server(app)
const socketIO = require('socket.io')(server)
const game = require("./socket/game_socket")

//Get Routers
const playerRouter = require("./router/player.js")
const feedRouter = require("./router/feed.js")

//Use Routers
app.use(playerRouter)
app.use(feedRouter)

//Run Game Socket IO
game.runSocket(socketIO)

//Server PORT
const hostname = '192.168.1.9'
const port = 3000

//Listen for 3000 port
server.listen(port, hostname, () => {
    console.log(`Server running at http://${hostname}:${port}/`);
})
