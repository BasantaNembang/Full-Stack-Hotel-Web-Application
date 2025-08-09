import "./App.css";
import Fotter from "./components/fotter/Fotter";
import Navbar from "./components/navbar/Navbar";
import Home from "./pages/home/Home";
import { useContext, useEffect, useState } from "react";
import { Routes, Route, useLocation } from "react-router";
import BrowseAllRooms from "./pages/browse/BrowseAllRooms";
import AdminSection from "./pages/admin/AdminSection";
import Modal from "./service/Modal/Modal";
import BookingRoom from "./pages/bookRoom/BookingRoom";
import FindBooking from "./pages/find-booking/FindBooking";
import { AuthContext } from "./context/AuthContext";

function App() {
  let [bolean, Setbolean] = useState(false);
  //let [bol_login, Setbol_login] = useState(false); 
  let {bol_login, Setbol_login} = useContext(AuthContext);


  let location =  useLocation();

  let { flag } = location.state || {};
  
  useEffect(() => {
    if (flag) {
       Setbol_login(true);}}
  ,[flag]);



  return (
    <>
      <Navbar bolean={bolean} Setbolean={Setbolean} Setbol_login={Setbol_login} />
       {/* Set here a  model  */}
       {
        bol_login ? (<Modal Setbol_login={Setbol_login}/>): undefined
       }
     
      <Routes>
         <Route path="/" element={<Home bolean={bolean} Setbolean={Setbolean} />} />
         <Route path="/browse" element={<BrowseAllRooms/>} />
         <Route path="/admin" element={<AdminSection/>} />
         <Route path="/booking" element={<BookingRoom/>} />
         <Route path="/sign-up-login" element={<Modal/>} />
         <Route path="/find-booking" element={<FindBooking/>} />
      </Routes>
      <Fotter />
    </>
  );
}

export default App;
