var exec = require('cordova/exec');

var LivePreview = {
    getLivePreview: function(callback, error) {
        exec(function(image) {
                callback(image);
            },
            function(error) {
                error(error);
            },
            'LivePreview',
            'getLivePreview');
    }
}

module.exports = LivePreview;