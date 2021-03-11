$(document).ready(function(){
    
    $("#signupEmail").val("folaudev+"+generateUUID()+"@gmail.com");
    $("#signupPassword").val("Test1234!");

    $("#signupBtn").click(function(){
        console.log("signing up...");
        var payload = {};
        payload['email'] = $("#signupEmail").val();
        payload['password'] = $("#signupPassword").val();

        $.ajax({
            url: HOST+'/users/signup',
            type: 'POST',
            data: JSON.stringify(payload),
            headers: {
                "x-api-key": 'test-web-api-key'
            },
            contentType: "application/json"
        })
        .then(function(data) {
            console.log("data");
            console.log(data);
            addTokenToLocalStorage(data.token, data);
            console.log("token="+getTokenFromLocalStorage());
            console.log("userUuuid="+getUserUuidFromLocalStorage());
            window.location.href = "/";
        })
        .catch(function(jqXHR){
            console.log(jqXHR);
            var data = jQuery.parseJSON(jqXHR.responseText);
            console.log(data.message);
            console.log(jqXHR.status);
        });
    });
});