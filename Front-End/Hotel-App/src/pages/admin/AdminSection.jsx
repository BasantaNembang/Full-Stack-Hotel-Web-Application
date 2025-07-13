import React, { useEffect, useState } from "react";
import "./admin.css";
import { motion } from "framer-motion";
import ManageRooms from "../../components/manage-room/ManageRooms";
import ViewRoom from "../../components/view-room/ViewRoom";
import ExistingBookings from "../../components/existing-bookings/ExistingBookings";

function AdminSection() {

  let [message, SetMessage] = useState("Admin Home");
  let [flag, Setflag] = useState(false);
  let [flagAddRoom, SetflagAddRoom] = useState(false);//for ManageRooms.jsx
  let [manageBookingBool, SetmanageBookingBool] = useState(true);//for ManageRooms.jsx
 

  //for add tabel ::
  let [updateRoomBool, SetupdateRoomBool] = useState(false);
  let [roomText, SetroomText] = useState("");
  let [HotelDto, SetHotelDto] = useState({});  //store 


  useEffect(()=>{
     if(updateRoomBool){
      SetroomText("Add a New Room")
     }else{
      SetroomText("Update Room")
     }
  }, [updateRoomBool]);
 


  let trackManageRoom = () => {
       Setflag((prev)=>!prev)
       SetMessage("All Rooms")
    }
 
  let trackManageRoom0 = () => {
     Setflag((prev)=>!prev)
     SetmanageBookingBool((prev)=>!prev)
     SetMessage("Existings Rooms")
  }  

    return (
    <>
      <div className="adminSection">
        {/* the background image sections */}
        <div className="backgroundAdminImage">
          <motion.span
            initial={{ y: "2rem", opacity: 0 }}
            animate={{ y: 0, opacity: 1 }}
            transition={{
              duration: 3,
              type: "spring",
            }}
            key={message}
          >
            {message}
          </motion.span>
        </div>
        
        {/* changes */}
    

         {/* the content tabel filter and so on..... */}
           
        
        {
         flag && flagAddRoom ? <ViewRoom HotelDto = {HotelDto}  roomText={roomText}  SetflagAddRoom={SetflagAddRoom}/> :
         
         flag ? ( manageBookingBool ?  (<ManageRooms SetHotelDto = {SetHotelDto}   SetflagAddRoom={SetflagAddRoom} SetupdateRoomBool={SetupdateRoomBool}/>) : <ExistingBookings />  )     :  
         
         
         (
            <div className="adminSectionWelcome">
             <h2>Welcome to admin Panel</h2>
              <ul>
                <button  onClick={trackManageRoom} ><u>Manage rooms</u></button> <br />
                <button onClick={trackManageRoom0} ><u>Manage booking</u></button>
              </ul>
            </div>
           )
        }


        {/* changes */}

         

      </div>
    </>
  );
}

export default AdminSection;
