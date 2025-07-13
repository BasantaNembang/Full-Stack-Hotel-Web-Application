import React, {useContext, useRef, useState} from 'react'
import "./login.css"
import { useForm } from 'react-hook-form';
import { checkOTP, loginUser, sendOtpByEmail, updateForgotPassWord } from '../../utils/API-functions-Users';
import { toast } from 'react-toastify';
import { AuthContext } from '../../context/authContext';
import Cookies from 'universal-cookie';


function Login({Setbol_login}) {

    let form = useForm();
    let {register, handleSubmit, formState} = form;
    let {errors} = formState;
    let [flag, SetFlag ]= useState(false);

    let myRef = useRef();
    let myRefForUpdatePsw = useRef();
    let refLostYourPSW = useRef();
    let forgotRef = useRef(null);
    let updatePasswordFiled0 = useRef();
    let updatePasswordFiled1 = useRef();
    let myRefForOTP = useRef();
    let refOTP = useRef();

    let [formData, SetformData] = useState({
          username:"",
          password:"",
    });
    let [custumErrorMSG, SetcustumErrorMSG] = useState("");

    let cookes = new Cookies();

    let tarckSignUp = (e) =>{
     let {value, name} = e.target;
     SetformData({...formData, [name]:value})
    }
    
   
    let {SetjwtToken } = useContext(AuthContext);
    

    //login user
    let loginIN = async () =>{
    let jwtToken = await loginUser(formData);
      if(jwtToken){
          toast.success("Login Successfully");
          console.log(jwtToken)
          SetformData({
           username:"",
           password:""});
          
          cookes.set("jwtToken", jwtToken, { path: "/", maxAge:1500});  //25mins
          SetjwtToken(jwtToken)
          setTimeout(() => {
            Setbol_login(false);
        }, 3700);
        }
      else{
          toast.error("Login Failed");
      }
  }


    let showForgetPassword = () =>{
        myRef.current.style.display="block"   
        SetFlag(true)     
    }

    let removeForgetPassword = () =>{
      //remove the send mail section
       myRef.current.style.display="none"
       SetFlag(false)
       //clear the filed
        forgotRef.current.value="";
    }

    let showOtpBOX = () =>{
      //remove the send mail section
       myRef.current.style.display="none"
       SetFlag(false)

       //show enter OTP section
       myRefForOTP.current.style.display="block" 

    }


   let postForgotPASSWORD = async() =>{
     if(forgotRef.current.value===""||null){
          SetcustumErrorMSG("Please Enter Your Email")
     }else{
       let flag = await sendOtpByEmail(forgotRef.current.value);
       if(flag===true){
        SetcustumErrorMSG("")
        showOtpBOX();
        SetFlag(true)  //block the input fleld of login
        refLostYourPSW.current.style.display="none"   //remove the forgot your password btn
        toast.success("Otp is valid for 5 mins, Please Check Your mail")
        SetcustumErrorMSG("");
        }else{
          SetcustumErrorMSG("Not Found!!  UserName is not correct or not Sign-IN")
        } 
      }
   }
   
     

   let submitOTP = async() =>{
    if(refOTP.current.value===""||null){
        SetcustumErrorMSG("Please Enter Your OTP")
    }else{
      let response = await checkOTP(refOTP.current.value);
      if(response===true){
        //remove the otp-section
         myRefForOTP.current.style.display="none"
        //show the change password section
        myRefForUpdatePsw.current.style.display="block";
        SetcustumErrorMSG("")
      }else{
        SetcustumErrorMSG("Not Found  OTP not Matched");
      }
    }

   }

  let updatePassWordFunction = async() =>{
     if(updatePasswordFiled0.current.value!==updatePasswordFiled1.current.value){
         SetcustumErrorMSG("PassWord did`t macth")
     }else{
       let flag = await updateForgotPassWord(updatePasswordFiled0.current.value);
       if(flag===true){
          //remove forgot password section
           myRefForUpdatePsw.current.style.display="none"
          //get the forgot password section BTN
           refLostYourPSW.current.style.display="block" 
           toast.success("PassWord updated successfully");
           SetcustumErrorMSG("");
       }else{
        SetcustumErrorMSG("Retry again")
       }
     }

   }


   return (
    <>
      <div className="loginConatiner" >
        <h3>Sign In</h3>
          <form action="#" onSubmit={handleSubmit(loginIN)}>
            <fieldset disabled={flag}>
              <div className="basic-info">
                  <div className="basic-info-left">
                    <div className="child-input-login">
                      <label htmlFor="f-name">Email</label> <br />
                      <input type="text" id="f-name"  value={formData.username}  {...register("username", {required:{value:true, message:"Enter Your Email"}})} onChange={tarckSignUp}/>
                      { !flag? (<span className="errorClass" style={{display:"flex"}}>{errors.username?.message}</span>) : undefined}
                    </div>

                    <div className="child-input-login">
                      <label htmlFor="l-name">Password</label> <br />
                      <input type="text"  id="l-name"  value={formData.password}  {...register("password", {required:{value:true, message:"Enter Your Password"}})} onChange={tarckSignUp}/>
                      { !flag? (<span className="errorClass" style={{display:"flex"}}>{errors.password?.message}</span>) : undefined}
                    </div>
                </div>
              </div>
            </fieldset>

            <button type='submit'>Log In</button>

            <p id='lostPp' onClick={showForgetPassword} ref={refLostYourPSW}>Lost Your Password?</p>

            {/* for forget PassWord -> send OTP by entrying email */}
            <div className="forGetPassword" style={{display:"none"}} ref={myRef}>
              <fieldset  >
                  <div className="child-input-login">
                    <label htmlFor="forgot_password">Email Address</label> <br />
                    <input type="email"    id="forgot_password" ref={forgotRef} />
                    <span className="errorClass" style={{display:"flex"}}>{custumErrorMSG.length>0? custumErrorMSG : undefined}</span>    
                  </div>

                  <button onClick={postForgotPASSWORD}>Recovery</button>
              </fieldset>  
                  <p id='btLogin' onClick={removeForgetPassword}>Back to Login</p>
            </div>

          {/* form to check OTP */}
          <div className='CheckOTP-Box' style={{display:"none"}} ref={myRefForOTP}>
             <div>
                  <label htmlFor="otp">Enter OTP</label> <br />
                  <input type="number"  id="otp" ref={refOTP}/><br />
                  <span className="errorClass">{custumErrorMSG.length>0? custumErrorMSG : undefined}</span>    <br />
                  <button onClick={submitOTP}>Submit OTP</button>
             </div>
          </div>   
        

          {/* enter password to change */}
           <div className="forUpdatePassWord" style={{display:"none"}} ref={myRefForUpdatePsw}>
            <span>Update Your PassWord</span>
              <fieldset>
                 <div>
                   <input type="password" name="" id="" placeholder='Enter Your New Password' ref={updatePasswordFiled0}/>
                 </div>
                 <div>
                   <input type="password" name="" id="" placeholder='Enter Your New Password'ref={updatePasswordFiled1}/>
                 </div>
                 <span className="errorClass" style={{display:"flex"}}>{custumErrorMSG.length>0? custumErrorMSG : undefined}</span>    
 
                 <button onClick={updatePassWordFunction}>Update PassWord</button>
              </fieldset>
 
           </div>
            
          </form>
      </div>
    
    </>
  )
}

export default Login;


