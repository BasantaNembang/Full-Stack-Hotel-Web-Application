import React from 'react'
import "./fotter.css"


function Fotter() {
  const date = new Date();

  let year = date.getFullYear();
  

  return (
    <>
        <div className="fotterSections">
            <span>@BN Hotel {year}</span>
        </div>
    
    </>
  )
}

export default Fotter
