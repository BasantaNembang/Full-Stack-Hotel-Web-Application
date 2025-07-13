import React, { useRef } from "react";
import "./nav.css";
import { FiAlignJustify } from "react-icons/fi";
import { GrClose } from "react-icons/gr";
import { NavLink } from "react-router";



function Navbar({ bolean, Setbolean, Setbol_login}) {

  let myRef = useRef();


  let clickedBurger = () =>{
      if(myRef.current){
        Setbolean((prev)=>!prev);}
  }


  let tarckClicked =()=>{
    // console.log("HI there ",bolean)
    if(bolean){
      Setbolean((prev)=>!prev);} 
   }
   


  return (
    <>
      <div className="navContainer" style={{height: bolean? "38rem": " ", marginLeft: bolean? "10%": " ", width: bolean? "90%": " "}} onClick={tarckClicked}>
        <div className={!bolean ? "navItem" : "navRespons"}>
          <div className="leftNav">
            <ul>
              <NavLink className={(e)=>{return e.isActive? "" : "no-addUnderLine"}} to="/"><b>BN</b> Hotel</NavLink>
              <NavLink className={(e)=>{return e.isActive? "" : "no-addUnderLine"}} to="/browse">Browse All Rooms</NavLink>
              <NavLink  className={(e)=>{return e.isActive? "" : "no-addUnderLine"}} to="/admin" >Manage Rooms "ADMIN"</NavLink>
            </ul>
          </div>

          <div className="rightNav">
            <ul>
             <NavLink className={(e)=>{return e.isActive? "" : "no-addUnderLine"}} to="/find-booking"><li>Find My Bookings</li></NavLink>
              
              <li onClick={()=>Setbol_login(true)}>Accounts</li>
            </ul>
          </div>
        </div>
        <div className="burgerIcon" ref={myRef} onClick={clickedBurger}  >
          {
             !bolean ? <FiAlignJustify /> :
              <GrClose  />
          }
        </div>
      </div>
    </>
  );
}

export default Navbar;
