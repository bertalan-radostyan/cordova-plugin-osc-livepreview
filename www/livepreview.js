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
    },
    stopLivePreview: function(callback, error) {
        exec(function() {
            callback();
        }, function(err) {
            error(err);
        },
        'LivePreview',
        'stopLivePreview');
    }
}

module.exports = LivePreview;