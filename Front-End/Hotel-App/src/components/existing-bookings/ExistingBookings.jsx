import React, { useEffect, useState } from "react";
import "./existingbook.css";
import { DateRangePicker } from "react-date-range";
import { useQuery } from "@tanstack/react-query";
import { deleteBookedRoom, getBookedRooms } from "../../utils/API-functions-Bookings";
import { toast } from 'react-toastify';

const ExistingBookings = () => {
  let [stateDate, SetSateDate] = useState(new Date());
  let [endDate, SetEndDate] = useState(new Date());
  let [BookingROOM, SetBookingROOM] = useState([]);
  let [useQ, SetuseQ] = useState(0);

  //get all booked rooms vid DB;
  const { isPending, isError, data, error } = useQuery({
    queryKey: ["getBooked", useQ],
    queryFn: getBookedRooms,
  });

  useEffect(() => {
    SetBookingROOM(data);
  }, [useQ, data]);

  let handleSelect = (ranges) => {
    let filteredData = data?.filter((booking) => {
      let bookingDATE = new Date(booking.check_in_date);
      return (
        bookingDATE >= ranges.selection.startDate &&
        bookingDATE <= ranges.selection.endDate
      );
    });

    SetSateDate(ranges.selection.startDate);
    SetEndDate(ranges.selection.endDate);

    SetBookingROOM(filteredData);
  };

  const selectionRange = {
    startDate: stateDate,
    endDate: endDate,
    key: "selection",
  };


  let cancelBooking = async(id) => {
    let flag = await deleteBookedRoom(id);
    if(flag === true){
     toast.success("Delete SUCESSFULLY");
     //render latest data after delete
     SetuseQ((prev)=>prev+1);
    }else{
      toast.error("failed")
    }
  };


  if (isPending) {
    return <span>Loading...</span>;
  }

  if (isError) {
    return <span>Error: {error.message}</span>;
  }

  return (
    <>
      <div className="existingRoomContainer">
        {/* <h4>Find booking by date</h4> */}

        <DateRangePicker ranges={[selectionRange]} onChange={handleSelect} />

        {/* table */}
        <div className="existingRoomConatinerTable">
          <div className="tabelSection">
            <table>
              <thead>
                <tr>
                  <th id="sn">S/N</th>
                  <th id="booking_id">Booking-ID</th>
                  <th id="room_id">Room-ID</th>
                  <th>Check-In Date</th>
                  <th>Check-Out Date</th>
                  <th>Guest Name</th>
                  <th>Guest Email</th>
                  <th id="adults">Adults</th>
                  <th id="children">Children</th>
                  <th id="total_guest">Total Guest</th>
                  <th>Confirm Code</th>
                  <th>Action</th>
                </tr>
              </thead>
              <tbody>
                {BookingROOM?.map((each, i) => (
                  <tr key={i}>
                    <td>{i + 1}</td>
                    <td>{each.bid}</td>
                    <td>{each.room_id}</td>
                    <td>{each.check_in_date}</td>
                    <td>{each.check_out_date}</td>
                    <td>{each.full_name}</td>
                    <td>{each.email}</td>
                    <td>{each.adults}</td>
                    <td>{each.children}</td>
                    <td>{each.total_guest}</td>
                    <td>{each.confirmation_code}</td>
                    <td id="cancelBooking">
                      <button onClick={() => cancelBooking(each.bid)}>
                        cancel
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </>
  );
};

export default ExistingBookings;
