import React from "react";
import "../../styles/Player.css"


const WordPicker = ({item, callBack})=>{
    return(
        <div>
            <ul onClick={()=>{callBack(item[0])}}>{item[0]}</ul>
            <ul onClick={()=>{callBack(item[1])}}>{item[1]}</ul>
            <ul onClick={()=>{callBack(item[2])}}>{item[2]}</ul>
        </div>
    )
}

export default WordPicker