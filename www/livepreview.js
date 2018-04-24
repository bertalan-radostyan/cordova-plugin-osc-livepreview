var exec = require('cordova/exec');
var Rx = require('rxjs/Rx');
var Observable = Rx.Observable;

var LivePreview = {
    getLivePreview: function(ip) {
        return Observable.create(function(observer) {
            exec(function(image) {
                    observer.next(image);
                },
                function(error) {
                    observer.error(error);
                },
                'LivePreview',
                'getLivePreview');
        });
    }
}

module.exports = LivePreview;