$(document).ready(function(){

    // menu is static

    // load deals
    loadDeals();

    function loadDeals(){
        $.ajax({
            url: HOST+'/products',
            type: 'GET',
            headers: {
            },
            contentType: "application/json"
        })
        .then(function(data) {
            console.log("data");
            console.log(data);
        })
        .catch(function(jqXHR){
            console.log(jqXHR);
            var data = jQuery.parseJSON(jqXHR.responseText);
            console.log(data.message);
            console.log(jqXHR.status);
        });
    }

    
    
});