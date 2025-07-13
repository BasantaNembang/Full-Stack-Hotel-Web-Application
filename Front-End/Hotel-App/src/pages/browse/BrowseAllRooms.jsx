import React, { useEffect, useState } from "react";
import "./browse.css";
import CardRoomDetials from "../../components/box-room/CardRoomDetials";
import { keepPreviousData, useQuery } from "@tanstack/react-query";
import { getPaginationData, getPaginationDataType } from "../../utils/API-functions-Hotels";


function BrowseAllRooms() {

  let [pageNumber, SetpageNumber] = useState(0);
  let [pageNumber0, SetpageNumber0] = useState(0);
  let [AllData, SetAllData] = useState([]);
  let [Rommtype, SetRoomtype] = useState({ roomtype:""});
  


  //tanstack for all type
    const { isPending, error, data } = useQuery({
      queryKey: ['repoData', pageNumber],
      queryFn:()=> getPaginationData(pageNumber, 3),
             
      staleTime:10000,
      placeholderData:keepPreviousData
    });
  
  
  
    //tanstack for custom type 
    const { data:data0 } = useQuery({
      queryKey: ['repoDataType', pageNumber0],
      queryFn:()=> getPaginationDataType(pageNumber, 3, Rommtype.roomtype),
      enabled:pageNumber0>0,   //disable the initail quering
      staleTime:10000,
    placeholderData:keepPreviousData});



   //on change 
  let getRoom = (e) =>{
    let {value, name} = e.target;
    SetRoomtype({...Rommtype, [name]: value });
   
    SetpageNumber(0)
    SetpageNumber0((prev)=>prev+1);
  }
   
   let getNextPage =()=>{
    if(Rommtype.roomtype!==""){  //it for custom type
      SetpageNumber((prev)=>prev+1)
      SetpageNumber0((prev)=>prev+1)
     }else{    //its for all type
      SetpageNumber((prev)=>prev+1)
     }
  }
  

  let getPrevPage =()=>{
    if(Rommtype.roomtype!==""){  
      SetpageNumber0((prev)=>prev-1);
      SetpageNumber((prev)=>prev-1);
     }else{    
      SetpageNumber((prev)=>prev-1)
     }
 }
  
  useEffect(()=>{
    SetAllData(data)
    if(pageNumber0>0){
      SetAllData(data0)
    }
   }, [data, pageNumber, pageNumber0, data0]);

 

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
      <div className="browserRoom">
        <div className="filterSection">
          <div>
            <div className="searchRoomType">
              <span>Filter By Room Types :</span><br />
              <select name="roomtype" id="cars" style={{height:"1.9rem", color:"#000"}}
               value={Rommtype.roomtype} onChange={(e)=>getRoom(e)}
              >
                <option value="volvo">Select Room Type</option>
                <option value="Single room">Single room</option>
                <option value="Double room">Double room</option>
                <option value="Triple room">Triple room</option>
                <option value="Queen room">Queen room</option>
                <option value="King room">King room</option>
                <option value="Twin room">Twin room</option>
                <option value="Deluxe room">Deluxe room</option>
              </select>
              <button onClick={clearALlFilter}>Clear Filter</button>
            </div>
          </div>

          <div className="paginationBar">
            {/* <nav aria-label="pagination"> */}
            <ul class="pagination">
              <li>
                <a href="#" onClick={(e)=>{e.preventDefault();
                  if(pageNumber === 0) return;
                   getPrevPage()}} >
                  <span aria-hidden="true">&laquo;</span>
                  <span class="visuallyhidden">previous set of pages</span>
                </a>
              </li>
              <li>
                <a href="#">
                  <span class="visuallyhidden">page </span>{pageNumber+1}
                </a>
              </li>
              <li>
                <a href="#"  onClick={(e)=>{e.preventDefault();
                  if(AllData?.length<3) return;
                   getNextPage();                  
                }}>
                  <span class="visuallyhidden">next set of pages</span>
                  <span aria-hidden="true">&raquo;</span>
                </a>
              </li>
            </ul>
            {/* </nav> */}
          </div>
        </div>
        <div className="displatRoomSection">

            {
              AllData?.map((each, i)=>(
                <CardRoomDetials each={each} key={i}/>
              ))
            }
            
        </div>
      </div>
    </>
  );
}

export default BrowseAllRooms;
