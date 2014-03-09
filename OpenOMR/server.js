var express = require("express");
var app = express();

var spawn = require('child_process').spawn;
var nextFileCounter = 0;

var server = app.listen(8080, function() {
    console.log('Listening on port %d', server.address().port);
});

app.get('/recogniseMusic', function(req, res){
  var child = spawn('bash', ['translate.sh', nextFileCounter++, 'http://localhost:'+server.address().port]);
  child.stdout.pipe(res);
  
});


app.get('/ls', function(req, res) {
  var child = spawn('ls', ['-al']);
  child.stdout.pipe(res);
})

app.use(express.static(__dirname + '/'));
