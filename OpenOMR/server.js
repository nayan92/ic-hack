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

app.get('/ls', function(req, res) {
  var child = spawn('ls', ['-al']);
  child.stdout.pipe(res);
})

app.post('/picupload', function(req, res) {
	console.log(new Date());
	console.log(JSON.stringify(req.files));
	fs.readFile(req.files.music.path, function (err, data) {
	  var newPath = __dirname + "/uploads/"+nextFileCounter+".jpg";
	  fs.writeFile(newPath, data, function (err) {
	    var child = spawn('bash', ['translate.sh', 'uploads/'+nextFileCounter+".jpg", nextFileCounter++, 'http://129.31.195.224:'+server.address().port]);
	    child.on("exit", function(exitCode){
	    	if(exitCode < 0){
	    		res.end("SAT");
	    	}
	    });
	    child.stdout.pipe(res);
	  });
	});
});


app.use(express.static(__dirname + '/'));
