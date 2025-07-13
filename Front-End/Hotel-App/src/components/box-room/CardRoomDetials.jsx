import React from 'react';
import './cardRoom.css';
import { NavLink } from "react-router";

const CardRoomDetials = ({each}) => {
 
    return (
            <div className="cardRoomBox">
               <div className="imageCard">
                  <figure>
                    <img src={each.imageurl} alt="" />
                  </figure>
               </div>
               <div className="detailsCard">
                    <span >{each.roomtype}</span> <br />
                    <span >$ {each.roomprice}</span>
                    <p>Lorem ipsum dolor sit, amet consectetur adipisicing elit. Quaerat consequuntur sint ullam?</p>

                </div>
                 <div className="Card" >
                    <NavLink to="/booking"  state={each.hid} > <button>Book Now</button></NavLink>                                 
                </div>
        </div>
    );
};

export default CardRoomDetials;