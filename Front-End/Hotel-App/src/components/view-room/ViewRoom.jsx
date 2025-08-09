import React, { useEffect, useRef, useState } from "react";
import "./viewroom.css";
import { useForm } from "react-hook-form";
import { addRoom, updateSingleRoom } from "../../utils/API-functions-Hotels";


function ViewRoom({HotelDto, roomText,  SetflagAddRoom }) {

  let sendData = new FormData();

  let form = useForm();
  let {register, handleSubmit, formState} = form;
  let {errors} = formState;
  let [selectedImage, SetSelectedImages] = useState();

  let [flag, Setflag] = useState(false);

  let refFile = useRef();

  
  let [data, Setdata] = useState({
    roomtype: "",
    roomprice: 0.00      
  })
  
  let [fileMessage, SetfileMessage] = useState("");
  let [successMsg, SetsuccessMsg] = useState("");



  let trackField = (e) =>{
    let {value, name} = e.target;
   
    Setdata({...data, [name]: value });

   
  }



  let backToAdmin = () => {
    SetflagAddRoom((prev) => !prev);
  };

  let trackUploadImage = (e) => {  //file-upload
    SetSelectedImages(URL.createObjectURL(e.target.files[0]));

    if(e.target.files[0]){
      Setflag(true);
    }

  };

  let deleteImage = () => {
    SetSelectedImages("");
    refFile.current.value = "";
    Setflag(false);
  };
  


  //import form utils
  let addNewRoom = async () =>{
   
    //update the old one
    if(HotelDto.hid){
      
        sendData.append("data", JSON.stringify(data));

        if(selectedImage === ""){  //if user has remove the old pic
          //alert the file form validation okey....
          SetfileMessage("Please Select Image")
          return;
        }

      let file = refFile.current.files;
      if(file.length!=0){
          sendData.append("file", file ? file[0] : new Blob);}
     
      
      let flag =  await updateSingleRoom(HotelDto.hid, sendData);
      //console.log(flag)
        if(flag){
            SetsuccessMsg("Room updated successfully...")

            setTimeout(() => {
              SetsuccessMsg("")
            }, 3000);
          }else{
            throw errors("Something went wrong...");
          }
     
    }
    

   //add the new one
   else{
    let file = refFile.current.files;
    if(file[0]){   //if file present
    let file = refFile.current.files;
    sendData.append("file", file ? file[0] : new Blob);
    sendData.append("data", JSON.stringify(data));
    let flag = await addRoom(sendData);
   
     //clear the form and set the success message as well
          if(flag){
          Setdata({
            roomtype:"",
            roomprice: ""});
            SetSelectedImages("");
            Setflag(false);
            refFile.current.value = "";
            SetsuccessMsg("Room added successfully...")

            setTimeout(() => {
              SetsuccessMsg("")
            }, 3000);
          }else{
            throw errors("Something went wrong...");
          }
      }else{
      SetfileMessage("Please Select Image")
      return;
      } 
   }   
  }


 
   useEffect(()=>{
    if(HotelDto.hid){
      Setdata({
        roomtype: HotelDto.roomtype, 
        roomprice: HotelDto.roomprice });
    }
  
   //for image
   if(selectedImage===undefined){
     SetSelectedImages(HotelDto.imageurl)
  }
  }, [HotelDto.hid] );
 


  return (
    <>
      <div className="viewRoomContainer">
        <form action="#" onSubmit={handleSubmit(addNewRoom)}>
          <div className="infoViewRoom">
            <h3>{roomText}</h3>
               {
                successMsg.length>0 ? <span className="successSpan">{successMsg}</span>  : undefined
               }
            <div className="viewRoomDiv">
              <label htmlFor="filterBuRoom0">Room Type :</label> <br />
              <select  id="filterBuRoom0" value={data.roomtype}
               {...register("roomtype", {required:{value:true,  message:"Please Select Room Type"}})}
               onChange={trackField} >
                <option value="">Select a room type </option>
                <option  value="Single room">Single room</option>
                <option value="Double room">Double room</option>
                <option value="Triple room">Triple room</option>
                <option value="Queen room">Queen room</option>
                <option value="King room">King room</option>
                <option value="Twin room">Twin room</option>
                <option value="Deluxe room">Deluxe room</option>
              </select>
            </div>
            <span className="errorAdmin">{errors.roomtype?.message}</span>


            <div className="viewRoomDiv">
              <label htmlFor="price">Enter Price</label> <br />
              <input type="number" name="roomprice" id="price" value={data.roomprice} 
              {...register("roomprice", {required:{value:true,  message:"Please Enter Amount"}})}
              onChange={trackField} />
            </div>
            <span className="errorAdmin">{errors.roomprice?.message}</span>


            <div className="viewRoomDiv">
              <label class="custom-file-upload">
                
                <input type="file"  className={!flag?  "makeStableImage" : undefined}    ref={refFile}
                
                onChange={trackUploadImage} />
                Image
              </label>
            </div>
          </div>
          {fileMessage.length>0 ? <span className="errorAdmin">{fileMessage}</span> :  undefined}
         



          <div className="viewImage">
            {
              selectedImage && 
              (
                <>
                 <span onClick={deleteImage} >x</span> 
                 <img src={selectedImage} alt="image" /> 
                </>
              )

            }
           
            {/* */}
         
          
              <div className="viewRoomBTN">
                <button onClick={backToAdmin}>Back</button>
                <button type="submit">Save Room</button>
              </div>

          </div>
          
        </form>
      </div>
    </>
  );
}

export default ViewRoom;
