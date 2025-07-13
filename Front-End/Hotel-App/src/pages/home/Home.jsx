import React from "react";
import "./home.css";
import DisplayImage from "../../components/displayImage/DisplayImage";
import DateR from "../../components/date-room/DateSection";
import BrowseRoom from "../../components/browse-room/BrowseRoom";
import ExperinceImage from "../../components/experience-image/ExperinceImage";
import OurService from "../../components/our-service/OurService";
import BrowseData from "../../components/browse-room-database/BrowseData";



function Home({ bolean, Setbolean }) {
  return (
    <>
      <div className="homeContainer">
        <DisplayImage bolean={bolean} Setbolean={Setbolean} />
        <DateR />
        <BrowseData />
        <ExperinceImage />
        {/* coming form database ..... */}
        <BrowseRoom />
        <OurService />
      </div>
    </>
  );
}

export default Home;
