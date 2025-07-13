import React from "react";
import "./dateRome.css"


function DateSection() {
  return (
    <>
      <div className="dateSectionContainer">
        <div className="dateChild">
          <p>Check-In-Date</p>
          <input type="datetime-local" name="" id="" />
        </div>
        <div className="dateChild">
          <p>Check-Out-Date</p>
          <input type="datetime-local" name="" id="" />
        </div>

        <div className="dateChild">
          <label for="cars">Room Types :</label> <button type="submit">Search</button><br />
          <select name="cars" id="cars">
            <option value="volvo">Select Room Type</option>
               <option  value="Single room">Single room</option>
                <option value="Double room">Double room</option>
                <option value="Triple room">Triple room</option>
                <option value="Queen room">Queen room</option>
                <option value="King room">King room</option>
                <option value="Twin room">Twin room</option>
                <option value="Deluxe room">Deluxe room</option>
          </select>
        </div>
      </div>
    </>
  );
}

export default DateSection;
