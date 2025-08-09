//Here is the all backend function for users
import axios from "axios";

export const Api =  axios.create({
   baseURL : "http://localhost:9090/user"
});



//function to add users
export async function addUser(sendData){
    let response = null;
    //console.log(sendData)
     try {
        response = await Api.post("/sign-up", sendData, {
            headers:{"Content-Type":"application/json"}
        });
        //console.log(response)
        if(response.status===200){
            return true;
        }else{
            return false;}}
        catch (error) {console.error(error)}
}



//function to login users
export async function loginUser(sendData){
    let response = null;
     try {
        response = await Api.post("/login", sendData, {
            headers:{"Content-Type":"application/json"}
        });
        //console.log(response)
        if(response.status===200){
            return response.data;
        }else{
            return [];}}
        catch (error) {console.error(error)
    }
}


//function to send OTP
export async function sendOtpByEmail(username) {
    let response = null;
    try {
      response = await Api.post("/send-otp/"+username);
      if(response.status===200){
        return true;
      }  
    } catch (error) {
        console.error(error);
    }
    
}


//function to check OTP
export async function checkOTP(otp) {
    let response = null;
    try {
        response = await Api.post("/check-otp/" + otp);
        if(response.status===200){
        return true;
      }  
    } catch (error) {
        console.error(error)
    } 
}

//function to update Password 
export async function updateForgotPassWord(password) {
    let response = null;
     try {
        response = await Api.put("/update-password/" + password);
        if(response.status===200){
        return true;
      }  
    } catch (error) {
        console.error(error)
    } 
}


