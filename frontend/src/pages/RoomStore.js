import React from "react";
import '../styles/RoomStyle.css';
import TableComponent from "../components/roomComponent/TableComponent";
import RoomCreateComponent from "../components/roomComponent/RoomCreateComponent";
import Cookies from 'universal-cookie';

const RoomStore = props => {
    const cookies = new Cookies();
        return (
        <div>
            <img className="imgw" alt="no foto" src="crocodileWelcoming.png"/>
            <img className="imgm" alt="no foto" src="crocodileMarsh.png"/>
            <RoomCreateComponent/>
            <TableComponent/>
        </div>
        )

}
export default RoomStore;