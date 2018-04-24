var exec = require('cordova/exec');
var Observable = require('rxjs/Observable');

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