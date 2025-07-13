import React, {  useEffect, useState } from "react";
import "./manageroom.css";
import { keepPreviousData, useQuery } from "@tanstack/react-query";
import { getPaginationData, getPaginationDataType, getSingelData } from "../../utils/API-functions-Hotels";



function ManageRooms({SetHotelDto,  SetflagAddRoom, SetupdateRoomBool}) {

 
  let [pageNumber, SetpageNumber] = useState(0);
  let [pageNumber0, SetpageNumber0] = useState(0);
  let [AllData, SetAllData] = useState([]);
  let [Rommtype, SetRoomtype] = useState({ roomtype:""});

  
  
  //tanstack for all type
  const { isPending, error, data } = useQuery({
    queryKey: ['repoData', pageNumber],
    queryFn:()=> getPaginationData(pageNumber, 5),
    staleTime:10000,
    placeholderData:keepPreviousData
  });



  //tanstack for custom type 
   const { data:data0 } = useQuery({
    queryKey: ['repoDataType', pageNumber0],
    queryFn:()=> getPaginationDataType(pageNumber, 5, Rommtype.roomtype),
    enabled:pageNumber0>0,   //disable the initail quering
    staleTime:10000,
    placeholderData:keepPreviousData});
  

  useEffect(()=>{
    SetAllData(data)
    if(pageNumber0>0){
      SetAllData(data0)
    }
   }, [data, pageNumber, pageNumber0, data0]);
  


  let addNewRoom =() =>{   //provide a form to add new Room(Admin)
      SetflagAddRoom((prev)=>!prev);
      SetupdateRoomBool(true);
      SetHotelDto({});
  }


  // view and edit the Room
  let viewAndEditRoom = async(id)=>{
    
    SetflagAddRoom((prev)=>!prev);
    SetupdateRoomBool(false);

    let data  = await getSingelData(id);
 
    if(data.hid){  //!!
      SetHotelDto(data);
    }
  }



  //on change 
  let getRoom = (e) =>{
   // SetpageNumber((prev)=>prev+1);

    let {value, name} = e.target;
    SetRoomtype({...Rommtype, [name]: value });
    SetpageNumber(0)
    SetpageNumber0((prev)=>prev+1);
  }
   

 let updatePaginationUp = () =>{

      if(Rommtype.roomtype!==""){  //it for custom type
        SetpageNumber((prev)=>prev+1)
        SetpageNumber0((prev)=>prev+1)
       }else{    //its for all type
        SetpageNumber((prev)=>prev+1)
       }
 }


 let updatePaginationDown = () =>{

  if(Rommtype.roomtype!==""){  //it for custom type
    SetpageNumber0((prev)=>prev-1);
    SetpageNumber((prev)=>prev-1);
    //console.log(AllData)
   }else{    //its for all type
    SetpageNumber((prev)=>prev-1)
   }
    
 }


  let clearALlFilter = () =>{
    SetpageNumber(0)
    SetpageNumber0(0)
    SetAllData(data);
    SetRoomtype({
      roomtype:""
    })
  }

  
  if (isPending) return 'Loading...'

  if (error) return 'An error has occurred: ' + error.message



  return (
    <>
      <div className="manageRoomContainer">
        <div className="topSection">
          <div className="topSectionLeft">
            <h3>Existing Rooms</h3>
            <select  id="filterBuRoom" name="roomtype" value={Rommtype.roomtype} onChange={(e)=>getRoom(e)}>
                <option value="">Select a room type </option>
                <option  value="Single room">Single room</option>
                <option value="Double room">Double room</option>
                <option value="Triple room">Triple room</option>
                <option value="Queen room">Queen room</option>
                <option value="King room">King room</option>
                <option value="Twin room">Twin room</option>
                <option value="Deluxe room">Deluxe room</option>
            </select>
            <button type="submit" onClick={clearALlFilter}>Clear</button>
          </div>
          <div className="topSectionRight" onClick={addNewRoom}>    
            {/* the add functionality */}
            <u>+ add</u>
          </div>
        </div>
        <div className="tabelSection">
          <table>
            <thead>
              <tr>
                <th>ID</th>
                <th>Room type</th>
                <th>Room price</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody>
                {
                  AllData?.map((each, i)=>(
                    <tr key={i}>
                      <td data-column="First Name">{each.hid}</td>
                      <td data-column="Last Name">{each.roomtype}</td>
                      <td data-column="Job Title">{each.roomprice}</td>
                      <td data-column="Twitter"><button onClick={()=>viewAndEditRoom(each.hid)}>View/Edit</button>  <button>Delete</button>  </td>
                    </tr>
                  ))                  
                }
            </tbody>
          </table>
        </div>

        <div className="btnManageRoom">
                <button disabled={pageNumber === 0 ? true : false} onClick={updatePaginationDown}>Prev</button>
                <button style={{width:"3rem"}}>{pageNumber+1}</button>
                <button disabled={AllData?.length<5}  onClick={updatePaginationUp}>Next</button>
        </div>
      </div>
    </>
  );
}

export default ManageRooms;
