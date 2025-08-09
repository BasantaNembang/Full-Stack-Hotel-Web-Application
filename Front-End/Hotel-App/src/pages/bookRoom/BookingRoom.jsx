import React, {useContext, useEffect, useRef, useState} from 'react'
import './bookingRoom.css'
import { NavLink, useLocation } from 'react-router-dom'
import { getSingelData } from '../../utils/API-functions-Hotels';
import { useQuery } from "@tanstack/react-query";
import SummaryReserv from '../../components/summaryReservation/SummaryReserv';
import { useForm } from 'react-hook-form';
import { toast } from 'react-toastify';
import { CiWifiOn } from "react-icons/ci";
import { FaTv } from "react-icons/fa";
import { MdOutlineFreeBreakfast } from "react-icons/md";
import { FaCar } from "react-icons/fa";
import { LuCircleParking } from "react-icons/lu";
import { GiLoincloth } from "react-icons/gi";
import { AuthContext } from '../../context/AuthContext';

function BookingRoom() {
    
   let form = useForm();
   let {register, handleSubmit, formState} = form;
   let {errors} = formState;
  
  
    let [reserveRoomInfo, SetreserveRoomInfo] = useState({
            full_name:"",
            email:"",
            check_in_date:"",
            check_out_date:"",
            room_id:"",
            adults:0,
            children:0,
            room_price:0,
            total_guest:0,
       });


  let [falg, Setfalg] = useState(false);
  let [sRfalg, SetfalgSR] = useState(false);
  let [reserveRoomPrice, SetreserveRoomPrice] = useState(0);
  let [totalDaysBooked, SetTotalDaysBooked] = useState(0);
  let summaryBookRef = useRef(null);
 

   let location = useLocation();

   let hid = location.state;
  
    const { isPending, error, data } = useQuery({
      queryKey: ["getSingelRoom", hid],
      queryFn: ()=>getSingelData(hid),
    });
  
  
  //no credential navigate to sign-in page 
   let { isAuthenticated, Setbol_login} = useContext(AuthContext);

   let isAuthentcate = isAuthenticated();
  
  if(!isAuthentcate==true){
     //go to login page
     Setbol_login(true);

  }

  

  
  let trackFeiled =(e)=>{
    let {name, value} = e.target;
     SetreserveRoomInfo({...reserveRoomInfo, [name]:value})
  }



  let reserveRoomINFO = () =>{
        
     let checkInDate = new Date(reserveRoomInfo.check_in_date);
     let checkOutDate = new Date(reserveRoomInfo.check_out_date);
   
     let currentDateY = new Date()
     
     
     if(checkInDate < currentDateY){
        toast.error("Check-In-Date should be greater than current date")
        return;
     }
    
     //get total number of days booked
       let totalDaysBooked = checkOutDate-checkInDate
       let finalTotalDays = Math.ceil(totalDaysBooked / (1000 * 60 * 60 * 24));
       SetTotalDaysBooked(finalTotalDays);
    

      if(checkInDate > checkOutDate){
        toast.error("Check-In-Date should be less than Check-Out-Date")
        return;
      }
  
       SetreserveRoomInfo({...reserveRoomInfo, room_price:data.roomprice*finalTotalDays,
        room_id: data.hid,
        total_guest: Number(reserveRoomInfo.adults) + Number(reserveRoomInfo.children)
       }); 
       Setfalg(true);
       SetfalgSR(true);  


   //scroll to summary section
   summaryBookRef.current?.scrollIntoView({ behavior: "smooth" })

}

 let updateRoomPrice =()=>{
    let checkInDate = new Date(reserveRoomInfo.check_in_date);
    let checkOutDate = new Date(reserveRoomInfo.check_out_date);

    let currentDateY = new Date()


    if(checkInDate < currentDateY){
       toast.error("Check-In-Date should be greater than current date");
       SetfalgSR(false);        //disable the sumarryReserv
       return;
    }
    
    if(checkInDate > checkOutDate){
       toast.error("Check-In-Date should be less than Check-Out-Date")
       SetfalgSR(false);
       return;
    }
    
    //get total number of days booked
    let totalDaysBooked = checkOutDate-checkInDate
    let finalTotalDays = Math.ceil(totalDaysBooked / (1000 * 60 * 60 * 24));
    SetTotalDaysBooked(finalTotalDays);
    

    if(data!=undefined){
    SetreserveRoomInfo({...reserveRoomInfo, room_price:data.roomprice*finalTotalDays,
      //room_id: data.hid,
       }); 
  }   
  
   SetfalgSR(true);
 }

  //on date changes price also changes 
  useEffect(()=>{
    updateRoomPrice()
  }, [reserveRoomInfo.check_in_date, reserveRoomInfo.check_out_date]);

 

  if (isPending) return "Loading...";

  if(error) return "An error has occurred: " + error.message;

  
  return (
    <>
     <div className="bookingRoom">
     <NavLink to="/browse" > <button>Back</button>   </NavLink>

        <div className="infoSectionbookingRoom">
            <div className="informationRoom">
              <span>{data.roomtype}</span><br />
                <figure>
                    <img src={data.imageurl} alt="" />
                </figure><br />
                <div className="detailsRoom">
                  <table>
                    <tbody>
                      <tr>
                      <td><strong>Room Type</strong></td>
                      <td>{data.roomtype}</td>
                    </tr>
                    <tr>
                      <td><strong>Price Per Night</strong></td>
                      <td>{data.roomprice}</td>                    
                    </tr>
                    <tr>
                      <td><strong>Services</strong></td>
                      <td className='iconService'> 
                        <span><CiWifiOn />wifi</span>
                        <span><FaTv />tv</span>
                        <span><MdOutlineFreeBreakfast />coffe/tea</span>
                        <span><FaCar />car service</span>
                        <span><LuCircleParking />free parking</span>
                        <span><GiLoincloth />laundry</span>
                      </td>                     
                    </tr>   
                   </tbody>                              
                  </table>
               </div>          
            </div>

            <div className="informationBooking">
                <fieldset>
                    <form action="#" onSubmit={handleSubmit(reserveRoomINFO)}>
                         <h4 style={{fontSize:"2rem"}}>Reserve Room</h4>  

                         <div className="topSectionInfoBook">
                            <div className="topSectionInfoBook1">
                              <label htmlFor="full_name">Full Name :</label><br />
                              <input type="text" value={reserveRoomInfo.full_name} id="full_name" 
                              {...register("full_name", {required:{value:true, message:"Enter FullName"}})} onChange={trackFeiled}/>
                            </div>
                            <span className="errorClass">{errors.full_name?.message}</span>

                            <div className="topSectionInfoBook1">
                              <label htmlFor="email">Email :</label><br />
                              <input type="email" value={reserveRoomInfo.email}   id="email"
                               {...register("email", {required:{value:true, message:"Enter Email"}})} onChange={trackFeiled}/>
                            </div>
                            <span className="errorClass">{errors.email?.message}</span>

                         </div>    

                         <div className="buttonSectionInfoBook">
                           <span style={{fontSize:"1.6rem"}}>Lodging Period</span>  

                           <div className="buttonSectionConatiner">
                                <div className="buttonSectionInfoBook1">
                                  <label htmlFor="check_in_date">Check In :</label><br />
                                  <input type="date" value={reserveRoomInfo.check_in_date}  id="check_in_date" 
                                   {...register("check_in_date", {required:{value:true, message:"Enter Your Check-In-Date "}})}  onChange={trackFeiled}/>
                                <span className="errorClass">{errors.check_in_date?.message}</span>
                                </div>
                                                  


                                <div className="buttonSectionInfoBook1">
                                  <label htmlFor="check_out_date">Check Out :</label><br />
                                  <input type="date" value={reserveRoomInfo.check_out_date}  id="check_out_date"
                                  {...register("check_out_date", {required:{value:true, message:"Enter Your Check-Out-Date "}})}  onChange={trackFeiled}/>
                                 <span className="errorClass">{errors.check_out_date?.message}</span>
                                </div>
                              

                           </div> 
                         </div> 

                          
                         <div className="buttonSectionInfoBook" style={{marginTop:"2rem"}}>
                           <span style={{fontSize:"1.6rem"}}>Number of Guest</span>  

                           <div className="buttonSectionConatiner">
                                <div className="buttonSectionInfoBook1">
                                  <label htmlFor="adults">Adults </label><br />
                                  <input type="number" value={reserveRoomInfo.adults}   id="adults" 
                                   {...register("adults", {required:{value:true, message:"Enter Number of adults "}})} onChange={trackFeiled}/>
                                <span className="errorClass">{errors.adults?.message}</span>
                                </div>
                                <div className="buttonSectionInfoBook1">
                                  <label htmlFor="checkout">Children</label><br />
                                  <input type="number" value={reserveRoomInfo.children} id="children"
                                    {...register("children", {required:{value:true, message:"Enter Number of children"}})} onChange={trackFeiled}/>
                                <span className="errorClass">{errors.children?.message}</span>
                               </div>
                           </div>  
                         </div>   

                     <button type='submit' id='submitBTN'>Continue</button>      
                              
                    </form>
                </fieldset>
            </div>
      
        </div>
       
       <div ref={summaryBookRef}>
        {
          falg? (<SummaryReserv reserveRoomInfo={reserveRoomInfo} totalDaysBooked={totalDaysBooked} SetreserveRoomInfo={SetreserveRoomInfo} Setfalg={Setfalg} sRfalg={sRfalg}/>  ) : undefined 
        }
        
       </div>

       </div>
    
    </>
  )
}

export default BookingRoom;



