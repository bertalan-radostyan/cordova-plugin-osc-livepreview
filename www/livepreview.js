var exec = require('cordova/exec');

var LivePreview = {
    getLivePreview: function(ip, callback, error) {
        exec(function(image) {
                callback(image);
            },
            function(error) {
                error(error);
            },
            'LivePreview',
            'getLivePreview', [ip]);
    }
}

module.exports = LivePreview;