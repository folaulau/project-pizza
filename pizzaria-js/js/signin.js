$(document).ready(function(){
    
    $("#signinEmail").val("folaudev@gmail.com");
    $("#signinPassword").val("Test1234!");

    $("#signinBtn").click(function(){
        console.log("signing up...");
        var email = $("#signinEmail").val();
        var password = $("#signinPassword").val();
        console.log("password="+password);
        console.log("email="+email);
        var authorization = btoa(email+":"+password);

        console.log("authorization - Basic "+authorization);
        
        $.ajax({
            url: HOST+'/users/login?type=password',
            type: 'post',
            data: {
                
            },
            headers: {
                "Authorization": "Basic "+authorization,  
                "x-api-key": 'Header Value Two'
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