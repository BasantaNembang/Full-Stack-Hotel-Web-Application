
import { createContext, useState } from "react";
import Cookies from "universal-cookie";

export const AuthContext = createContext();

export const AuthProvider = ({children}) =>{

    let [jwtToken, SetjwtToken] = useState("");

    let [bol_login, Setbol_login] = useState(false);  //for modal
    let [flag, SetFlag] = useState(true);  //for login page
    
    
    
    let cookes = new Cookies();

    let isAuthenticated = () =>{
        //get from cookies 
        let  jwt = cookes.get("jwtToken");
        if(jwtToken || jwt){
            return true;
        }    
        else{
            return false;
        }
    }

     let data={
       SetjwtToken, isAuthenticated, bol_login, Setbol_login,flag, SetFlag
    }

    return (
        <AuthContext.Provider value={data}>
            {children}
         </AuthContext.Provider>
    )
}





