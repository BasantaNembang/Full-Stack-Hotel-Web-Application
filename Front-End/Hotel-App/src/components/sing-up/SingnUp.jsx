import React, { useState } from "react";
import "./singup.css";
import { useForm } from "react-hook-form";
import { addUser } from "../../utils/API-functions-Users";
import { toast } from "react-toastify";



function SingnUp({Setbol_login}) {

  let form = useForm();
  let {register, handleSubmit, formState} = form;
  let {errors} = formState;

  
  let [formData, SetformData] = useState({
      fname:"",
      lname:"",
      address:"",
      number:"",
      email:"",
      gender:"",
      username:"",
      password:"",
  });
  
  let tarckSignUp = (e)=>{
    let {name, value} = e.target;
    SetformData({...formData, [name]:value})
  }
  
  

  let signUP = async() =>{
     let flag = await addUser(formData); 
     if(flag){
        toast.success("User Created Successfully");
        SetformData({
          fname:"",
          lname:"",
          address:"",
          number:"",
          email:"",
          gender:"",
          username:"",
          password:"",});

        setTimeout(() => {
            Setbol_login(false);
        }, 3700);
     }  
    
  }

  

  return (
    <>
      <div className="contanier-SingUp" style={ errors.fname? { minHeight: "42rem" } : { height: "35rem" }  }>
        <h3>Sign-Up</h3>
        <form action="#" onSubmit={handleSubmit(signUP)}>
          <div className="basic-info">
              <div className="basic-info-left">
                <div className="child-input">
                  <label htmlFor="f-name">Frist Name</label> <br />
                  <input type="text" id="f-name"  value={formData.fname}  {...register("fname", {required:{value:true, message:"Enter Frist Name"}})} onChange={tarckSignUp}/>
                </div>
                <span className="errorClass">{errors.fname?.message}</span>


                <div className="child-input">
                  <label htmlFor="l-name">Last Name</label> <br />
                  <input type="text" value={formData.lname} id="l-name"  {...register("lname", {required:{value:true, message:"Enter Last Name"}})} onChange={tarckSignUp}/>
                </div>
                <span className="errorClass">{errors.lname?.message}</span>

                <div className="child-input">
                  <label htmlFor="address">Address</label> <br />
                  <input type="text" value={formData.address} id="address" {...register("address", {required:{value:true, message:"Enter Address"}})} onChange={tarckSignUp}/>
                </div>
                <span className="errorClass">{errors.address?.message}</span>

              </div>


              <div className="basic-info-right">
                <div className="child-input">
                  <label htmlFor="contact">Contact Number</label> <br />
                  <input type="number" value={formData.number}  id="f-name"  {...register("number",  {required:{value:true, message:"Enter Number"},
                   minLength:{value:9, message:"Enter atleast 10 number"},
                   maxLength:{value:10, message:"Enter atleast 10 number"}})} onChange={tarckSignUp}/>
              </div>
              <span className="errorClass">{errors.number?.message}</span>


              <div className="child-input">
                  <label htmlFor="email" >Email Address</label> <br />
                  <input type="email" value={formData.email} id="email" {...register("email", {required:{value:true, message:"Enter Email"}})} onChange={tarckSignUp}/>
              </div>
              <span className="errorClass">{errors.email?.message}</span>  
            </div>

          </div>

          <p id="gender">Gender :</p>
          <div className="radio-info"   >
               <div className="d-flex">
                 <label htmlFor="male">Male</label>
                 <input type="radio"  id="male" name="gender" value="male" checked={formData.gender==="male"} 
                  {...register("gender", {required:{value:true, message:"Selet Gender"}})} onChange={tarckSignUp}/>
               </div>

              <div className="d-flex">  
                <label htmlFor="female">Female</label>
                <input type="radio" id="female" name="gender" value="female" checked={formData.gender==="female"} 
                 {...register("gender", {required:{value:true, message:"Selet Gender"}})} onChange={tarckSignUp}/>
              </div>
              
              <div className="d-flex">
                <label htmlFor="other">Other</label>  
                <input type="radio" id="other" name="gender" value="other"  checked={formData.gender==="other"} 
                 {...register("gender", {required:{value:true, message:"Selet Gender"}})} onChange={tarckSignUp}/>
              </div>

          </div>
          <span className="errorClass">{errors.gender?.message}</span>  


          <div className="auth-info">
               <div>
                  <label htmlFor="u-name">User Name</label> <br />
                  <input type="email" value={formData.username} id="u-name" {...register("username", {required:{value:true, message:"Enter Your Enail"}})} onChange={tarckSignUp}/>
                  <span className="errorClass" style={{display:"flex"}}>{errors.username?.message}</span>  
                </div>

                <div>
                  <label htmlFor="password">Password</label> <br />
                  <input type="text" value={formData.password} id="password" {...register("password", {required:{value:true, message:"Enter Password"}})} onChange={tarckSignUp}/>
                  <span className="errorClass" style={{display:"flex"}}>{errors.password?.message}</span>
                </div>
          </div>
          
          <button>Register</button>
        </form>
      </div>
    </>
  );
}

export default SingnUp;
