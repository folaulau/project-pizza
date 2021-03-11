

/**
 * Constant
 */
var HOST = "http:localhost:8888/api";

function generateUUID(){
    var dt = new Date().getTime();
    var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
        var r = (dt + Math.random()*16)%16 | 0;
        dt = Math.floor(dt/16);
        return (c=='x' ? r :(r&0x3|0x8)).toString(16);
    });
    return uuid;
}

function addTokenToLocalStorage(token, apiSession){
    localStorage.setItem("token",token);
    localStorage.setItem("apiSession",JSON.stringify(apiSession));
}

function getTokenFromLocalStorage(){
   return localStorage.getItem("token");
}

function getUserUuidFromLocalStorage(){
    var apiSession = jQuery.parseJSON(localStorage.getItem("apiSession"));
    return apiSession.uuid;
 }