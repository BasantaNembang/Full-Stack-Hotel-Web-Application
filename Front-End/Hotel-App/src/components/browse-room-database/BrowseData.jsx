import React from "react";
import "./browsedata.css";

function BrowseData() {
  return (
    <>
      <div className="browseRoomContainer-D">
        <span className="brRoom-D">
          <u>Browse Rooms </u>
        </span>
        <div className="demoRooms-D">
          <figure>
            <img src="Hotel-1.jpg" alt="" />
          </figure>
          <div className="info-D">
            <p className="brRoom-D">New Room Type-1</p>
            <p>$10/night</p>
            <button className="brRoom-D" style={{ fontSize: "1rem" }}>
              Demo
            </button>
          </div>
        </div>

        <div className="demoRooms-D">
          <figure>
            <img src="Hotel-2.jpg" alt="" />
          </figure>
          <div className="info-D">
            <p className="brRoom-D">New Room Type</p>
            <p>$10/night</p>
            <button className="brRoom-D" style={{ fontSize: "1rem" }}>
              Demo
            </button>
          </div>
        </div>

        <div className="demoRooms-D">
          <figure>
            <img src="Hotel-3.jpg" alt="" />
          </figure>
          <div className="info-D">
            <p className="brRoom-D">New Room Test</p>
            <p>$10/night</p>
            <button className="brRoom-D" style={{ fontSize: "1rem" }}>
              Demo
            </button>
          </div>
        </div>

        <div className="demoRooms-D">
          <figure>
            <img src="Hotel-4.jpg" alt="" />
          </figure>
          <div className="info-D">
            <p className="brRoom-D">Room 15</p>
            <p>$10/night</p>
            <button className="brRoom-D" style={{ fontSize: "1rem" }}>
              Demo
            </button>
          </div>
        </div>
      </div>
    </>
  );
}

export default BrowseData;
