import React, {  useContext, useRef } from "react";
import "./modal.css";
import SingnUp from "../../components/sing-up/SingnUp";
import Login from "../../components/login/Login";
import { AuthContext } from "../../context/authContext";

function Modal({ Setbol_login }) {
    
  //let [flag, SetFlag] = useState(false);
  let ref = useRef(null);
  let ref0 = useRef(null);

  let {flag, SetFlag} = useContext(AuthContext);

  let removeModel =()=>{
    if(Setbol_login === undefined){
      ref.current.style.display = "none";
      ref0.current.style.display = "none";
    }
     Setbol_login(false)
  }


  return (
    <>
      <div className="model-wrapper" ref={ref0}/>
      <div className="modal-container"  ref={ref} >
        <div className="static-m-container">
          <span id="removeForm" onClick={() => removeModel()}>x</span>
          <ul>
            <li onClick={()=>SetFlag((prev)=>!prev)} className={flag ? 'active-modal' : ''}>LOGIN</li>
            <li onClick={()=>SetFlag((prev)=>!prev)} className={!flag ? 'active-modal' : ''} >REGISTER</li>
          </ul>
          <hr />
        </div>

        <div className="dynamic-m-container">
            {
                flag? <Login Setbol_login={Setbol_login} /> :   <SingnUp Setbol_login={Setbol_login} />
            }
           
        </div>
      </div>
    </>
  );
}

export default Modal;
