var express = require("express");
var fs = require("fs");
var app = express();


app.configure(function(){
    app.use(express.methodOverride());
    app.use(express.multipart());
   });

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

app.post('/picupload', function(req, res) {
	console.log(JSON.stringify(req.files));
	fs.readFile(req.files.music.path, function (err, data) {	  // ...
	  var newPath = __dirname + "/uploads/"+nextFileCounter;
	  fs.writeFile(newPath, data, function (err) {
	    var child = spawn('bash', ['translate.sh', 'uploads/'+nextFileCounter, nextFileCounter++, 'http://localhost:'+server.address().port]);
	    child.stdout.pipe(res);
	  });
	});
});


app.use(express.static(__dirname + '/'));
