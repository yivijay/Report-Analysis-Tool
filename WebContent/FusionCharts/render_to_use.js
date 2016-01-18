var page = require('webpage').create(),
webpageURLWithChart  = 'bb.html',
outputImageFileName = 'savedImage1.png',
delay = 10000;
page.open(webpageURLWithChart, function (){window.setTimeout(function (){page.render(outputImageFileName);phantom.exit();}, delay);});