import React from 'react'
import './summaryReserv.css'
import { toast } from 'react-toastify';
import { bookedRoom } from '../../utils/API-functions-Bookings';


function SummaryReserv({reserveRoomInfo, totalDaysBooked, SetreserveRoomInfo, Setfalg, sRfalg}) {

  let submitRoomBooking = async(e) => {
    e.preventDefault();
     

    if(sRfalg){
    let flag = await bookedRoom(reserveRoomInfo)
    
    console.log(flag)
    if(flag === true){
    toast.success("Booking Confirmed")
    setTimeout(() => {
      SetreserveRoomInfo({
        full_name:"",
        email:"",
        check_in_date:"",
        check_out_date:"",
        adults:"",
        children:"",
        roomPrice:0
      });
      Setfalg(false);
    }, 3500);}
    else{
      toast.error(flag)
    }
   
  }
  

  }
    return (
      <>
        <div className="summaryReservContainer" >
          <form action="#" onClick={submitRoomBooking} >
              <div className="summaryReserve">
                <h2>Reserve Summary</h2>
                <div className="summaryDetails">
                    <span>Name : <strong>{reserveRoomInfo.full_name}</strong></span>
                    <span>Email : <strong>{reserveRoomInfo.email}</strong></span>
                    <span>Check-in-Date : <strong>{reserveRoomInfo.check_in_date}</strong></span>
                    <span>Check-out-Date : <strong>{reserveRoomInfo.check_out_date}</strong></span>
                    <span>Number of Days Booked : <strong>{totalDaysBooked}</strong></span>
                </div>
                <div className="summaryDetails">
                    <p><b>Number of Guest</b></p>
                    <span>Adults : <strong>{reserveRoomInfo.adults}</strong></span>
                    <span>Childern : <strong>{reserveRoomInfo.children}</strong></span>
                </div>
                <span>Total Payment : <strong>{reserveRoomInfo.room_price}</strong></span>
              </div>
              <button id='summaryBTN'>Confirm Booking & procced to payment</button>
            </form>   
        </div>
      </>
  )
}

export default SummaryReserv;

