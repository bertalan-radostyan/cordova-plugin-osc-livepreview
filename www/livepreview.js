var exec = require('cordova/exec');

var LivePreview = {
    getLivePreview: function(ip, callback, error) {
        exec(function(image) {
                callback(image);
            },
            function(err) {
                error(err);
            },
            'LivePreview',
            'getLivePreview', [ip]);
    }
}

module.exports = LivePreview;