var exec = require('cordova/exec');

var LivePreview = {
    getLivePreview: function(callback) {
        exec(function(image) {
                callback(image);
            },
            function(error) {
                callback(error);
            },
            'LivePreview',
            'getLivePreview');
    }
}

module.exports = LivePreview;