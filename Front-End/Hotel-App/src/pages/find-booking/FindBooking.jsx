import React, { useRef, useState, useContext } from "react";

import { deleteBookedRoom, getBookingRoomCID } from "../../utils/API-functions-Bookings";
import { toast } from "react-toastify";
import { AuthContext } from "../../context/authContext";
import "./findbooking.css";

const FindBooking = () => {

  let [bookingData, SetBookingData] = useState(null);
  let [errorMSG, SetErrorMSG] = useState('');
  let ref = useRef(null);

  
  //no credential navigate to sign-in page 
   let { isAuthenticated, Setbol_login} = useContext(AuthContext);

   let isAuthentcate = isAuthenticated();
   //console.log(falg)
  
  if(!isAuthentcate==true){
     //go to login page
     Setbol_login(true);

  }
 


  let OnChangeFindBooking =async()=>{
    if(ref.current.value.length===7){
      let bookedRoom = await getBookingRoomCID(ref.current.value);
      if(bookedRoom===undefined){
        SetErrorMSG("No Such Booking with given Confirmation Number is avaiable as of now..")
      }else{
         SetErrorMSG('')
         SetBookingData(bookedRoom);
      }
    }else{
      SetBookingData(null);
      SetErrorMSG("No Such Booking with given Confirmation Number is avaiable as of now..")
    }
  } 


  let FindBooking =async(e)=>{
     e.preventDefault();

     if(ref.current.value===''){
      return;
     }else{
     let bookedRoom = await getBookingRoomCID(ref.current.value);
      if(bookedRoom===undefined){
        SetErrorMSG("No Such Booking with given Confirmation Number is avaiable as of now..")
      }else{
       SetErrorMSG('');
       SetBookingData(bookedRoom);
      }     
     }
  }



  let DeleteBooking = async(id)=>{
    let flag = await deleteBookedRoom(id);
    //console.log(flag)
    if(flag===true){
     toast.success('deleted sucessfully')
     SetBookingData(null)
     ref.current.value = '';
     //remove the field
    }else{
      toast.error("some-thing went wrong")
    } 
 }
 
  
  return (
    <>
      <div className="findBookingContainer">
        <div className="search-Box">
          <h2>Find My Booking</h2>
          <form action="#">
            <input type="text" name="" id="" ref={ref}  onChange={OnChangeFindBooking}/>
            <button onClick={(e)=>FindBooking(e)}>Find booking</button>
          </form>
        </div>
        <div className="BookingContent-Box">
          {/* error msg */}
          {errorMSG.length>1?  ( <span>{errorMSG}</span>) : undefined  }
     
          { bookingData?.bid? (<h3>Booking Information</h3>): undefined}
          
         {
          bookingData?.bid?
          (
            <section>
              <span>Booking ID: <strong>{bookingData.bid}</strong></span>
              <span>Confirmation Code: <strong>{bookingData.confirmation_code}</strong></span>
              <span>Room ID: <strong>{bookingData.room_id}</strong> </span>
              <span>Room Type: <strong>{bookingData.room_type}</strong> </span>
              <span>Check-In-Date: <strong>{bookingData.check_in_date}</strong></span>
              <span>Check-Out-Date: <strong>{bookingData.check_out_date}</strong></span>
              <span>Full Name: <strong>{bookingData.full_name}</strong></span>
              <span>Email Address: <strong>{bookingData.email}</strong></span>
              <span>Adults: <strong>{bookingData.adults}</strong></span>
              <span>Children: <strong>{bookingData.children}</strong></span>
              <span>Total Guest: <strong>{bookingData.total_guest}</strong></span>
              <button onClick={()=>DeleteBooking(bookingData.bid)}>Cancel Booking</button>
          </section>
          ) : undefined
          }
         
        </div>
      </div>
    </>
  );
};

export default FindBooking;
