const state = require("./player_state")

function runSocket(io) {
    //Run When player is connected to server
    io.on('connection', (socket) => {
        
        let username = ""
        //Make this username online state is true
        socket.on('username', (name) => {
            username = name
            console.log("OnLine : " + username)
            state.makeStateOnLine(name)
        })

        /**
         * Description : for Requests type like send, accept , refuse Challenges
         * Hint : When one player emit server should emit to all players 
         * expect the sender
         * 
         * Username : Sender 
         * Player   : Receiver
         */
        socket.on('request', (state, player) => {
            if (state === 'send') {
                socket.broadcast.emit('request', 'receive', username, player)
            } else if (state === 'accept') {
                //emit to player2 that player accept challenge
                socket.broadcast.emit('request', 'accept', username, player)
                //emit game to send question to two players
            } else if (state === 'refuse')
                socket.broadcast.emit('request', 'refuse', username, player)
        })

        /**
         * Description : for MultiPlayers Activity sockets
         *               to control game state
         * 
         */
        socket.on('play', (state, sender, reciver,message) => {
            if (state === 'start') {
                socket.broadcast.emit('play', 'start', sender, reciver,message)
            } else if (state === 'answer') {
                socket.broadcast.emit('play', 'answer', sender, reciver,message)
            } else if (state === 'next') {
                //Go To next level 
                socket.broadcast.emit('play', 'next', sender, reciver,message)
            } else if (state === 'end') {
                //Add This information to feed database  
                socket.broadcast.emit('play', 'end', sender, reciver,message)     
            }
        })

        //Run When Player is dis connected from server
        socket.on('disconnect', () => {            
            if (!(username === '')) {
                state.makeStateOffLine(username)
                console.log("Offline : " + username)
            }
        })
    })
}

module.exports.runSocket = runSocket