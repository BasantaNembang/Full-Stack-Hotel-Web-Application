import React from "react";
import "./ourservice.css";
import { CiWifiOn } from "react-icons/ci";
import { MdOutlineFreeBreakfast } from "react-icons/md";
import { GiClothes } from "react-icons/gi";
import { GiWineGlass } from "react-icons/gi";
import { MdOutlineDirectionsCar } from "react-icons/md";
import { CiIceCream } from "react-icons/ci";

function OurService() {
  return (
    <>
      <div className="ourService">
        <span className="topService">
          Service at BN hotel - 24 - Hour Front Desk
        </span>
        <hr />
        <div className="imageOurService">
          <span>Our Services</span>
        </div>
        <div className="serviceList">
          <div className="service">
            <div className="icon">
              <CiWifiOn className="react-icon" /> <span> : Wifi</span>
            </div>
            <div className="paraService">
              Stay connected with high speed internet access
            </div>
          </div>
          <div className="service">
            <div className="icon">
              <MdOutlineFreeBreakfast className="react-icon" />{" "}
              <span> : Breakfast</span>
            </div>
            <div className="paraService">
              Stay your day with a delicious breakfast buffet
            </div>
          </div>
          <div className="service">
            <div className="icon">
              <GiClothes className="react-icon" /> <span> : laundry</span>
            </div>
            <div className="paraService">
              Keep your clothes clean and fresh with our laundry service
            </div>
          </div>
          <div className="service">
            <div className="icon">
              <GiWineGlass className="react-icon" /> <span> : Mini-bar</span>
            </div>
            <div className="paraService">
              Enjoy a refreshing drink or snack from our in room mini bar
            </div>
          </div>
          <div className="service">
            <div className="icon">
              <MdOutlineDirectionsCar className="react-icon" />{" "}
              <span> : Parking</span>
            </div>
            <div className="paraService">
              Park your car conveniently in our on-site parking lot.
            </div>
          </div>
          <div className="service">
            <div className="icon">
              <CiIceCream className="react-icon" />{" "}
              <span> : Air conditioning</span>
            </div>
            <div className="paraService">
              Stay cool and comfortable with out air conditioning system{" "}
            </div>
          </div>
        </div>
        <hr style={{marginTop:"5rem"}}/>
      </div>
    </>
  );
}

export default OurService;
