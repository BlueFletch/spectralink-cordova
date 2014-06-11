var cordova = require('cordova');
var exec = require('cordova/exec');

function BarcodeScanner() {

}
/**
 * Start listening for the barcode scanner
 */
BarcodeScanner.prototype.register = function (onScan, fail) {
    exec(onScan, fail, 'SpectralinkBarcode', 'register', []);
};

/**
 * Stop listening for barcode scans
 */
BarcodeScanner.prototype.unregister = function () {
    exec(null, null, 'SpectralinkBarcode', 'unregister', []);
};




//create instance
var barcodeScanner = new BarcodeScanner();

module.exports = barcodeScanner;