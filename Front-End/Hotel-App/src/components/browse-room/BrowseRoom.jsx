import React from "react";
import "./bRoom.css";
import { Swiper, SwiperSlide, useSwiper } from "swiper/react";
import "swiper/css";
import { useQuery } from "@tanstack/react-query";
import { getAllRooms } from "../../utils/API-functions-Hotels";

function BrowseRoom() {
  //get all data

  const { isPending, error, data } = useQuery({
    queryKey: ["getALlRoom"],
    queryFn: getAllRooms,
    // placeholderData: keepPreviousData,
  });

 

  if (isPending) return "Loading...";

  return (
    <>
      <div className="browseRoomContainer">
        <Swiper
          spaceBetween={50}
          slidesPerView={4}
          breakpoints={{
            250: {
              slidesPerView: 1,
            },
            450: {
              slidesPerView: 1,
            },
            600: {
              slidesPerView: 2,
            },
            750: {
              slidesPerView: 3,
            },
            1100: {
              slidesPerView: 4,
            },
          }}
        >
          <SliderButton />
          {data?.map((each, i) => (
            <SwiperSlide key={i}>
              <div className="newOne">
                <div>
                    <div className="demoRooms" style={{marginLeft:"2rem"}}>
                    <figure>
                      <img src={each.imageurl} alt="gf" />
                    </figure>
                    <div className="info">
                      <p className="brRoom">{each.roomtype}</p>
                      <p>{each.roomprice}/night</p>
                      <button className="brRoom" style={{ fontSize: "1rem" }}>
                       View/Book NOW
                      </button>
                    </div>
                  </div>
                </div>
              </div>
            </SwiperSlide>
          ))}
        </Swiper>
      </div>
    </>
  );
}

export default BrowseRoom;

let SliderButton = () => {
  let swiperC = useSwiper();
  return (
    <>
      <div className="butS">
        <button
          onClick={() => {
            swiperC.slidePrev();
          }}
        >
          &lt;
        </button>
        <button
          onClick={() => {
            swiperC.slideNext();
          }}
        >
          &gt;
        </button>
      </div>
    </>
  );
};
