//Here is the all backend function for bookings
import axios from "axios";

export const Api = axios.create({
  baseURL: "http://localhost:9090/bookings",
});

//function to booked room
export async function bookedRoom(bookingInfo) {
  let response = null;
  //console.log(bookingInfo)
  try {
    response = await Api.post("/place", bookingInfo, {
      headers: { "Content-Type": "application/json" },
    });
    console.log(response);
    if (response.status === 202 || response.status === 200) {
      if (response?.data.msg === "Room has been booked already...") {
        return response.data.msg;
      } else {
        return true;
      }
    }
  } catch (error) {
    console.error("error  ", error);
    return false;
  }
}

//functions to get all bookings-rooms
export async function getBookedRooms() {
  let response = null;
  try {
    response = await Api.get("/get-all");
    if (response.status === 200) {
      return response.data;
    } else {
      return [];
    }
  } catch (error) {
    console.error("error  ", error);
  }
}

//functions to delete
export async function deleteBookedRoom(id) {
  let response = null;
  try {
    response = await Api.delete(`/delete/${id}`);
    //console.log(response);
    if (response.status === 200) {
      return true;
    }
  } catch (error) {
    console.error("error  ", error);
    return false;
  }
}

//function to get booking via confirmation code
export async function getBookingRoomCID(confirmID) {
  let response = null;
  try {
    response = await Api.get(`/get/confirm-number/${confirmID}`);
    if (response.status === 200) {
        return response.data;
     } else {
      return [];
    }
  } catch (error) {
    console.error("error  ", error);
  }
}


