import React from "react";
import "./disimage.css";
import { motion } from "framer-motion";

function DisplayImage({ bolean, Setbolean }) {
  let clickedDisplayImg = () => {
    if (bolean) {
      Setbolean(false);
    }
  };

  return (
    <>
      <div className="disPlayImageContainer" onClick={clickedDisplayImg}>
        <motion.span
          initial={{ y: "2rem", opacity: 0 }}
          animate={{ y: 0, opacity: 1 }}
          transition={{
            duration: 3,
            type: "spring",
          }}
        >
          Welcome to <strong style={{ color: "#4c82ff" }}> BN-Hotel</strong>
        </motion.span>
        <motion.span
          initial={{ y: "2rem", opacity: 0 }}
          animate={{ y: 0, opacity: 1 }}
          transition={{
            duration: 3,
            type: "spring",
          }}
        >
          Experience the Best Hospitality in Town
        </motion.span>
      </div>
    </>
  );
}

export default DisplayImage;
