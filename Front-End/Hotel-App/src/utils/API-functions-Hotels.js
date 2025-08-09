//Here is the all backend function for rooms
import axios from "axios";

export const Api =  axios.create({
   baseURL : "http://localhost:9090/hotel"
});



//function to get all room
export async function getAllRooms(){
    let response = null;
     try {
        response = await Api.get("/get");
        //console.log(response.data)
        if(response.status===200){
            return response.data;}
            else{return [];
        }
     } catch (error) {console.error(error)}
}


//function to add room
export async function addRoom(sendData) {
    
  let response=null;
   try {
    response  =  await Api.post("/add", sendData , {
        headers:{"Content-Type": "multipart/form-data"}});
     } catch (error) { 
      console.log("error-------------->")
      console.error(error)
    }
    if(response.status===200){
        return true;
    }else{
        return false;
    }
 }




 //function to fecth paginations data 
 export async function getPaginationData(pageNumber, pageSize) {
    let response=null;
    try {
    // response  =  await Api.get(`http://localhost:8080/hotel/get-pagination?pageNumber=${pageNumber}&pageSize=${pageSize}`);
     response  =  await Api.get(`/get-pagination?pageNumber=${pageNumber}&pageSize=${pageSize}`);
     //console.log(response)
     if(response.status===202){
         return response.data;
       }else{return [];
     }
   } catch (error) {console.error(error)}

 }


 //function to get single room
 export async function getSingelData(hid){
 
      let response = null;
        try {
          //response = await Api.get(`http://localhost:8080/hotel/get-room/${hid}`);
          response = await Api.get(`/get-room/${hid}`);
          if(response.status===200){
            return response.data;}else{return []  }
        } catch (error) {  console.error(error)
          }
      }



//function to update room
export async function updateSingleRoom(hid, HotelDto){
 
  let response = null;

  try {
    response = await Api.put(`/update/${hid}`, HotelDto, {
      headers:{"Content-Type": "multipart/form-data"} });
      //console.log(response)

      if(response.status === 202){
        return true;
      }
      else{ return false;
      }
  } catch (error) {  console.error(error)  }

}


//function to get pagiantion room by type ||getPaginationDataType
export async function getPaginationDataType(pageNumber, pageSize, type) {
  
  let response = null;
  try {
   // response = await Api.get(`http://localhost:8080/hotel/room/${type}?pageNumber=${pageNumber}&pageSize=${pageSize}`);
    response = await Api.get(`/room/${type}?pageNumber=${pageNumber}&pageSize=${pageSize}`);
    if(response.status === 202){
      return response.data;
    }else{
      return [];
    }
  } catch (error) { console.error(error) }
  
}


